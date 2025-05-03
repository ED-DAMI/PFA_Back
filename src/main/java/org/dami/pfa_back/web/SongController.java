package org.dami.pfa_back.web;

import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Services.SongService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


import static java.util.stream.StreamSupport.stream;

@RestController
@RequestMapping("/api/songs")

public class SongController {


    private final SongService songService;

    public SongController(SongService songService) {
        this.songService = songService;
    }

    @GetMapping("/recommendations")
    public ResponseEntity<List<Song>> getAllSongs(@RequestHeader(value = "Authorization",required = false) String token) {
        System.out.println(token);
        return ResponseEntity.ok( stream(songService.findAll().spliterator(),false)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Song> getSongById(@PathVariable String id) {
        return ResponseEntity.ok(songService.findById(id));
    }

    @PostMapping("/upload")
    public ResponseEntity<Song> uploadSong(
            @RequestParam("title") String title,
            @RequestParam("artist") String artist,
            @RequestParam("genre") String genre,
            @RequestParam("audio") MultipartFile audioFile,
            @RequestParam("cover") MultipartFile coverFile
    ) {
        return ResponseEntity.ok(songService.uploadSong(title, artist, genre, audioFile, coverFile));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSong(@PathVariable String id) {
        songService.deleteSong(id);
        return ResponseEntity.noContent().build();
    }
}
