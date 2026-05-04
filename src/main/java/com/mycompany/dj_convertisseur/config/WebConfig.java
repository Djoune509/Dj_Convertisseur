/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.dj_convertisseur.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.io.File;
/**
 *
 * @author StHilaireDjoune
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
  
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // N ap pran chemen folder a sou òdinatè a
        String path = new File("src/main/resources/static/files/").getAbsolutePath();
        
        // Nou di Spring: Si yon moun mande yon bagay nan /files/, 
        // ale chèche l dirèkteman nan folder sa a.
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///" + path + "/");
    }
}

