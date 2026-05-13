package order_service.order.Service;

import com.zaxxer.hikari.util.ConcurrentBag;
import feign.FeignException;
import order_service.order.Client.ProductClient;
import order_service.order.Entity.CartItemEntity;
import order_service.order.Exception.InsufficientStockException;
import order_service.order.Exception.NotFoundById;
import order_service.order.Exception.ProductOutOfStockException;
import order_service.order.Exception.ProductServiceException;
import order_service.order.Mapper.EntityToCart;
import order_service.order.Mapper.EntityToCartItem;
import order_service.order.Repository.CartItemRepo;
import order_service.order.Request.AddToCartRequest;
import order_service.order.Request.UpdateCartItemRequest;
import order_service.order.Request.UpdateOrderStatusRequest;
import order_service.order.Response.CartItemResponse;
import order_service.order.Response.CartResponse;
import order_service.order.Response.OrderDetailResponse;
import order_service.order.Response.ProductResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    private final CartItemRepo cartItemRepo;
    private final ProductClient productClient;

    public CartService(CartItemRepo cartItemRepo, ProductClient productClient) {
        this.cartItemRepo = cartItemRepo;
        this.productClient = productClient;
    }

    public CartResponse getCart(Long userId){
        var cartItems = cartItemRepo.findByUserIdOrderByCreatedAtDesc(userId);
        List<CartItemResponse> cartItemResponses = cartItems.stream()
                .map(EntityToCartItem::entityToCartItem)
                .toList();
        return EntityToCart.entityToCart(cartItemResponses,userId);
    }

    @Transactional
    public CartItemResponse addItem(Long userId, AddToCartRequest request) {

        ProductResponse product;
        try {
            product = productClient.getProductById(request.getProductId());
        } catch (FeignException.NotFound e) {
            throw new NotFoundById("Product not found with id: " + request.getProductId());
        } catch (FeignException e) {
            throw new ProductServiceException("Product service error: " + e.status());
        }


        if (product.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(
                    "Requested quantity " + request.getQuantity() +
                            " exceeds available stock " + product.getStockQuantity()
            );
        }

        if (product.getStockQuantity() == 0) {
            throw new ProductOutOfStockException("Product out of stock");
        }

        Optional<CartItemEntity> cartItemEntity = cartItemRepo.findByUserIdAndProductId(userId,request.getProductId());
        if (cartItemEntity.isPresent()){
            Integer newQuantity = cartItemEntity.get().getQuantity() + request.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                throw new InsufficientStockException(
                        "Cannot add " + request.getQuantity() + " more items. " +
                                "Available: " + product.getStockQuantity() +
                                ", In cart: " + cartItemEntity.get().getQuantity()
                );
            }
            CartItemEntity item = cartItemEntity.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepo.save(item);
            return EntityToCartItem.entityToCartItem(item);
        }

        CartItemEntity entity = new CartItemEntity();
        entity.setUserId(userId);
        entity.setProductId(product.getId());
        entity.setQuantity(request.getQuantity());

        entity.setProductTitle(product.getTitle());
        entity.setProductPrice(product.getPrice());
        entity.setProductImageUrl(product.getMainImageUrl());

        entity.setCategoryName(product.getCategoryName());

        entity.setSellerId(product.getSellerId());
        entity.setSellerName(product.getSellerName());

        System.out.println(product.getPrice());
        System.out.println(product.getTitle());

        cartItemRepo.save(entity);

        return EntityToCartItem.entityToCartItem(entity);
    }

    @Transactional
    public CartItemResponse updateStock(Long productId, UpdateCartItemRequest request, Long userId) {
        ProductResponse productResponse = productClient.getProductById(productId);

        if (productResponse.getStockQuantity() < request.getQuantity()) {
            throw new InsufficientStockException(
                    "Requested quantity " + request.getQuantity() +
                            " exceeds available stock " + productResponse.getStockQuantity()
            );
        }

        var cartItemEntity = cartItemRepo.findByUserIdAndProductId(userId,productId)
                .orElseThrow(()-> new NotFoundById("not found by id: " + productId));

        Integer newQuantity = cartItemEntity.getQuantity()+request.getQuantity();
        if (productResponse.getStockQuantity()<newQuantity){

            throw new InsufficientStockException(
                    "Cannot add " + request.getQuantity() + " more items. " +
                            "Available: " + productResponse.getStockQuantity() +
                            ", In cart: " + cartItemEntity.getQuantity()
           );
        }

        cartItemEntity.setQuantity(cartItemEntity.getQuantity() + request.getQuantity());
        cartItemRepo.save(cartItemEntity);
        return EntityToCartItem.entityToCartItem(cartItemEntity);
    }

    @Transactional
    public void deleteItem(Long productId, Long userId) {
        var cartItemEntity  = cartItemRepo.findByUserIdAndProductId(userId,productId)
                .orElseThrow(()-> new NotFoundById("cartItem wit productId: " + productId + " and userId: " + userId + " not found"));
        cartItemRepo.delete(cartItemEntity);
    }

    @Transactional
    public void deleteAllCart(Long userId) {
        cartItemRepo.deleteAllCartByUserId(userId);
    }

}
