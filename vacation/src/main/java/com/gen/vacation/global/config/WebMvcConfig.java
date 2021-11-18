package com.gen.vacation.global.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by dhwlsdn741@gmail.com
 * User: jinwoo
 * Date: 2020-08-03
 * Time: 오후 1:23
 */
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${file.upload.directory}")
    private String resourcesLocation;
    @Value("${file.uri.path}")
    private String resourcesUriPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcesUriPath + "/**")
                .addResourceLocations("file:///" + resourcesLocation + "/");

        registry.addResourceHandler("/image/**")
                .addResourceLocations("classpath:/template/static/image/")
                .setCachePeriod(300);
    }

}
