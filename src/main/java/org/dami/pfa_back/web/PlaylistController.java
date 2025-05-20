package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.PlaylistDTO;
import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Security.JwtUtil;
import org.dami.pfa_back.Services.PlaylistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {
    private final PlaylistService playlistService;
    private final JwtUtil jwtUtil;

    public PlaylistController(PlaylistService plylistService, JwtUtil jwtUtil) {
        this.playlistService = plylistService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/me")
    public ResponseEntity<List<Playlist>> getAll(
            @RequestHeader("Authorization") String auth){
            String token = auth.substring(7);
        System.out.println("token = " + token);
        if (!jwtUtil.validateToken(token))
              throw new RuntimeException("Invalide Token");

           String ownerid = jwtUtil.extractUserId(token);


            List<Playlist> playlists = playlistService.getPlaylistByOwnerId(ownerid);
            return ResponseEntity.ok(playlists);
    }
    @PostMapping
    public ResponseEntity<Playlist> save(@RequestBody PlaylistDTO playlistDTO
              , @RequestHeader("Authorization") String auth){


        String token = auth.substring(7);
        String userId = jwtUtil.extractUserId(token);
        Playlist playlist=new Playlist();
        playlist.setOwnerId(userId);
        playlist.setCreatedAt(new Date());
        playlist.setName(playlistDTO.getName());
        playlist.setUpdatedAt(new Date());
        playlist.setDescription(playlistDTO.getDescription());
        Playlist saved = playlistService.save(playlist);

        return ResponseEntity.ok(saved);
    }
    @PostMapping("/{playlistId}/songs")
    public ResponseEntity<Playlist> addSongToPlaylist(@PathVariable String playlistId, @RequestBody String songId ){
        songId=songId.substring(11,songId.length()-2);
        Playlist playlist = playlistService.addSongToPlaylist(playlistId, songId);
        return ResponseEntity
                .ok(playlist);
    }


}
