package webProject.togetherPartyTonight.infra.S3;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import webProject.togetherPartyTonight.domain.review.exception.ReviewException;
import webProject.togetherPartyTonight.global.error.CommonException;
import webProject.togetherPartyTonight.global.error.ErrorCode;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Setter
public class S3Service {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final String s3regionUrl  = ".s3.ap-northeast-2.amazonaws.com/";

    private final String http = "https://";

    public String uploadImage(MultipartFile multipartFile, String directory, Long userId) {

        String s3Url = http+bucket+ s3regionUrl;

        String fileName = directory+ userId.toString()+"/"+ createFileName(multipartFile.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try(InputStream inputStream = multipartFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            }
        catch(IOException e) {
            throw new ReviewException(ErrorCode.S3_UPLOAD_FAIL);
            }
        return s3Url + fileName;
    }

    public void deleteImage(String fileName) {
        String[] split = fileName.split("/");

        String key = fileName.substring(72);
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, key));
    }

    private String createFileName(String fileName) {
        //같은 이름의 파일 중복 방지하기 위해 UUID사용
        String extension = getFileExtension(fileName);
        String uuid = UUID.randomUUID().toString().concat(extension);
        return uuid;
    }

    private String getFileExtension(String fileName) {
        try {
            String extension = fileName.substring(fileName.lastIndexOf("."));
            return checkImageExtension(extension);
        }
        catch (StringIndexOutOfBoundsException e) {
            throw new CommonException(ErrorCode.INVALID_EXTENSION);
        }
    }

    private String  checkImageExtension(String extension) {
        String[] image = {".jpg", ".png", ".jpeg", ".svg"};
        boolean check = false;
        for (String ex : image) {
            if (extension.equals(ex) || extension.equals(ex.toUpperCase())) {
                check = true;
                break;
            }
        }
        if (!check)  throw new CommonException(ErrorCode.INVALID_IMAGE_EXTENSION);
        return extension;
    }

    public String getImage(String name) {
        return http+bucket+s3regionUrl+name;
    }
}
