// org.dami.pfa_back.web.UserController.java
package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.DTO.UserDto; // Assurez-vous que ce DTO existe
import org.dami.pfa_back.Documents.History;
import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Repository.HistoryRepo;
import org.dami.pfa_back.Security.JwtUtil;
import org.dami.pfa_back.Services.SongService;
import org.dami.pfa_back.Services.UserService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static org.springframework.data.elasticsearch.annotations.FieldType.Date;

@RequestMapping("/api/users")
@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final HistoryRepo historyRepo;
    private final SongService songService;
    private final UserService userService;
    private final JwtUtil jwtUtil;


    public UserController(HistoryRepo historyRepo,
                          SongService songService,
                          UserService userService,
                          JwtUtil jwtUtil) {
        this.historyRepo = historyRepo;
        this.songService = songService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/history")
    public ResponseEntity<List<SongDto>> getHisory(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);
        List<SongDto> songs = new ArrayList<>(historyRepo.findByUserId(userId)
                .stream()
                .map(History::getSongId)
                .map(songService::findById) // S'assurer que findById ne retourne pas null mais Optional ou lance une exception
                .map(songService::mapToSongDto)
                .toList());
                songs.forEach(s->{
                    History history = historyRepo.findBySongIdAndUserId(s.getId(), userId);
                    s.setListenedAt(history.getDate());
                });
        songs.sort(Comparator.comparing(SongDto::getListenedAt));
        songs=songs.reversed();

        return ResponseEntity.ok(songs);
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> getUserProfile(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);

        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        return ResponseEntity.ok(mapToUserDto(user));
    }

    @PutMapping(value = "/profile/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserDto> updateUserProfile(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "username", required = false) String newUsername,
            @RequestParam(value = "avatar", required = false) MultipartFile avatarFile) {
        String token = authHeader.substring(7);
        String userId = jwtUtil.extractUserId(token);
        try {
            User updatedUser = userService.updateUserProfile(userId, newUsername, avatarFile);
            return ResponseEntity.ok(mapToUserDto(updatedUser));
        } catch (IOException e) {
            logger.error("IO Error updating profile for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Ou un DTO d'erreur plus descriptif
        } catch (IllegalArgumentException e) {
            logger.warn("Invalid argument updating profile for user {}: {}", userId, e.getMessage());
            return ResponseEntity.badRequest().body(null); // Ou un DTO d'erreur plus descriptif
        } catch (Exception e) { // Catch générique pour d'autres erreurs inattendues
            logger.error("Unexpected error updating profile for user {}: {}", userId, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{userId}/avatar")
    public ResponseEntity<Resource> getUserAvatar(@PathVariable String userId) {
        try {
            Resource resource = userService.loadUserAvatar(userId);
            String contentType = determineContentType(resource.getFilename());
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IOException e) {
            // Ceci peut être une RuntimeException de FileStorageService si le fichier n'est pas trouvé
            logger.warn("Avatar not found or error loading avatar for user {}: {}", userId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) { // Par exemple, si loadUserAvatar lance une RuntimeException
            logger.warn("Error loading avatar for user {}: {}", userId, e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("User does not have an avatar set")) {
                return ResponseEntity.notFound().build(); // Cas spécifique où l'avatar n'est pas défini
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // MODIFICATION: mapToUserDto pour renvoyer le chemin relatif correct
    private UserDto mapToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        if (user.getAvatarFilename() != null && !user.getAvatarFilename().isBlank()) {
            dto.setAvatarUrl("api/users/" + user.getId() + "/avatar");
        }
        return dto;
    }

     private String determineContentType(String filename) {
        if (filename == null) return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        String lowerFilename = filename.toLowerCase();
        if (lowerFilename.endsWith(".png")) return MediaType.IMAGE_PNG_VALUE;
        if (lowerFilename.endsWith(".jpg") || lowerFilename.endsWith(".jpeg")) return MediaType.IMAGE_JPEG_VALUE;
        if (lowerFilename.endsWith(".gif")) return MediaType.IMAGE_GIF_VALUE;
        // Ajouter d'autres types si nécessaire
        return MediaType.APPLICATION_OCTET_STREAM_VALUE;
    }

}
