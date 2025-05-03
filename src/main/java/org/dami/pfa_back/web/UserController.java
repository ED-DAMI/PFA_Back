package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.PlaylistDTO;
import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Repository.PlaylistRepo;
import org.dami.pfa_back.Services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/users/me")
public class UserController {


    private final PlaylistService playlistService;

    public UserController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/playlists")
    public ResponseEntity<List<PlaylistDTO>> getMyPlaylists(@RequestHeader("Authorization") String token) {
        if (playlistService.findAll() == null)
            throw new RuntimeException(" reposotory.findall is null");
        return ResponseEntity
                .ok (StreamSupport
                        .stream(playlistService
                                .findAll()
                                .spliterator()
                                ,false)
                        .map(PlaylistDTO::mapPlylist).toList());
    }
}

