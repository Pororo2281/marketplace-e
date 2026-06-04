package product_service.demo.Service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.*;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import product_service.demo.Exception.FileStorageException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Service
public class FileService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucketName;


    public FileService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }


    public String uploadProductFile(MultipartFile file, String folder) {

        try {
            String extension = "";

            String originalFilename = file.getOriginalFilename();
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }

            String objectName = folder + "/" + file.getOriginalFilename() + "_" + UUID.randomUUID() + extension;

            InputStream inputStream = file.getInputStream();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
            inputStream.close();
            return objectName;

        } catch (Exception e) {
            throw new FileStorageException(
                    "Failed to upload file to MinIO",
                    e
            );
        }
    }

    public void deleteFile(String fileUrl,String bucketName){
        try{
            URI uri = new URI(fileUrl);
            String path = uri.getPath();
            String objectName = path.substring(path.indexOf(bucketName));
            objectName = objectName.substring(bucketName.length()+1);

            RemoveObjectArgs args = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build();
            minioClient.removeObject(args);

        } catch (
                ServerException |
                InsufficientDataException |
                ErrorResponseException |
                IOException |
                NoSuchAlgorithmException |
                InvalidKeyException |
                InvalidResponseException |
                XmlParserException |
                InternalException |
                URISyntaxException e
        ) {
            throw new FileStorageException(
                    "Failed to delete file from MinIO",
                    e
            );
        }
    }

}
