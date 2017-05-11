package com.example.service;

import com.aliyun.oss.OSSClient;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.PutObjectRequest;
import com.example.util.DemoUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * This sample demonstrates how to upload/download an object to/from
 * Aliyun OSS using the OSS SDK for Java.
 */

/**
 * Created by asus-Iabx on 2017/4/20.
 */
@Service
public class AliyunService {

    String endpoint = "http://oss-cn-shanghai.aliyuncs.com";
    String accessKeyId = "LTAIGgk5JTYtuAdC";
    String accessKeySecret = "5sKiX1Dj2xzp84JIOWIpROk3HJ27hv";

    String bucketName = "panada";
    String key;


    public String saveimage(MultipartFile file) throws IOException {
        int dotpos = file.getOriginalFilename().lastIndexOf(".");
        if (dotpos < 0)
            return null;
        String fileext = file.getOriginalFilename().substring(dotpos + 1).toLowerCase();
        if (!DemoUtil.isFileAllowed(fileext)) {
            return null;
        }
        String filename = UUID.randomUUID().toString().replaceAll("-", "") + "." + fileext;
        key=filename;

        OSSClient client = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {

            /**
             * Note that there are two ways of uploading an object to your bucket, the one
             * by specifying an input stream as content source, the other by specifying a file.
             */

            /*
             * Upload an object to your bucket from an input stream
             */
            System.out.println("Uploading a new object to OSS from an input stream\n");
            client.putObject(bucketName, key, new ByteArrayInputStream(file.getBytes()));
            return DemoUtil.ALIYUN_DOMAIN_PREFIX+key;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorCode());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            /*
             * Do not forget to shut down the client finally to release all allocated resources.
             */
            client.shutdown();
        }
        return "hello";
    }
}
