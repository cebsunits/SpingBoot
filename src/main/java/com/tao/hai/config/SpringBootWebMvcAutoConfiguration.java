package com.tao.hai.config;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.springframework.web.servlet.resource.WebJarsResourceResolver;

@SpringBootConfiguration

public class SpringBootWebMvcAutoConfiguration extends WebMvcAutoConfiguration {
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/"};


    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("/webjars/")
                    .resourceChain(false)
                    .addResolver(new WebJarsResourceResolver())
                    .addResolver(new PathResourceResolver());
        }
        /**静态资源*/
//        registry.addResourceHandler("/templates/**.js")
//                .addResourceLocations("/templates/");
//        registry.addResourceHandler("/templates/**.css")
//                .addResourceLocations("/templates/");
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**").addResourceLocations(
                    CLASSPATH_RESOURCE_LOCATIONS);
        }
    }
}
