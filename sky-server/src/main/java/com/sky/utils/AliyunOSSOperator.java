package com.sky.utils;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.CredentialsProviderFactory;
import com.aliyun.oss.common.auth.EnvironmentVariableCredentialsProvider;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;
@Component
public class AliyunOSSOperator {
    //声明临时文件地址
    private String uploadDir="D:/upload/";
    // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
    private String endpoint="https://oss-cn-beijing.aliyuncs.com";
    // 填写Bucket所在地域
    private String bucketName="elaibulai";
    //    @Value("${aliyun.oss.region}")
    private String region="cn-beijing";
    @SneakyThrows
    public String UploadFile(MultipartFile file) throws com.aliyuncs.exceptions.ClientException {
        String originalFilename = file.getOriginalFilename(); // 原始文件名（如 test.jpg）
        String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")); // 文件后缀（.jpg）
        String newFileName = UUID.randomUUID().toString().replace("-", "") + suffix; // 唯一文件名（如 550e8400e29b41d4a716446655440000.jpg）

        // 3. 构建文件完整存储路径（服务器端路径）
        File destFile = new File(uploadDir + newFileName);

        // 4. 确保存储目录存在（不存在则自动创建）
        if (!destFile.getParentFile().exists()) {
            destFile.getParentFile().mkdirs();
        }

        // 5. 保存文件到服务器（核心操作：将内存/临时文件转移到目标路径）
        try {
            file.transferTo(destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 6. 获取【服务器端完整存储路径】（业务可用，如返回给前端预览）
        String serverFilePath = destFile.getAbsolutePath();

        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        EnvironmentVariableCredentialsProvider credentialsProvider = null;
        credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();

        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = newFileName;
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        String filePath= serverFilePath.replace("\\", "\\\\");
        // 填写Bucket所在地域。以华东1（杭州）为例，Region填写为cn-hangzhou。

        // 创建OSSClient实例。
        // 当OSSClient实例不再使用时，调用shutdown方法以释放资源。
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        OSS ossClient = OSSClientBuilder.create()
                .endpoint(endpoint)
                .credentialsProvider(credentialsProvider)
                .clientConfiguration(clientBuilderConfiguration)
                .region(region)
                .build();

        try {
            InputStream inputStream = null;
            inputStream = new FileInputStream(filePath);
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        if(destFile.exists()){
            destFile.delete();
        }
        return endpoint.split("//")[0]+"//"+bucketName+"."+endpoint.split("//")[1]+"/"+objectName;
    }

}
