package org.dami.pfa_back.web;

import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Documents.history;
import org.dami.pfa_back.Repository.historyRepo;
import org.dami.pfa_back.Security.JwtUtil;
import org.dami.pfa_back.Services.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/users")
@RestController
public class UserController {
    private final historyRepo hisoryRepo;
    private final SongService songService;
    private final JwtUtil jwtUtil;

    public UserController(historyRepo hisoryRepo, SongService songService, JwtUtil jwtUtil) {
        this.hisoryRepo = hisoryRepo;
        this.songService = songService;

        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/history")
    public ResponseEntity<List<SongDto>> getHisory(@RequestHeader("Authorization") String auth){
        String userId = jwtUtil.extractUserId(auth.substring(7));
        List<SongDto> songs = hisoryRepo.findByUserId(userId)
                .stream()
                .map(history::getSongId)
                .map(songService::findById)
                .map(songService::mapToSongDto)
                .toList();
        return ResponseEntity
                .ok(songs);
    }
}

