package org.dami.pfa_back.web;

import org.dami.pfa_back.Documents.Tag;
import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Repository.SongRepo;
import org.dami.pfa_back.Repository.TagRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Spliterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("api/tags")
public class TagsController {
    private final SongRepo songRepo;
    private final TagRepo tagRepo;
    public TagsController(SongRepo songRepo, TagRepo tagRepo1) {
        this.songRepo = songRepo;
        this.tagRepo = tagRepo1;
    }

    @GetMapping
    ResponseEntity<List<Tag>>gettags(){
        List<Tag> tags = StreamSupport.stream(tagRepo.findAll().spliterator(), false).toList();
        return ResponseEntity.ok(tags);
    }
}
