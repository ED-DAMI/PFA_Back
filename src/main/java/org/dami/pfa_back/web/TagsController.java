package org.dami.pfa_back.web;

import co.elastic.clients.transport.http.TransportHttpClient;
import org.dami.pfa_back.DTO.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@RestController
@RequestMapping("api/tags")
public class TagsController {
    @GetMapping
    ResponseEntity<List<Tag>>gettags(){
        Random r=new Random();
        List<Tag> tags = Stream
                .of("Raja", "Chainable", "Mok")
                .map(e -> new Tag("" + r.nextInt(), e))
                .toList();
        return ResponseEntity
                .ok(tags);
    }
}
