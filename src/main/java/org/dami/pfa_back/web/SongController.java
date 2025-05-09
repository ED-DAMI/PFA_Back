package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.Documents.Song; // Pour GET by ID et retour d'upload
// Votre exception personnalisée
import org.dami.pfa_back.Services.FileStorageService;
import org.dami.pfa_back.Services.SongService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map; // Utilisé si SongService retourne un Map pour les flux

@RestController
@RequestMapping("/api/songs")
public class SongController {

    private static final Logger log = LoggerFactory.getLogger(SongController.class);
    private final SongService songService;
    private final FileStorageService fileStorageService;

    public SongController(SongService songService, FileStorageService fileStorageService) {
        this.songService = songService;
        this.fileStorageService = fileStorageService;
    }

    // Helper pour extraire le token Bearer
    private String extractToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.toLowerCase().startsWith("bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    // GET /api/songs
    // Corresponds to Dart: fetchSongs({String? authToken})
    // Peut aussi gérer fetchSongsByTag si tagId est fourni.
    @GetMapping
    public ResponseEntity<List<SongDto>> getAllSongs(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader,
            @RequestParam(value = "tagId", required = false) String tagId) {
        String token = extractToken(authorizationHeader);
        log.info("Fetching songs. Tag ID: '{}', Token present: {}", tagId, (token != null));

        List<SongDto> songs;
        if (tagId != null && !tagId.isEmpty()) {
            songs = songService.getSongsByTagId(tagId, token); // Méthode à ajouter/vérifier dans SongService
        } else {
            // Si getRecommendations est bien la méthode pour obtenir toutes les chansons sous forme de DTO
            songs = songService.getAllSongsAsDto(token); // Préférer un nom explicite si différent de recommandations
        }
        return ResponseEntity.ok(songs);
    }

    // GET /api/songs/recommendations
    // Corresponds to Dart: fetchRecommendations(String? token)
    @GetMapping("/recommendations")
    public ResponseEntity<List<SongDto>> getRecommendations(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        log.info("Fetching recommendations. Token present: {}", (token != null));
        List<SongDto> recommendations = songService.getRecommendations(token);
        return ResponseEntity.ok(recommendations);
    }
    @GetMapping("/{songId}/recommendations")
    public ResponseEntity<List<SongDto>> getRecommendationsBySong(
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        log.info("Fetching recommendations. Token present: {}", (token != null));
        List<SongDto> recommendations = songService.getRecommendations(token);
        return ResponseEntity.ok(recommendations);
    }


    // GET /api/songs/{id}
    // Corresponds to Dart: fetchSongById(String songId, {String? authToken})
    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        log.info("Fetching song by ID: {}. Token present: {}", id, (token != null));
        try {
            Song song = songService.findById(id); // Suppose que findById retourne l'entité Song
            return ResponseEntity.ok(song);
        } catch (ResourceNotFoundException e) {
            log.warn("Song not found with ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    // GET /api/songs/{id}/audio
    // Corresponds to Dart implicit call for audio stream
    @GetMapping("/{id}/audio")
    public ResponseEntity<Resource> getSongAudio(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = extractToken(authorizationHeader);
        log.info("Fetching audio for song ID: {}. Token present: {}", id, (token != null));
        try {
            // SongService pourrait retourner un objet ou un Map contenant la ressource et le nom de fichier
            // pour éviter des appels multiples ou la reconstruction du chemin ici.
            // Exemple: Map<String, Object> audioData = songService.getAudioStreamData(id, token);
            // Resource resource = (Resource) audioData.get("resource");
            // String filenameWithExtension = (String) audioData.get("filename");
            songService.IncrementerView(id);
            String filenameWithExtension = songService.getAudioFilenameById(id); // Peut lancer ResourceNotFoundException
            Resource resource = songService.getAudioResourceById(id, token);    // Peut lancer IOException ou ResourceNotFoundException

            String contentType = "application/octet-stream"; // Default
            try {
                Path filePath = fileStorageService.getAudioStorageLocation().resolve(filenameWithExtension).normalize();
                contentType = Files.probeContentType(filePath);
                if (contentType == null) { // Fallback si probeContentType échoue
                    if (filenameWithExtension.endsWith(".mp3")) contentType = "audio/mpeg";
                    else if (filenameWithExtension.endsWith(".ogg")) contentType = "audio/ogg";
                    else if (filenameWithExtension.endsWith(".wav")) contentType = "audio/wav";
                    log.warn("Could not determine content type for audio file '{}', using default '{}'", filenameWithExtension, contentType);
                }
            } catch (IOException e) {
                log.error("Could not determine file type for audio '{}': {}", filenameWithExtension, e.getMessage());
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (ResourceNotFoundException e) {
            log.warn("Audio not found for song ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (IOException e) {
            log.error("IO Error retrieving audio for song ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not retrieve audio file", e);
        }
    }

    // GET /api/songs/{id}/cover
    // Corresponds to Dart implicit call for cover image
    @GetMapping("/{id}/cover")
    public ResponseEntity<Resource> getSongCover(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) throws IOException {
        String token = extractToken(authorizationHeader);
        log.info("Fetching cover for song ID: {}. Token present: {}", id, (token != null));
        // Idéalement, le service gère la logique de fallback et retourne la ressource et son nom.
        // Map<String, Object> coverData = songService.getCoverImageStreamData(id, token);
        // Resource resource = (Resource) coverData.get("resource");
        // String filename = (String) coverData.get("filename"); // Pourrait être le nom réel ou "default-cover.jpg"

        Resource resource;
        String filename; // Nom de fichier à utiliser pour le Content-Disposition et la détection du type MIME

        try {
            filename = songService.getImageFilenameById(id); // Peut lancer ResourceNotFound si pas d'extension
            resource = songService.getImageResourceById(id, token, true); // true = forcer le fichier spécifique
        } catch (ResourceNotFoundException e) {
            // Si la chanson n'a pas d'image de couverture spécifique (pas d'extension stockée)
            log.warn("No specific cover image for song ID: {}. Serving default.", id);
            resource = songService.getDefaultCoverImage(); // Méthode à ajouter dans SongService
            filename = resource.getFilename(); // Devrait être le nom du fichier par défaut (ex: "default-cover.jpg")
        } catch (IOException ioEx) {
            // Si une extension est stockée mais que le fichier est manquant/corrompu
            log.error("IOException for specific cover for song ID: {}. Serving default. Error: {}", id, ioEx.getMessage());
            resource = songService.getDefaultCoverImage();
            filename = resource.getFilename();
        }


        String contentType = "application/octet-stream";
        try {
            Path filePath = fileStorageService.getImageStorageLocation().resolve(filename).normalize();
            contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                String fnLower = filename.toLowerCase();
                if (fnLower.endsWith(".jpg") || fnLower.endsWith(".jpeg")) contentType = "image/jpeg";
                else if (fnLower.endsWith(".png")) contentType = "image/png";
                else if (fnLower.endsWith(".webp")) contentType = "image/webp";
                log.warn("Could not determine content type for image file '{}', using default '{}'", filename, contentType);
            }
        } catch (IOException e) {
            log.error("Could not determine file type for image '{}': {}", filename, e.getMessage());
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filename + "\"")
                .body(resource);
    }


    // POST /api/songs/upload
    // Corresponds to Dart: uploadSong(...)
    // Retourne l'entité Song de la chanson créée.
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Song> uploadSong(
            @RequestParam("title") String title,
            @RequestParam("artist") String artist, // Correspond au nom envoyé par Dart
            @RequestParam("genre") String genre,   // Correspond au nom envoyé par Dart
            @RequestParam("audio") MultipartFile audioFile,
            @RequestParam("cover") MultipartFile coverImageFile,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) { // Upload peut être protégé

        String token = extractToken(authorizationHeader);
        // Idéalement, le token serait utilisé pour identifier l'uploader
        // String uploaderId = userService.getUserIdFromToken(token); // Exemple
        log.info("Uploading song: '{}' by '{}'. Token present: {}", title, artist, (token != null));

        try {
            // Le service pourrait prendre l'ID de l'uploader ou le token pour des raisons de sécurité/audit
            Song uploadedSong = songService.uploadSong(title, artist, genre, audioFile, coverImageFile /*, uploaderId */);
            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedSong);
        } catch (IllegalArgumentException e) {
            log.warn("Bad request during song upload: {}", e.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (IOException e) { // Spécifique pour les erreurs de stockage de fichiers
            log.error("IO error during song upload: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store uploaded file.", e);
        } catch (RuntimeException e) { // Autres erreurs d'exécution
            log.error("Runtime error during song upload: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload song: " + e.getMessage(), e);
        }
    }

    // DELETE /api/songs/{id}
    // Corresponds to Dart: deleteSong(String songId, {required String authToken})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(
            @PathVariable String id,
            @RequestHeader(value = "Authorization") String authorizationHeader) { // Token est requis par l'API Dart
        String token = extractToken(authorizationHeader);
        if (token == null) {
            log.warn("Attempt to delete song {} without valid token format", id);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is missing or invalid format");
        }
        log.info("Deleting song ID: {}. Token present.", id);

        // Ici, vous feriez une vérification d'autorisation plus poussée
        // par exemple, songService.deleteSong(id, token); où le service valide le token et les droits.
        try {
            songService.deleteSong(id, token); // Le service doit gérer l'autorisation
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (ResourceNotFoundException e) {
            log.warn("Song to delete not found, ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (SecurityException e) { // Si le service lance une exception de sécurité
            log.warn("Unauthorized attempt to delete song ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, e.getMessage(), e);
        } catch (RuntimeException e) {
            log.error("Error deleting song ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete song", e);
        }
    }

    // POST /api/songs/{id}/view
    // Corresponds to Dart: incrementSongView(String songId, {String? authToken})
    @PostMapping("/{id}/view")
    public ResponseEntity<Void> incrementView(
            @PathVariable String id,
            @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String token = extractToken(authorizationHeader); // Le token peut être optionnel pour cette action
        log.info("Incrementing view for song ID: {}. Token present: {}", id, (token != null));
        try {
            songService.incrementViewCount(id, token); // Le service gère la logique, potentiellement avec le token
            return ResponseEntity.ok().build(); // Ou ResponseEntity.noContent().build()
        } catch (ResourceNotFoundException e) {
            log.warn("Song not found for view increment, ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (RuntimeException e) {
            log.error("Error incrementing view for song ID: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to increment view count", e);
        }
    }
}
