package org.dami.pfa_back.web;

import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @GetMapping
    public ResponseEntity<List<Playlist>> getAllPlaylists() {
        return ResponseEntity
                .ok(StreamSupport
                        .stream(
                                playlistService.findAll().spliterator()
                                ,false).toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playlist> getPlaylistById(@PathVariable String id) {
        return ResponseEntity.ok(playlistService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Playlist> createPlaylist(
            @RequestBody Playlist request,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(playlistService.createPlaylist(user, request));
    }

    @PostMapping("/{playlistId}/songs")
    public ResponseEntity<?> addSongToPlaylist(
            @PathVariable String playlistId,
            @RequestBody Map<String, String> body
    ) {
        String songId = body.get("songId");
        playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{playlistId}/songs/{songId}")
    public ResponseEntity<?> removeSongFromPlaylist(
            @PathVariable String playlistId,
            @PathVariable String songId
    ) {
        playlistService.removeSongFromPlaylist(playlistId, songId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{playlistId}")
    public ResponseEntity<?> deletePlaylist(@PathVariable String playlistId) {
        playlistService.deletePlaylist(playlistId);
        return ResponseEntity.noContent().build();
    }
}

