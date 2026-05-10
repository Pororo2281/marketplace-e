package order_service.order.Service;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import order_service.order.Exception.NotFoundById;
import order_service.order.Repository.OrderItemRepo;
import order_service.order.Repository.PurchasedProductRepo;
import org.apache.tomcat.util.http.Method;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class DownloadService {

    private final PurchasedProductRepo repo;
    private final MinioClient minioClient;

    public DownloadService(PurchasedProductRepo repo, MinioClient minioClient) {
        this.repo = repo;
        this.minioClient = minioClient;
    }

    // сделать проверку на accessCount
    public String getDownloadLink(Long orderItemId, Long userId) {
        var purchasedProduct = repo.findByOrderItemIdAndUserId(orderItemId,userId)
                .orElseThrow(()-> new NotFoundById("purchasedProduct with id: " + orderItemId + " and userId: " + userId +  " not found"));
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(io.minio.http.Method.valueOf(Method.GET))
                            .bucket("products")
                            .object(purchasedProduct.getDownloadUrl())
                            .expiry(60 * 20)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
