package org.dami.pfa_back.web;

// --- Necessary Imports ---
import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.Documents.Song;        // Still used for upload response (could change to DTO)
     // DTO for the song list
import org.dami.pfa_back.Services.SongService;
import org.springframework.core.io.Resource;    // For returning file data
import org.springframework.http.HttpHeaders;   // For setting response headers
import org.springframework.http.HttpStatus;     // For HTTP status codes
import org.springframework.http.MediaType;     // For setting Content-Type
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException; // For cleaner error responses
// Or your specific NotFoundException

import javax.persistence.EntityNotFoundException;
import java.net.MalformedURLException;
import java.util.List;
// --- End Imports ---

@RestController
@RequestMapping("/api/songs") // Base path for all song-related endpoints
@CrossOrigin(origins = "*") // Allow requests from any origin (adjust for production!)
public class SongController {

    private final SongService songService;

    // Use constructor injection
    public SongController(SongService songService) {
        this.songService = songService;
    }

    // --- Endpoint 1: Get Song Recommendations (List without URLs) ---
    @GetMapping("/recommendations")
    // TODO: Secure this endpoint via Spring Security - Ensure only authenticated users can access
    public ResponseEntity<List<SongDto>> getRecommendations(
            @RequestHeader(value = "Authorization", required = false) String token) { // Token is now required
        // Note: Token validation should happen *before* this method is called (e.g., Security Filter)
        try {
            List<SongDto> songs = songService.getRecommendations(token);
            return ResponseEntity.ok(songs);
        } catch (Exception e) {
            System.err.println("Error fetching recommendations: " + e.getMessage());
            // Log the exception 'e' properly
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve recommendations", e);
        }
    }

    // --- Endpoint 2: Get Audio Stream by Song ID ---
    @GetMapping("/{id}/audio")
    // TODO: Secure this endpoint via Spring Security - Ensure only authenticated users can access
    // IMPORTANT: If using audioplayers' UrlSource, this endpoint might need different security
    // (e.g., public or signed URLs) as UrlSource doesn't easily send Auth headers.
    public ResponseEntity<Resource> getAudioStreamById(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String token // Required for security check
    ) {
        try {
            System.out.println("\n\nResponseEntity<Resource> getAudioStreamById");
            Resource resource = songService.getAudioResourceById(id, token);
            String filename = songService.getAudioFilenameById(id); // Needed for content type
            String contentType = determineContentType(filename, true);

            return ResponseEntity.ok()
                    .contentType(MediaType.valueOf("audio/mpeg"))
                    .header(HttpHeaders.ACCEPT_RANGES, "bytes") // Indicate support for range requests
                    .body(resource);
        } catch (Exception e) {
            System.err.println("Error streaming audio for id " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not stream audio", e);
        }
    }

    // --- Endpoint 3: Get Cover Image by Song ID ---
    @GetMapping("/{id}/image")
    // TODO: Secure this endpoint via Spring Security - Ensure only authenticated users can access
    public ResponseEntity<Resource> getImageById(
            @PathVariable String id
           // Required for security check
    ) {

        try {
            var token="jdeqfoersg";
            Resource resource = songService.getImageResourceById(id, token);
            String filename = songService.getImageFilenameById(id); // Needed for content type and filename header
            String contentType = determineContentType(filename, false);

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (EntityNotFoundException e) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (MalformedURLException e) {
            System.err.println("URL formation error for image " + id + ": " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error loading image file", e);
        } catch (Exception e) {
            System.err.println("Error getting image for id " + id + ": " + e.getMessage());
            // Log the exception 'e' properly
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not get image", e);
        }
    }

    // --- Endpoint 4: Upload a New Song ---
    @PostMapping("/upload")
    // TODO: Secure this endpoint via Spring Security (Authentication + Authorization/Role Check needed)
    public ResponseEntity<Song> uploadSong(
            @RequestParam("title") String title,
            @RequestParam("artist") String artistName, // Use descriptive names
            @RequestParam("genre") String genreName,
            @RequestParam("audio") MultipartFile audioFile,
            // Add other optional parameters from Song entity as needed
            // @RequestParam(value="album", required=false) String album,
            @RequestParam(value = "cover", required = false) MultipartFile coverFile // Cover is optional
    ) {
        // Basic validation for required fields
        if (title == null || title.isBlank() ||
                artistName == null || artistName.isBlank() ||
                genreName == null || genreName.isBlank() ||
                audioFile == null || audioFile.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title, Artist, Genre, and Audio file are required.");
        }

        try {
            // Call the service method responsible for handling the upload logic
            // This method now internally handles file storage and saving metadata
            Song uploadedSong = songService.uploadSong(title, artistName, genreName, audioFile, coverFile);

            // Return the created Song object (as per your original controller)
            // Alternatively, return SongListDto if preferred:
            // SongListDto dto = songService.mapToSongListDto(uploadedSong);
            // return ResponseEntity.status(HttpStatus.CREATED).body(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedSong);
        } catch (IllegalArgumentException e) { // Catch validation errors from service
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (Exception e) { // Catch file storage errors or DB errors
            System.err.println("Upload failed: " + e.getMessage());
            // Log the exception 'e' properly
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload song.", e);
        }
    }

    // --- Endpoint 5: Delete a Song ---
    @DeleteMapping("/{id}")
    // TODO: Secure this endpoint via Spring Security (Authentication + Authorization/Role Check needed)
    public ResponseEntity<Void> deleteSong(
            @PathVariable String id
            // @RequestHeader("Authorization") String token // Include if auth check needed here
    ) {
        // TODO: Implement validation/authorization using the token if needed here
        System.out.println("Request to delete song: " + id);
        try {
            songService.deleteSong(id);
            return ResponseEntity.noContent().build(); // Standard success response for DELETE
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (Exception e) { // Catch potential errors during file deletion etc.
            System.err.println("Delete failed for id " + id + ": " + e.getMessage());
            // Log the exception 'e' properly
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not delete song.", e);
        }
    }

    // --- Helper Method to Determine Content Type ---
    // (Could be moved to a separate utility class)
    private String determineContentType(String filename, boolean isAudio) {
        if (filename == null || filename.isBlank()) {
            return "application/octet-stream"; // Default
        }
        String filenameLower = filename.toLowerCase();
        if (isAudio) {
            if (filenameLower.endsWith(".mp3")) return "audio/mpeg";
            if (filenameLower.endsWith(".m4a")) return "audio/mp4";
            if (filenameLower.endsWith(".ogg")) return "audio/ogg";
            if (filenameLower.endsWith(".wav")) return "audio/wav";
            // Add more audio types as needed
        } else {
            if (filenameLower.endsWith(".jpg") || filenameLower.endsWith(".jpeg")) return "image/jpeg";
            if (filenameLower.endsWith(".png")) return "image/png";
            if (filenameLower.endsWith(".gif")) return "image/gif";
            if (filenameLower.endsWith(".webp")) return "image/webp";
            // Add more image types as needed
        }
        return "application/octet-stream"; // Fallback
    }

    // REMOVED: @GetMapping("/{id}") - Not needed in this specific workflow
    // public ResponseEntity<Song> getSongById(@PathVariable String id) { ... }

}
