package org.dami.pfa_back.web;

import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Services.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users/me")
public class UserController {


    private PlaylistService playlistService;

    @GetMapping("/playlists")
    public ResponseEntity<List<Playlist>> getMyPlaylists(@RequestBody User user) {
        return ResponseEntity.ok(playlistService.findByUser(user));
    }
}

