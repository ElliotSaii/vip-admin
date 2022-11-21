package com.techguy.oss;
import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.ClientConfiguration;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectResult;
import com.techguy.utils.CommonUtils;
import com.techguy.utils.FileTypeFilter;
import com.techguy.utils.StrAttackFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.UUID;


@Slf4j
public class OssBootUtil {

    private static String endPoint;
    private static String accessKeyId;
    private static String accessKeySecret;
    private static String bucketName;
    private static String staticDomain;

    public static void setEndPoint(String endPoint) {
        OssBootUtil.endPoint = endPoint;
    }

    public static void setAccessKeyId(String accessKeyId) {
        OssBootUtil.accessKeyId = accessKeyId;
    }

    public static void setAccessKeySecret(String accessKeySecret) {
        OssBootUtil.accessKeySecret = accessKeySecret;
    }

    public static void setBucketName(String bucketName) {
        OssBootUtil.bucketName = bucketName;
    }

    public static void setStaticDomain(String staticDomain) {
        OssBootUtil.staticDomain = staticDomain;
    }

    public static String getStaticDomain() {
        return staticDomain;
    }

    public static String getEndPoint() {
        return endPoint;
    }

    public static String getAccessKeyId() {
        return accessKeyId;
    }

    public static String getAccessKeySecret() {
        return accessKeySecret;
    }

    public static String getBucketName() {
        return bucketName;
    }

    public static OSSClient getOssClient() {
        return ossClient;
    }




    private static OSSClient ossClient = null;


    public static String upload(MultipartFile file, String fileDir,String customBucket) {
        String FILE_URL = null;
        initOSS(endPoint, accessKeyId, accessKeySecret);
        StringBuilder fileUrl = new StringBuilder();
        String newBucket = bucketName;
        if(ObjectUtil.isNotEmpty(customBucket)){
            newBucket = customBucket;
        }
        try {
            //判断桶是否存在,不存在则创建桶
            if(!ossClient.doesBucketExist(newBucket)){
                ossClient.createBucket(newBucket);
            }
            // 获取文件名
            String orgName = file.getOriginalFilename();
            if("" == orgName){
                orgName=file.getName();
            }
            //update-begin-author:liusq date:20210809 for: 过滤上传文件类型
            FileTypeFilter.fileTypeFilter(file);
            //update-end-author:liusq date:20210809 for: 过滤上传文件类型
            orgName = CommonUtils.getFileName(orgName);
            String fileName = orgName.indexOf(".")==-1
                    ?orgName + "_" + System.currentTimeMillis()
                    :orgName.substring(0, orgName.lastIndexOf(".")) + "_" + System.currentTimeMillis() + orgName.substring(orgName.lastIndexOf("."));
            if (!fileDir.endsWith("/")) {
                fileDir = fileDir.concat("/");
            }
            //update-begin-author:wangshuai date:20201012 for: 过滤上传文件夹名特殊字符，防止攻击
            fileDir= StrAttackFilter.filter(fileDir);
            //update-end-author:wangshuai date:20201012 for: 过滤上传文件夹名特殊字符，防止攻击
            fileUrl = fileUrl.append(fileDir + fileName);

            if (ObjectUtil.isNotEmpty(staticDomain) && staticDomain.toLowerCase().startsWith("http")) {
                FILE_URL = staticDomain + "/" + fileUrl;
            } else {
                FILE_URL = "https://" + newBucket + "." + endPoint + "/" + fileUrl;
            }
            PutObjectResult result = ossClient.putObject(newBucket, fileUrl.toString(), file.getInputStream());
            // 设置权限(公开读)
//            ossClient.setBucketAcl(newBucket, CannedAccessControlList.PublicRead);
            if (result != null) {
                log.info("------OSS文件上传成功------" + fileUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return FILE_URL;
    }



    public static String getOriginalUrl(String url) {
        String originalDomain = "https://" + bucketName + "." + endPoint;
        if(url.indexOf(staticDomain)!=-1){
            url = url.replace(staticDomain,originalDomain);
        }
        return url;
    }




    public static String upload(MultipartFile file, String fileDir) {
        return upload(file, fileDir,null);
    }



    public static String upload(FileItemStream file, String fileDir) {
        String FILE_URL = null;
        initOSS(endPoint, accessKeyId, accessKeySecret);
        StringBuilder fileUrl = new StringBuilder();
        try {
            String suffix = file.getName().substring(file.getName().lastIndexOf('.'));
            String fileName = UUID.randomUUID().toString().replace("-", "") + suffix;
            if (!fileDir.endsWith("/")) {
                fileDir = fileDir.concat("/");
            }
            fileDir = StrAttackFilter.filter(fileDir);
            fileUrl = fileUrl.append(fileDir + fileName);
            if (ObjectUtil.isNotEmpty(staticDomain) && staticDomain.toLowerCase().startsWith("http")) {
                FILE_URL = staticDomain + "/" + fileUrl;
            } else {
                FILE_URL = "https://" + bucketName + "." + endPoint + "/" + fileUrl;
            }
            PutObjectResult result = ossClient.putObject(bucketName, fileUrl.toString(), file.openStream());
            // 设置权限(公开读)
            ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
            if (result != null) {
                log.info("------OSS文件上传成功------" + fileUrl);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return FILE_URL;
    }



    public static void deleteUrl(String url) {
        deleteUrl(url,null);
    }



    public static void deleteUrl(String url,String bucket) {
        String newBucket = bucketName;
        if(ObjectUtil.isNotEmpty(bucket)){
            newBucket = bucket;
        }
        String bucketUrl = "";
        if (ObjectUtil.isNotEmpty(staticDomain) && staticDomain.toLowerCase().startsWith("http")) {
            bucketUrl = staticDomain + "/" ;
        } else {
            bucketUrl = "https://" + newBucket + "." + endPoint + "/";
        }
        url = url.replace(bucketUrl,"");
        ossClient.deleteObject(newBucket, url);
    }



    public static void delete(String fileName) {
        ossClient.deleteObject(bucketName, fileName);
    }



    public static InputStream getOssFile(String objectName,String bucket){
        InputStream inputStream = null;
        try{
            String newBucket = bucketName;
            if(ObjectUtil.isNotEmpty(bucket)){
                newBucket = bucket;
            }
            initOSS(endPoint, accessKeyId, accessKeySecret);
            OSSObject ossObject = ossClient.getObject(newBucket,objectName);
            inputStream = new BufferedInputStream(ossObject.getObjectContent());
        }catch (Exception e){
            log.info("文件获取失败" + e.getMessage());
        }
        return inputStream;
    }



    public static InputStream getOssFile(String objectName){
        return getOssFile(objectName,null);
    }



    public static String getObjectURL(String bucketName, String objectName, Date expires) {
        initOSS(endPoint, accessKeyId, accessKeySecret);
        try{
            if(ossClient.doesObjectExist(bucketName,objectName)){
                URL url = ossClient.generatePresignedUrl(bucketName,objectName,expires);
                return URLDecoder.decode(url.toString(),"UTF-8");
            }
        }catch (Exception e){
            log.info("文件路径获取失败" + e.getMessage());
        }
        return null;
    }



    private static OSSClient initOSS(String endpoint, String accessKeyId, String accessKeySecret) {
        if (ossClient == null) {
            ossClient = new OSSClient(endpoint,
                    new DefaultCredentialProvider(accessKeyId, accessKeySecret),
                    new ClientConfiguration());
        }
        return ossClient;
    }





    public static String upload(InputStream stream, String relativePath) {
        String FILE_URL = null;
        String fileUrl = relativePath;
        initOSS(endPoint, accessKeyId, accessKeySecret);
        if (ObjectUtil.isNotEmpty(staticDomain) && staticDomain.toLowerCase().startsWith("http")) {
            FILE_URL = staticDomain + "/" + relativePath;
        } else {
            FILE_URL = "https://" + bucketName + "." + endPoint + "/" + fileUrl;
        }
        PutObjectResult result = ossClient.putObject(bucketName, fileUrl,stream);
        // 设置权限(公开读)
        ossClient.setBucketAcl(bucketName, CannedAccessControlList.PublicRead);
        if (result != null) {
            log.info("------OSS文件上传成功------" + fileUrl);
        }
        return FILE_URL;
    }


}
