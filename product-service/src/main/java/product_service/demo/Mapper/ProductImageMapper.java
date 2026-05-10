package product_service.demo.Mapper;

import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import product_service.demo.Entity.ProductImageEntity;
import product_service.demo.Response.ProductImageResponse;

@Component
public class ProductImageMapper {

    private final MinioClient minioClient;

    public ProductImageMapper(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public  ProductImageResponse entityToImage(ProductImageEntity image) {
        ProductImageResponse response = new ProductImageResponse();
        response.setId(image.getId());
        response.setImageUrl(getImageUrl(image.getUrl()));
        response.setSortOrder(image.getSortOrder());
        return response;
    }

    @PostConstruct
    public void init() {
        try {
            String bucketName = "products";

            String policy = """
            {
              "Version":"2012-10-17",
              "Statement":[
                {
                  "Effect":"Allow",
                  "Principal":"*",
                  "Action":["s3:GetObject"],
                  "Resource":["arn:aws:s3:::%s/*"]
                }
              ]
            }
            """.formatted(bucketName);

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(bucketName)
                            .config(policy)
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error setting bucket policy", e);
        }
    }

    private String getImageUrl(String objectKey) {
        return "http://localhost:9000/products/" + objectKey;
    }
}
