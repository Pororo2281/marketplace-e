package product_service.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import product_service.demo.Entity.ProductEntity;
import product_service.demo.Entity.ProductImageEntity;
import product_service.demo.Enum.ProductStatus;
import product_service.demo.Exception.NotFoundById;
import product_service.demo.Mapper.ProductMapper;
import product_service.demo.Repository.ProductRepo;
import product_service.demo.Request.CheckAvailabilityItem;
import product_service.demo.Request.CheckAvailabilityRequest;
import product_service.demo.Response.CheckAvailabilityResponse;
import product_service.demo.Response.ProductAvailabilityResponse;
import product_service.demo.Response.ProductResponse;


import java.util.ArrayList;
import java.util.List;

@Service
public class InternalProductService {

    private final ProductRepo productRepository;
    private final ProductMapper productMapper;

    public InternalProductService(ProductRepo productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Transactional(readOnly = true)
    public CheckAvailabilityResponse checkAvailability(CheckAvailabilityRequest request) {
        List<ProductAvailabilityResponse> availabilityList = new ArrayList<>();
        boolean allAvailable = true;
        int notFoundCount = 0;
        int insufficientStockCount = 0;
        int inactiveCount = 0;

        for (CheckAvailabilityItem item : request.getItems()) {
            ProductAvailabilityResponse availability = checkSingleProduct(item);
            availabilityList.add(availability);

            if (!availability.getAvailable()) {
                allAvailable = false;

                if (availability.getUnavailableReason().contains("not found")) {
                    notFoundCount++;
                } else if (availability.getUnavailableReason().contains("Insufficient stock")) {
                    insufficientStockCount++;
                } else if (availability.getUnavailableReason().contains("not active")) {
                    inactiveCount++;
                }
            }
        }

        String message = buildMessage(allAvailable, notFoundCount, insufficientStockCount, inactiveCount);

        return new CheckAvailabilityResponse(allAvailable, availabilityList, message);
    }


    private ProductAvailabilityResponse checkSingleProduct(CheckAvailabilityItem item) {
        ProductAvailabilityResponse response = new ProductAvailabilityResponse();
        response.setProductId(item.getProductId());
        response.setRequestedQuantity(item.getQuantity());

        ProductEntity product = productRepository.findById(item.getProductId()).orElse(null);

        if (product == null) {
            response.setAvailable(false);
            response.setAvailableQuantity(0);
            response.setUnavailableReason("Product not found");
            return response;
        }

        response.setTitle(product.getTitle());
        response.setPrice(product.getPrice());
        response.setAvailableQuantity(product.getStockQuantity());
        response.setStatus(product.getStatus().name());
        response.setSellerId(product.getSellerId());
        response.setImageUrl(getMainImageUrl(product));

        if (product.getStatus() != ProductStatus.ACTIVE) {
            response.setAvailable(false);
            response.setUnavailableReason("Product is not active (status: " + product.getStatus() + ")");
            return response;
        }

        if (product.getStockQuantity() == null || product.getStockQuantity() < item.getQuantity()) {
            response.setAvailable(false);
            response.setUnavailableReason(
                    String.format("Insufficient stock. Requested: %d, Available: %d",
                            item.getQuantity(),
                            product.getStockQuantity() != null ? product.getStockQuantity() : 0)
            );
            return response;
        }

        response.setAvailable(true);
        return response;
    }

    private String getMainImageUrl(ProductEntity product) {
        if (product.getImages() == null || product.getImages().isEmpty()) {
            return null;
        }

        return product.getImages().stream()
                .filter(ProductImageEntity::getIsMain)
                .findFirst()
                .map(ProductImageEntity::getUrl)
                .orElse(product.getImages().get(0).getUrl());
    }


    private String buildMessage(boolean allAvailable, int notFoundCount, int insufficientStockCount, int inactiveCount) {
        if (allAvailable) {
            return null;
        }

        List<String> problems = new ArrayList<>();

        if (notFoundCount > 0) {
            problems.add(notFoundCount + " product(s) not found");
        }

        if (insufficientStockCount > 0) {
            problems.add(insufficientStockCount + " product(s) have insufficient stock");
        }

        if (inactiveCount > 0) {
            problems.add(inactiveCount + " product(s) are not active");
        }

        if (problems.isEmpty()) {
            return "Some products are not available";
        }

        return String.join(", ", problems);
    }

    @Transactional
    public boolean reverse(CheckAvailabilityRequest request) {
        CheckAvailabilityResponse availability = checkAvailability(request);
        if (!availability.getAvailable()) {
            return false;
        }
        for (CheckAvailabilityItem item : request.getItems()) {
            ProductEntity product = productRepository.findById(item.getProductId()).orElseThrow();
            int newStock = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(newStock);
            productRepository.save(product);
        }
        return true;
    }

    public void reduceStock(CheckAvailabilityRequest request) {
        for (CheckAvailabilityItem item : request.getItems()) {
            ProductEntity product = productRepository.findById(item.getProductId()).orElseThrow();
            int newStock = product.getStockQuantity() - item.getQuantity();
            product.setStockQuantity(newStock);
            productRepository.save(product);
        }
    }

    @Transactional
    public void returnStock(CheckAvailabilityRequest request) {

        for (CheckAvailabilityItem item : request.getItems()) {

            ProductEntity product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new NotFoundById("Product not found"));

            product.setStockQuantity(
                    product.getStockQuantity() + item.getQuantity()
            );

            productRepository.save(product);
        }
    }

    public List<ProductResponse> getProductsBatch(List<Long> ids) {
        List<ProductEntity> products = productRepository.findAllById(ids);

        return products.stream()
                .map(productMapper::entityToProduct)
                .toList();
    }

    public Long getProductByProductId(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundById("Product not found"))
                .getSellerId();
    }

    public ProductResponse getSellerIdByProductId(Long productId) {
        return productRepository.findById(productId)
                .map(productMapper::entityToProduct)
                .orElseThrow(() -> new NotFoundById("Product not found"));
    }

    public void approveProduct(Long id) {
     var product =  productRepository.findById(id)
                .orElseThrow(() -> new NotFoundById("Product not found by id " + id));
     product.setStatus(ProductStatus.ACTIVE);
    }

    public void rejectProduct(Long id) {
        var product =  productRepository.findById(id)
                .orElseThrow(() -> new NotFoundById("Product not found by id " + id));
        product.setStatus(ProductStatus.REJECTED);
    }
}