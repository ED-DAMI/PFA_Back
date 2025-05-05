package org.dami.pfa_back.web;

import lombok.RequiredArgsConstructor;
import org.dami.pfa_back.Documents.Album;
import org.dami.pfa_back.Repository.AlbumRepo; // Assurez-vous que ce Repo existe
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;
import java.util.Date; // Pour potentiellement mettre à jour la date de sortie

@RestController
@RequestMapping("/api/albums")

public class AlbumController {

    private final AlbumRepo albumRepository;

    public AlbumController(AlbumRepo albumRepository) {
        this.albumRepository = albumRepository;
    }
    // Potentiellement ArtistRepo si on veut valider l'existence de l'artiste
    // private final ArtistRepo artistRepository;

    // GET all albums
    @GetMapping
    public ResponseEntity<Iterable<Album>> getAllAlbums() {
        Iterable<Album> albums = albumRepository.findAll();
        return ResponseEntity.ok(albums);
    }

    // GET album by ID
    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable String id) {
        Optional<Album> albumOptional = albumRepository.findById(id);
        return albumOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST (Create) a new album
    @PostMapping
    public ResponseEntity<Album> createAlbum(@RequestBody Album album) {
        // Optionnel : Assigner un nouvel ID
        if (album.getId() == null || album.getId().isEmpty()) {
            album.setId(UUID.randomUUID().toString());
        }
        // Optionnel : Mettre la date de sortie à maintenant si non fournie
        if (album.getReleaseDate() == null) {
            album.setReleaseDate(new Date());
        }


        try {
            Album savedAlbum = albumRepository.save(album);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAlbum);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT (Update) an existing album
    @PutMapping("/{id}")
    public ResponseEntity<Album> updateAlbum(@PathVariable String id, @RequestBody Album updatedAlbum) {
        Optional<Album> existingAlbumOptional = albumRepository.findById(id);

        if (existingAlbumOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // S'assure que l'ID correspond
        updatedAlbum.setId(id);

        // Optionnel : Valider que le nouvel artistId existe (si modifié)
        // TODO: Ajouter la validation des autres champs

        try {
            Album savedAlbum = albumRepository.save(updatedAlbum);
            return ResponseEntity.ok(savedAlbum);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE an album by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable String id) {
        if (!albumRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            albumRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // --- Endpoints Additionnels Possibles ---
    // GET albums by artist ID
    @GetMapping("/byArtist/{artistId}")
    public ResponseEntity<Iterable<Album>> getAlbumsByArtist(@PathVariable String artistId) {
        // Vous devrez ajouter la méthode findByArtistId dans AlbumRepo
        // Iterable<Album> albums = albumRepository.findByArtistId(artistId);
        // return ResponseEntity.ok(albums);
        // Pour l'instant, non implémenté sans la méthode du repo :
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).build();
    }
}
