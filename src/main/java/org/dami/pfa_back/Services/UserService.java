// org.dami.pfa_back.Services.UserService.java
package org.dami.pfa_back.Services;


import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Repository.UserRepo;

import org.springframework.core.io.Resource;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Ou une exception personnalisée
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepo userRepository;
    private final FileStorageService fileStorageService;

    public UserService(UserRepo userRepository, FileStorageService fileStorageService) {
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }

    @Transactional // Important pour s'assurer que les opérations sont atomiques
    public User updateUserProfile(String userId, String newUsername, MultipartFile avatarFile) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        // Mettre à jour le nom d'utilisateur s'il est fourni et différent
        if (newUsername != null && !newUsername.isBlank() && !user.getUsername().equals(newUsername)) {
            // Vérifier si le nouveau nom d'utilisateur est déjà pris (optionnel mais recommandé)
            if (userRepository.findByUsername(newUsername).isPresent() && !userRepository.findByUsername(newUsername).get().getId().equals(userId)) {
                throw new IllegalArgumentException("Username '" + newUsername + "' is already taken.");
            }
            user.setUsername(newUsername);
            logger.info("Updating username for user {} to {}", userId, newUsername);
        }

        // Gérer l'upload de l'avatar
        if (avatarFile != null && !avatarFile.isEmpty()) {
            // Supprimer l'ancien avatar s'il existe
            if (user.getAvatarFilename() != null && !user.getAvatarFilename().isBlank()) {
                fileStorageService.deleteFile(user.getAvatarFilename(), false); // false car c'est une image
                logger.info("Deleted old avatar {} for user {}", user.getAvatarFilename(), userId);
            }

            // Construire un nom de fichier unique (par exemple, userId + extension)
            String originalFilename = avatarFile.getOriginalFilename();
            String extension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            // Valider l'extension si nécessaire (ex: .jpg, .png)
            if (!extension.matches("\\.(jpeg|jpg|png|gif)$")) {
                throw new IllegalArgumentException("Invalid image file extension. Only JPG, PNG, GIF are allowed.");
            }

            String avatarFilename = userId + "_avatar" + extension; // ex: "someUserId_avatar.jpg"

            // Stocker le nouveau fichier et obtenir son extension (le nom est déjà défini)
            fileStorageService.storeFileAndGetExtension(avatarFile, false, avatarFilename);
            user.setAvatarFilename(avatarFilename);
            logger.info("Stored new avatar {} for user {}", avatarFilename, userId);
        }

        return userRepository.save(user);
    }

    // Méthode pour récupérer la ressource de l'avatar
    public Resource loadUserAvatar(String userId) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));

        if (user.getAvatarFilename() == null || user.getAvatarFilename().isBlank()) {

            throw new RuntimeException("User does not have an avatar set.");
        }
        return fileStorageService.loadFileAsResource(user.getAvatarFilename(), false);
    }
}
