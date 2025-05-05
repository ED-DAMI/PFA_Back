package org.dami.pfa_back.config; // Créez ce package s'il n'existe pas

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${upload.audio.dir:uploads/audio/}")
    private String audioUploadDir;

    @Value("${upload.image.dir:uploads/images/}")
    private String imageUploadDir;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Autoriser toutes les routes
                .allowedOrigins("*") // Autoriser toutes les origines
                .allowedMethods("*") // Autoriser toutes les méthodes HTTP
                .allowedHeaders("*") // Autoriser tous les en-têtes
                .allowCredentials(false) // Désactiver les cookies (sinon `*` n'est pas autorisé)
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/media/**")
                .addResourceLocations("file:uploads/"); // Le dossier "uploads" est à la racine du projet
    }
}
