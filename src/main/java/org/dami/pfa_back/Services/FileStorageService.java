package org.dami.pfa_back.Services;

 // Adjust package name as needed

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageService {

    private final Path audioStorageLocation;
    private final Path imageStorageLocation;

    // Constructor to initialize storage paths from application properties
    // and create directories if they don't exist.
    public FileStorageService(@Value("${file.upload-dir.audio}") String audioUploadDir,
                              @Value("${file.upload-dir.images}") String imageUploadDir) {
        // Resolve paths absolutely and normalize them for consistency and security
        this.audioStorageLocation = Paths.get(audioUploadDir).toAbsolutePath().normalize();
        this.imageStorageLocation = Paths.get(imageUploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.audioStorageLocation);
            Files.createDirectories(this.imageStorageLocation);
            System.out.println("Storage directories initialized/verified:");
            System.out.println("Audio: " + this.audioStorageLocation);
            System.out.println("Images: " + this.imageStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("FATAL: Could not create storage directories. Check permissions and paths. " + ex.getMessage(), ex);
        }
    }

    /**
     * Stores the uploaded file with the specified target filename and returns the file extension.
     *
     * @param file           The MultipartFile uploaded by the user.
     * @param isAudio        True if the file is audio, false if it's an image (determines storage subdir).
     * @param targetFilename The final filename to use (e.g., "songId.mp3").
     * @return The extracted file extension (e.g., ".mp3", ".jpg").
     * @throws IOException If the file is empty, has no extension, or an I/O error occurs during saving.
     */
    public String storeFileAndGetExtension(MultipartFile file, boolean isAudio, String targetFilename) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("Cannot store an empty or null file.");
        }
        if (targetFilename == null || targetFilename.isBlank()) {
            throw new IllegalArgumentException("Target filename cannot be null or blank.");
        }

        // --- Extract extension for validation and return ---
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = "";
        int lastDot = originalFilename.lastIndexOf('.');
        if (lastDot >= 0) {
            fileExtension = originalFilename.substring(lastDot); // Includes the dot (e.g., ".mp3")
        } else {
            // Extension is crucial for reconstructing the filename later
            throw new IOException("File is missing a valid extension: " + originalFilename);
        }
        // --- Optional: Add validation here to check if fileExtension is allowed ---
        // e.g., Set<String> allowedAudioExt = Set.of(".mp3", ".m4a", ".wav");
        // if (isAudio && !allowedAudioExt.contains(fileExtension.toLowerCase())) { ... throw exception ... }

        // --- Determine target directory and path ---
        Path targetDirectory = isAudio ? this.audioStorageLocation : this.imageStorageLocation;
        Path targetLocation = targetDirectory.resolve(targetFilename).normalize();

        // --- Security check: Prevent storing outside the intended directory ---
        // Although normalize() helps, an extra check doesn't hurt.
        if (!targetLocation.startsWith(targetDirectory)) {
            throw new IOException("Security risk: Cannot store file outside of the designated storage directory. Target path: " + targetLocation);
        }

        // --- Ensure parent directories exist (should already be done by constructor, but safe) ---
        Files.createDirectories(targetLocation.getParent());

        // --- Copy the file to the target location ---
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Stored file: " + targetLocation);
        } catch (IOException ex) {
            System.err.println("Failed to store file " + targetFilename + " at " + targetLocation + " : " + ex.getMessage());
            throw new IOException("Could not store file " + targetFilename + ". Please try again!", ex);
        }

        return fileExtension; // Return the extracted extension (including the dot)
    }

    /**
     * Loads a file as a Spring Resource based on its full filename (ID + extension).
     *
     * @param filenameWithExtension The complete filename (e.g., "songId.mp3").
     * @param isAudio               True if the file is expected in the audio directory, false for images.
     * @return The Resource representing the file.
     * @throws MalformedURLException   If the file path cannot be converted to a valid URL.
     * @throws RuntimeException        (or custom FileNotFoundException) If the file does not exist.
     */
    public Resource loadFileAsResource(String filenameWithExtension, boolean isAudio) throws MalformedURLException {
        try {
            Path targetDirectory = isAudio ? this.audioStorageLocation : this.imageStorageLocation;
            Path filePath = targetDirectory.resolve(filenameWithExtension).normalize();

            if (!filePath.startsWith(targetDirectory)) {
                throw new SecurityException("Attempt to access file outside designated directory: " + filenameWithExtension);
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                System.err.println("File not found or not readable at path: " + filePath);
                // You could create and throw a specific custom exception here like ResourceNotFoundException
                throw new RuntimeException("File not found: " + filenameWithExtension);
            }
        } catch (MalformedURLException ex) {
            System.err.println("Malformed URL for file " + filenameWithExtension + " : " + ex.getMessage());
            throw new MalformedURLException("Could not create URL for file: " + filenameWithExtension);
        } catch (SecurityException ex) {
            System.err.println("Security exception accessing file: " + filenameWithExtension + " : " + ex.getMessage());
            throw new RuntimeException("Access denied for file: " + filenameWithExtension);
        }
    }

    /**
     * Deletes a file based on its full filename (ID + extension).
     *
     * @param filenameWithExtension The complete filename (e.g., "songId.mp3").
     * @param isAudio               True if the file is in the audio directory, false for images.
     */
    public void deleteFile(String filenameWithExtension, boolean isAudio) {
        if (filenameWithExtension == null || filenameWithExtension.isBlank()) {
            System.err.println("Attempted to delete file with null or empty name.");
            return;
        }

        try {
            Path targetDirectory = isAudio ? this.audioStorageLocation : this.imageStorageLocation;
            Path filePath = targetDirectory.resolve(filenameWithExtension).normalize();

            if (!filePath.startsWith(targetDirectory)) {
                System.err.println("Security risk: Attempt to delete file outside designated directory: " + filenameWithExtension);
                return; // Or throw SecurityException
            }

            boolean deleted = Files.deleteIfExists(filePath);
            if (deleted) {
                System.out.println("Successfully deleted file: " + filePath);
            } else {
                System.out.println("File not found for deletion (or already deleted): " + filePath);
            }
        } catch (IOException ex) {
            // Log the error appropriately
            System.err.println("Could not delete file " + filenameWithExtension + ". Error: " + ex.getMessage());

        } catch (SecurityException ex) {
            System.err.println("Security exception deleting file: " + filenameWithExtension + " : " + ex.getMessage());
        }
    }
    public Path getAudioStorageLocation() {
        return audioStorageLocation;
    }

    public Path getImageStorageLocation() {
        return imageStorageLocation;
    }
}
