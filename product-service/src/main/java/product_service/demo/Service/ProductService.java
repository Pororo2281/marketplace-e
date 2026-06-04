package product_service.demo.Service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import product_service.demo.Entity.ProductAttributeEntity;
import product_service.demo.Entity.ProductEntity;
import product_service.demo.Entity.ProductImageEntity;
import product_service.demo.Enum.ProductEventType;
import product_service.demo.Enum.ProductStatus;
import product_service.demo.Event.ProductBatchEvent;
import product_service.demo.Event.ProductEvent;
import product_service.demo.Exception.NotFoundById;
import product_service.demo.Exception.ProductArchivedException;
import product_service.demo.Exception.ProductImagesRequiredException;
import product_service.demo.Exception.ProductModificationNotAllowedException;
import product_service.demo.Mapper.ProductEventMapper;
import product_service.demo.Mapper.ProductMapper;
import product_service.demo.MapperToEntity.AttributeEntityMapper;
import product_service.demo.Pageable.CreatePageable;
import product_service.demo.RabbitMq.RabbitProducer;
import product_service.demo.Repository.CategoryRepo;
import product_service.demo.Repository.ProductRepo;
import product_service.demo.Request.CreateProductRequest;
import product_service.demo.Request.UpdatePriceRequest;
import product_service.demo.Request.UpdateProductRequest;
import product_service.demo.Request.UpdateStockRequest;
import product_service.demo.Response.ProductImageResponse;
import product_service.demo.Response.ProductResponse;
import product_service.demo.Specifications.ProductSpecifications;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepo repository;
    private final CategoryRepo categoryRepo;
    private final ObjectMapper objectMapper;
    private final FileService fileService;
    private final ProductMapper productMapper;
    private final RabbitProducer producer;

    public ProductService(ProductRepo repository, CategoryRepo categoryRepo, ObjectMapper objectMapper, FileService fileService, ProductMapper productMapper, RabbitProducer producer) {
        this.repository = repository;
        this.categoryRepo = categoryRepo;
        this.objectMapper = objectMapper;
        this.fileService = fileService;
        this.productMapper = productMapper;
        this.producer = producer;
    }

    public Page<ProductResponse> getProductsByCategory(Long id, Pageable pageable) {
        if (!categoryRepo.existsById(id)) {
            throw new NotFoundById("Category with id: " + id + " not found");
        }
        return repository.findByCategoryId(id, pageable)
                .map(productMapper::entityToProduct);
    }

    public Page<ProductResponse> getAllProducts(Pageable pageable,
                                                Long categoryId,
                                                BigDecimal minPrice,
                                                BigDecimal maxPrice,
                                                String search,
                                                Long sellerId,
                                                String sort
    ) {

        Pageable sortedPageable = CreatePageable.createPageableWithSort(pageable, sort);

        Specification specification = Specification.where(ProductSpecifications.hasCategory(categoryId))
                .and(ProductSpecifications.hasMinPrice(minPrice))
                .and(ProductSpecifications.hasMaxPrice(maxPrice))
                .and(ProductSpecifications.hasSearch(search))
                .and(ProductSpecifications.hasSeller(sellerId))
                .and(ProductSpecifications.hasStatusActive());

        Page<ProductEntity> products = repository.findAll(specification, sortedPageable);

        return products.map(productMapper::entityToProduct);
    }

    public ProductResponse getProductById(Long id) {
        var productEntity = repository.findById(id)
                .orElseThrow(() -> new NotFoundById("Product with id: " + id + " not found"));
        if (productEntity.getStatus()!= ProductStatus.ACTIVE) {
            throw new NotFoundById("Product with id: " + id + " not found");
        }
        return productMapper.entityToProduct(productEntity);
    }

    public List<ProductResponse> getFeaturedProducts(int limit) {

        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by(Sort.Direction.DESC, "salesCount")
        );
        List<ProductEntity> featuredProducts = repository.findByStatus(ProductStatus.ACTIVE, pageable).getContent();
        return featuredProducts.stream()
                .map(productMapper::entityToProduct)
                .toList();
    }

    public List<ProductResponse> getNewProducts(int limit) {
        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );
        List<ProductEntity> newProducts = repository.findByStatus(ProductStatus.ACTIVE, pageable).getContent();
        return newProducts.stream()
                .map(productMapper::entityToProduct)
                .toList();
    }

    public List<ProductResponse> getPopularProducts(int limit) {
        Pageable pageable = PageRequest.of(
                0,
                limit,
                Sort.by(Sort.Direction.DESC, "salesCount")
        );
        List<ProductEntity> featuredProducts = repository.findByStatus(ProductStatus.ACTIVE, pageable).getContent();
        return featuredProducts.stream()
                .map(productMapper::entityToProduct)
                .toList();
    }


    public Page<ProductResponse> getSellerProducts(int page,
                                                   int size,
                                                   ProductStatus status,
                                                   Long categoryId,
                                                   Long sellerId) {
        Pageable pageable = PageRequest.of(
                page,
                size
        );

        Specification<ProductEntity> specification = ProductSpecifications.hasCategory(categoryId)
                .and(ProductSpecifications.hasStatus(status)
                .and(ProductSpecifications.hasSeller(sellerId)));
        Page<ProductEntity> products = repository.findAll(specification, pageable);
        return products.map(productMapper::entityToProduct);
    }

    @Transactional
    public ProductResponse createProduct(String json, List<MultipartFile> images,Long sellerId) {

        CreateProductRequest request = objectMapper.readValue(json, CreateProductRequest.class);

        var product = new ProductEntity();
        product.setTitle(request.getTitle());
        product.setSellerId(sellerId);
        product.setStatus(ProductStatus.DRAFT);
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());
        product.setCategory(categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() -> new NotFoundById("Category with id: " + request.getCategoryId() + " not found")));
        product.setCreatedAt(Instant.now());
        product.setUpdatedAt(Instant.now());
        if (request.getAttributes() != null) {
            product.setAttributes(request.getAttributes().stream()
                    .map(x->{
                        ProductAttributeEntity attribute = AttributeEntityMapper.attributeToEntity(x);
                        attribute.setProduct(product);
                        return attribute;
                    })
                    .toList());
        }
        ProductEntity savedProduct = repository.save(product);
        List<ProductImageEntity> productImages = saveProductImages(images, savedProduct);
        if (savedProduct.getImages() == null) {
            savedProduct.setImages(new ArrayList<>());
        }
        savedProduct.getImages().addAll(productImages);

        ProductEntity finalProduct = repository.save(savedProduct);

        ProductEvent productEvent = ProductEventMapper.createProductEvent(finalProduct, ProductEventType.CREATED);

        producer.sendProductEvent(productEvent);

        return productMapper.entityToProduct(finalProduct);
    }

    private List<ProductImageEntity> saveProductImages(List<MultipartFile> images, ProductEntity product) {
        List<ProductImageEntity> productImages = new ArrayList<>();
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        for (int i = 0; i < images.size(); i++) {
            MultipartFile imageFile = images.get(i);


            String imageUrl = fileService.uploadProductFile(
                    imageFile,  product.getId().toString()
            );

            ProductImageEntity imageEntity = new ProductImageEntity();
            imageEntity.setProduct(product);
            imageEntity.setUrl(imageUrl);
            int startIndex = product.getImages() != null ? product.getImages().size() : 0;
            imageEntity.setSortOrder(startIndex + i);
            if (product.getImages() == null || product.getImages().stream().filter(x->x.getIsMain()).findFirst().isEmpty()){
                imageEntity.setIsMain(i == 0);
            }
            productImages.add(imageEntity);
        }
        return productImages;
    }

    public ProductResponse getProductByIdForSeller(Long id,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(() -> new NotFoundById("Product with id: " + id + " not found"));
        if (productEntity.getStatus()== ProductStatus.ARCHIVED){
            throw new NotFoundById("Product with id: " + id + " not found");
        }
        return productMapper.entityToProduct(productEntity);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest updateRequest,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(() -> new NotFoundById("Product with id: " + id + " not found"));

        if (updateRequest.getCategoryId() != null) {
            productEntity.setCategory(categoryRepo.findById(updateRequest.getCategoryId())
                    .orElseThrow(() -> new NotFoundById("Category with id: " + updateRequest.getCategoryId() + " not found")));
        }
        if (updateRequest.getOldPrice()!= null){
            productEntity.setOldPrice(updateRequest.getOldPrice());
        }
        if (updateRequest.getPrice() != null){
            productEntity.setPrice(updateRequest.getPrice());
        }
        if (updateRequest.getTitle() != null){
            productEntity.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null) {
            productEntity.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getStockQuantity() != null) {
            productEntity.setStockQuantity(updateRequest.getStockQuantity());
        }
        if (updateRequest.getStatus() != null) {
            productEntity.setStatus(updateRequest.getStatus());
        }
        if (updateRequest.getAttributes()!=null){
            productEntity.getAttributes().clear();
            productEntity.getAttributes().addAll(updateRequest.getAttributes().stream()
                    .map(x->{
                        ProductAttributeEntity attribute = AttributeEntityMapper.attributeToEntity(x);
                        attribute.setProduct(productEntity);
                        return attribute;
                    })
                    .toList());
        }

        ProductEvent productEvent = ProductEventMapper.createProductEvent(productEntity, ProductEventType.UPDATED);
        producer.sendProductEvent(productEvent);

        repository.save(productEntity);

        return productMapper.entityToProduct(productEntity);
    }

    @Transactional
    public ProductResponse publishProduct(Long id,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));
        if (productEntity.getStatus()==ProductStatus.ACTIVE){
            throw new ProductModificationNotAllowedException("Product with id: " + id + " is already published");
        }
        if (productEntity.getStatus() == ProductStatus.ARCHIVED) {
            throw new ProductModificationNotAllowedException("Archived product cannot be published");
        }
        if (productEntity.getStatus()==ProductStatus.DRAFT){
            productEntity.setStatus(ProductStatus.ACTIVE);
            repository.save(productEntity);
        }
        return productMapper.entityToProduct(productEntity);
    }

    @Transactional
    public ProductResponse archiveProduct(Long id,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));
        if (productEntity.getStatus()==ProductStatus.ARCHIVED){
            throw new ProductModificationNotAllowedException("Product with id: " + id + " is already archived");
        }
        productEntity.setStatus(ProductStatus.ARCHIVED);

        ProductEvent productEvent = ProductEventMapper.createProductEvent(productEntity,ProductEventType.DELETED);
        producer.sendProductEvent(productEvent);

        repository.save(productEntity);
        return productMapper.entityToProduct(productEntity);
    }

    @Transactional
    public ProductResponse updateStock(Long id, UpdateStockRequest request,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));
        if (productEntity.getStatus() == ProductStatus.ARCHIVED) {
            throw new ProductModificationNotAllowedException("Cannot update stock of archived product");
        }
        productEntity.setStockQuantity(request.getStockQuantity());
        repository.save(productEntity);
        return productMapper.entityToProduct(productEntity);
    }

    @Transactional
    public ProductResponse updatePrice(Long id, UpdatePriceRequest request,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));
        if (productEntity.getStatus() == ProductStatus.ARCHIVED) {
            throw new ProductModificationNotAllowedException("Cannot update price of archived product");
        }
        if (request.getPrice()!= null){
            productEntity.setPrice(request.getPrice());
        }
        if (request.getOldPrice() != null){
            productEntity.setOldPrice(request.getOldPrice());
        }

        ProductEvent productEvent = ProductEventMapper.createProductEvent(productEntity, ProductEventType.UPDATED);
        producer.sendProductEvent(productEvent);

        repository.save(productEntity);
        return productMapper.entityToProduct(productEntity);
    }

    public ProductResponse addProductImages(Long id, List<MultipartFile> images,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));
        if (productEntity.getStatus() == ProductStatus.ARCHIVED) {
            throw new ProductArchivedException("Cannot add images to archived product");
        }
        List<ProductImageEntity> productImages = saveProductImages(images, productEntity);
        if (productImages==null || productImages.isEmpty()){
            throw new ProductImagesRequiredException("No images to add");
        }
        if (productEntity.getImages() == null) {
            productEntity.setImages(new ArrayList<>());
            productEntity.setImages(productImages);
        }
        productEntity.getImages().addAll(productImages);
        repository.save(productEntity);
        return productMapper.entityToProduct(productEntity);
    }

    public void deleteProductImage(Long id, Long imageId,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));
        if (productEntity.getStatus() == ProductStatus.ARCHIVED) {
            throw new ProductModificationNotAllowedException("Cannot delete images of archived product");
        }
        ProductImageEntity image = productEntity.getImages().stream().filter(x->x.getId().equals(imageId)).findFirst()
                .orElseThrow(()-> new NotFoundById("Image with id: " + imageId + " not found"));
        productEntity.getImages().remove(image);
        repository.save(productEntity);
        fileService.deleteFile(image.getUrl(),"products");
    }

    public ProductImageResponse setMainProductImage(Long id, Long imageId,Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));

        if (productEntity.getStatus() == ProductStatus.ARCHIVED) {
            throw new ProductModificationNotAllowedException("Cannot delete images of archived product");
        }

        ProductImageEntity imageEntity = productEntity.getImages().stream()
                .filter(x->x.getId().equals(imageId))
                .findFirst()
                .orElseThrow(()-> new NotFoundById("Image with id: " + imageId + " not found"));
        if (Boolean.TRUE.equals(imageEntity.getIsMain())){
            return new ProductImageResponse(imageEntity.getId(), imageEntity.getUrl(), imageEntity.getSortOrder());
        }

        productEntity.getImages().stream()
                .forEach(image->image.setIsMain(false));

        imageEntity.setIsMain(true);
        repository.save(productEntity);

        return new ProductImageResponse(imageEntity.getId(), imageEntity.getUrl(), imageEntity.getSortOrder());
    }

    public Page<ProductResponse> search(String query ,
                                  Long categoryId,
                                  BigDecimal minPrice,
                                  BigDecimal maxPrice,
                                  Pageable pageable) {
        Specification<ProductEntity> specification = Specification.where(ProductSpecifications.hasSearch(query))
                .and(ProductSpecifications.hasStatusActive())
                .and(ProductSpecifications.hasMinPrice(minPrice))
                .and(ProductSpecifications.hasMaxPrice(maxPrice))
                .and(ProductSpecifications.hasCategory(categoryId));
       Page<ProductResponse> productResponses =  repository.findAll(specification, pageable).map(productMapper::entityToProduct);
       return productResponses;
    }

    public List<String> getSearchSuggestions(String q) {
        if (q == null || q.trim().isEmpty()) {
            return List.of();
        }

        return repository.findSuggestions(q.trim());
    }

    public Page<ProductResponse> getProductsBySellerId(Long sellerId,Pageable pageable) {
        if (sellerId==null){
            throw new RuntimeException("Seller id must be provided");
        }
        Specification<ProductEntity> specification = Specification.where(ProductSpecifications.hasSeller(sellerId))
                .and(ProductSpecifications.hasStatusActive());

        return repository.findAll(specification, pageable)
                .map(productMapper::entityToProduct);
    }

     public Page<ProductResponse> getAllProductsForAdmin(ProductStatus status, Long sellerId, Pageable pageable) {
        Specification<ProductEntity> spec = Specification.where(ProductSpecifications.hasSeller(sellerId)
        .and(ProductSpecifications.hasStatus(status)));
         return   repository.findAll(spec, pageable).map(productMapper::entityToProduct);
    }

    public void forceDeleteProduct(Long id) {
        var productEntity = repository.findById(id)
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));
        repository.delete(productEntity);
    }

    public void deleteProduct(Long id, Long sellerId) {
        var productEntity = repository.findById(id)
                .filter(product->product.getSellerId().equals(sellerId))
                .orElseThrow(()-> new NotFoundById("Product with id: " + id + " not found"));

        ProductEvent productEvent = ProductEventMapper.createProductEvent(productEntity, ProductEventType.DELETED);
        producer.sendProductEvent(productEvent);
        repository.delete(productEntity);

    }

    public void reindexProducts() {
        List<ProductEntity> productEntities =
                repository.findAllByStatus(ProductStatus.ACTIVE);

        List<ProductEvent> productEvents = productEntities.stream()
                .map(e -> ProductEventMapper.createProductEvent(e, ProductEventType.UPDATED))
                .toList();

        int batchSize = 1000;

        for (int i = 0; i < productEvents.size(); i += batchSize) {
            int end = Math.min(i + batchSize, productEvents.size());

            List<ProductEvent> batch = productEvents.subList(i, end);
            ProductBatchEvent batchEvent = new ProductBatchEvent();
            batchEvent.setEvents(batch);
            batchEvent.setType(ProductEventType.UPDATED);
            producer.reindex(batchEvent);
        }
    }
}


