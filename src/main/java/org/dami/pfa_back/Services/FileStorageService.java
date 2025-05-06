package org.dami.pfa_back.Services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(FileStorageService.class);

    private final Path audioStorageLocation;
    private final Path imageStorageLocation;

    public FileStorageService(@Value("${file.upload-dir.audio}") String audioUploadDir,
                              @Value("${file.upload-dir.images}") String imageUploadDir) {
        this.audioStorageLocation = Paths.get(audioUploadDir).toAbsolutePath().normalize();
        this.imageStorageLocation = Paths.get(imageUploadDir).toAbsolutePath().normalize();
        initStorageDirectories();
    }

    private void initStorageDirectories() {
        try {
            Files.createDirectories(audioStorageLocation);
            Files.createDirectories(imageStorageLocation);
            logger.info("Storage directories initialized: audio={}, images={}", audioStorageLocation, imageStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create storage directories. " + ex.getMessage(), ex);
        }
    }

    public String storeFileAndGetExtension(MultipartFile file, boolean isAudio, String targetFilename) throws IOException {
        validateFileAndFilename(file, targetFilename);

        String originalFilename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String fileExtension = extractFileExtension(originalFilename);

        Path targetDirectory = getTargetDirectory(isAudio);
        Path targetLocation = targetDirectory.resolve(targetFilename).normalize();

        validateTargetLocation(targetLocation, targetDirectory);
        Files.createDirectories(targetLocation.getParent());

        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            logger.info("Stored file at: {}", targetLocation);
        } catch (IOException ex) {
            logger.error("Failed to store file {} at {}: {}", targetFilename, targetLocation, ex.getMessage());
            throw new IOException("Could not store file: " + targetFilename, ex);
        }

        return fileExtension;
    }

    public Resource loadFileAsResource(String filenameWithExtension, boolean isAudio) throws IOException {
        Path targetDirectory = getTargetDirectory(isAudio);
        Path filePath = targetDirectory.resolve(filenameWithExtension).normalize();

        validateTargetLocation(filePath, targetDirectory);

        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists() && resource.isReadable()) {
            return resource;
        }

        if (!isAudio) {
            Resource fallback = getFallbackImage(targetDirectory);
            if (fallback != null) return fallback;
        }

        logger.error("File not found or unreadable at: {}", filePath);
        throw new RuntimeException("File not found: " + filenameWithExtension);
    }

    public void deleteFile(String filenameWithExtension, boolean isAudio) {
        if (filenameWithExtension == null || filenameWithExtension.isBlank()) {
            logger.warn("Attempted to delete a file with null or empty name.");
            return;
        }

        Path targetDirectory = getTargetDirectory(isAudio);
        Path filePath = targetDirectory.resolve(filenameWithExtension).normalize();

        if (!filePath.startsWith(targetDirectory)) {
            logger.warn("Security warning: Attempt to delete outside of storage directory: {}", filenameWithExtension);
            return;
        }

        try {
            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                logger.info("Deleted file: {}", filePath);
            } else {
                logger.info("File not found for deletion: {}", filePath);
            }
        } catch (IOException | SecurityException ex) {
            logger.error("Error deleting file {}: {}", filenameWithExtension, ex.getMessage());
        }
    }

    public Path getAudioStorageLocation() {
        return audioStorageLocation;
    }

    public Path getImageStorageLocation() {
        return imageStorageLocation;
    }

    // --- Private helpers ---

    private Path getTargetDirectory(boolean isAudio) {
        return isAudio ? audioStorageLocation : imageStorageLocation;
    }

    private void validateFileAndFilename(MultipartFile file, String filename) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Cannot store an empty or null file.");
        }
        if (filename == null || filename.isBlank()) {
            throw new IllegalArgumentException("Target filename cannot be null or blank.");
        }
    }

    private void validateTargetLocation(Path location, Path baseDirectory) throws IOException {
        if (!location.startsWith(baseDirectory)) {
            throw new IOException("Security risk: Attempt to store or access outside of designated directory.");
        }
    }

    private String extractFileExtension(String filename) throws IOException {
        int lastDot = filename.lastIndexOf('.');
        if (lastDot >= 0) {
            return filename.substring(lastDot);
        } else {
            throw new IOException("Missing file extension in: " + filename);
        }
    }

    private Resource getFallbackImage(Path imageDirectory) {
        try {
            Path fallbackPath = imageDirectory.resolve("music.jpeg").normalize();
            Resource fallback = new UrlResource(fallbackPath.toUri());
            if (fallback.exists()) {
                logger.info("Returning fallback image: {}", fallbackPath);
                return fallback;
            }
        } catch (MalformedURLException e) {
            logger.error("Fallback image URL is malformed: {}", e.getMessage());
        }
        return null;
    }
}
