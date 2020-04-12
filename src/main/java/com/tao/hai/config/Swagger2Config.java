package com.tao.hai.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
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
                //为当前包路径
                        apis(RequestHandlerSelectors.any()).
                        paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title)
                //创建人
                .contact(new Contact(name, url, email)).
                        description(description).
                        termsOfServiceUrl(serviceUrl).
                        version(version).build();
    }
}
