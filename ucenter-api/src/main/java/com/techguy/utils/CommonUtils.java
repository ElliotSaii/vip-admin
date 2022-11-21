package com.techguy.utils;

import com.techguy.oss.OssBootUtil;
import org.springframework.web.multipart.MultipartFile;

public class CommonUtils {

    public static String upload(MultipartFile file, String bizPath, String uploadType) {
        String url = "";
            url = OssBootUtil.upload(file,bizPath);

        return url;
    }

    public static String getFileName(String fileName){
        //判断是否带有盘符信息
        // Check for Unix-style path
        int unixSep = fileName.lastIndexOf('/');
        // Check for Windows-style path
        int winSep = fileName.lastIndexOf('\\');
        // Cut off at latest possible point
        int pos = (winSep > unixSep ? winSep : unixSep);
        if (pos != -1)  {
            // Any sort of path separator found...
            fileName = fileName.substring(pos + 1);
        }
        //替换上传文件名字的特殊字符
        fileName = fileName.replace("=","").replace(",","").replace("&","")
                .replace("#", "").replace("“", "").replace("”", "");
        //替换上传文件名字中的空格
        fileName=fileName.replaceAll("\\s","");
        return fileName;
    }
}
