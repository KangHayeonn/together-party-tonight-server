package webProject.togetherPartyTonight.global.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import io.lettuce.core.dynamic.annotation.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import webProject.togetherPartyTonight.global.util.YamlPropertySourceFactory;


@ConfigurationProperties(prefix = "cloud.aws.credentials")
@ConstructorBinding
@RequiredArgsConstructor
@Getter
//@PropertySource(value = "classpath:aws.yml", factory = YamlPropertySourceFactory.class)
public class S3Config {

    //access key 와 secret key는 yml에서 가져옴
    private final String accessKey;

    private final String secretKey;

    private final String region = "ap-northeast-2";

    @Bean
    public AmazonS3Client amazonS3Client() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        return (AmazonS3Client) AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }
}
