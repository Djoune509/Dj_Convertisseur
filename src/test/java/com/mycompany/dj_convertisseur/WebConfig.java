package com.mycompany.dj_convertisseur;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Jwenn chemen kote fichiye yo ye a
        String reportPath = Paths.get(System.getProperty("user.dir"), "uploads", "files")
                                 .toAbsolutePath().toString();
        
        // Di Spring ke URL /files/** koresponn ak folder sa a
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:" + reportPath + "/");
    }
}