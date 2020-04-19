package com.tao.hai.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**file开头的信息*/
@Component
@ConfigurationProperties(prefix="file")
@Data
public class FileConfig {
    /**上传文件路径*/
    private String fileUploadUrl;
}
