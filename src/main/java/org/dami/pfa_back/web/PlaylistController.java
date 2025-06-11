package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.PlaylistDTO; // Si vous avez besoin de DTO pour le renommage
import org.dami.pfa_back.DTO.PlaylistNameUpdateDTO; // DTO spécifique pour le renommage
import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Security.JwtUtil;
import org.dami.pfa_back.Services.PlaylistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map; // Pour un corps de requête simple pour le renommage

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final JwtUtil jwtUtil;

    public PlaylistController(PlaylistService playlistService, JwtUtil jwtUtil) {
        this.playlistService = playlistService;
        this.jwtUtil = jwtUtil;
    }

    // --- Méthodes existantes ---

    @GetMapping("/me")
    public ResponseEntity<List<Playlist>> getAllMyPlaylists(
            @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7); // Supprime "Bearer "

        // Idéalement, la validation du token et l'extraction de l'ID utilisateur
        // devraient être gérées par un filtre de sécurité Spring Security.
        // Pour l'instant, on le fait manuellement.
        if (!jwtUtil.validateToken(token)) {
            // Retourner un statut 401 Unauthorized serait plus approprié
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            // throw new RuntimeException("Invalid Token"); // Ou une exception gérée globalement
        }

        String ownerId = jwtUtil.extractUserId(token);
        List<Playlist> playlists = playlistService.getPlaylistByOwnerId(ownerId); // Assurez-vous que le nom de la méthode est correct
        return ResponseEntity.ok(playlists);
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(@RequestBody PlaylistDTO playlistDTO,
                                                   @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String userId = jwtUtil.extractUserId(token);

        Playlist playlist = new Playlist();
        playlist.setOwnerId(userId);
        playlist.setCreatedAt(new Date());
        playlist.setName(playlistDTO.getName());
        playlist.setUpdatedAt(new Date()); // La date de mise à jour est la même que la création
        playlist.setDescription(playlistDTO.getDescription());
        // songIds est initialisé à une liste vide par défaut dans l'entité Playlist ou ici
        // playlist.setSongIds(new ArrayList<>());

        Playlist savedPlaylist = playlistService.save(playlist); // Méthode de service plus explicite

        // Pour une création, le statut 201 Created est plus approprié
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPlaylist);
    }

    @PostMapping("/{playlistId}/songs")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable String playlistId,
                                                      @RequestBody Map<String, String> payload, // Utiliser Map pour un JSON simple
                                                      @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String userId = jwtUtil.extractUserId(token); // Pour vérifier la propriété

        String songId = payload.get("songId");
        if (songId == null || songId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Ou un objet d'erreur
        }

        Playlist updatedPlaylist = playlistService.addSongToPlaylist(playlistId,songId);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<Void> removeSongFromPlaylist(@PathVariable String playlistId,
                                                       @PathVariable String songId,
                                                       @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = jwtUtil.extractUserId(token); // Pour vérifier la propriété

        playlistService.delete(playlistId, songId);
        // 204 No Content est approprié pour une suppression réussie sans corps de réponse
        return ResponseEntity.noContent().build();
    }

    // --- NOUVELLES MÉTHODES ---

    /**
     * Endpoint pour renommer une playlist existante.
     * Attend un corps JSON avec le nouveau nom, par exemple : {"name": "Nouveau Nom De Playlist"}
     */
    @PatchMapping("/{playlistId}") // PATCH est sémantiquement correct pour une mise à jour partielle
    public ResponseEntity<Playlist> renamePlaylist(@PathVariable String playlistId,
                                                   @RequestBody PlaylistNameUpdateDTO nameUpdateDTO, // DTO spécifique
                                                   @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        String userId = jwtUtil.extractUserId(token);

        if (nameUpdateDTO.getName() == null || nameUpdateDTO.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(null); // Ou un message d'erreur
        }

        Playlist updatedPlaylist = playlistService.renamePlaylist(playlistId, nameUpdateDTO.getName(), userId);
        return ResponseEntity.ok(updatedPlaylist);
    }

    /**
     * Endpoint pour supprimer une playlist entière.
     */
    @DeleteMapping("/{playlistId}")
    public ResponseEntity<Void> deletePlaylist(@PathVariable String playlistId,
                                               @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String userId = jwtUtil.extractUserId(token);

        playlistService.deletePlaylistById(playlistId, userId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
