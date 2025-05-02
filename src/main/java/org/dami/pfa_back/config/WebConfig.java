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

    // Injecter les chemins des répertoires pour les utiliser dans addResourceHandlers
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
        // Servir les fichiers audio
        String resolvedAudioDir = Paths.get(audioUploadDir).toAbsolutePath().normalize().toString();
        registry.addResourceHandler("/files/audio/**")
                .addResourceLocations("file:" + resolvedAudioDir + File.separator); // Utiliser File.separator pour la compatibilité OS

        // Servir les fichiers image
        String resolvedImageDir = Paths.get(imageUploadDir).toAbsolutePath().normalize().toString();
        registry.addResourceHandler("/files/images/**")
                // IMPORTANT: "file:" est nécessaire pour indiquer un chemin système
                .addResourceLocations("file:" + resolvedImageDir + File.separator); // Utiliser File.separator
    }
}
