package com.tao.hai.config;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Slf4j
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Value("swagger2.title")
    private String title;
    @Value("swagger2.description")
    private String description;
    @Value("swagger2.serviceUrl")
    private String serviceUrl;
    @Value("swagger2.version")
    private String version;
    @Value("swagger2.contact.name")
    private String name;
    @Value("swagger2.contact.url")
    private String url;
    @Value("swagger2.contact.email")
    private String email;


    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2).
                apiInfo(apiInfo()).select().
                //此包路径下的类，才生成接口文档
                        apis(RequestHandlerSelectors.any()).
                //加了ApiOperation注解的类，才生成接口文档
                       //apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class)).
                        paths(PathSelectors.any()).
                        build();
                        //.securitySchemes(Collections.singletonList(securityScheme()));
    }

    /***
     * oauth2配置
     * 需要增加swagger授权回调地址
     * http://localhost:8888/webjars/springfox-swagger-ui/o2c.html
     * @return
     */
    @Bean
    public SecurityScheme securityScheme() {
        return new ApiKey("X-Access-Token", "X-Access-Token", "header");
    }

    /**
     * api文档的详细信息函数,注意这里的注解引用的是哪个
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title)//大标题
                .version(version)// 版本号
                .description(description)// 描述
                //创建人
                .contact(new Contact(name, url, email)).
                        description(description).
                        termsOfServiceUrl(serviceUrl).
                        version(version).build();
    }
}
