package com.techguy.oss;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 云存储 配置
 */
@Configuration
public class OssConfiguration {

    @Value("${vip.oss.endpoint}")
    private String endpoint;
    @Value("${vip.oss.accessKey}")
    private String accessKeyId;
    @Value("${vip.oss.secretKey}")
    private String accessKeySecret;
    @Value("${vip.oss.bucketName}")
    private String bucketName;
    @Value("${vip.oss.staticDomain:}")
    private String staticDomain;


    @Bean
    public void initOssBootConfiguration() {
        OssBootUtil.setEndPoint(endpoint);
        OssBootUtil.setAccessKeyId(accessKeyId);
        OssBootUtil.setAccessKeySecret(accessKeySecret);
        OssBootUtil.setBucketName(bucketName);
        OssBootUtil.setStaticDomain(staticDomain);
    }
}