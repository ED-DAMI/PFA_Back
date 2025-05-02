package org.dami.pfa_back.web;

import lombok.RequiredArgsConstructor;
import org.dami.pfa_back.Documents.Genre;
import org.dami.pfa_back.Repository.GenreRepo; // Assurez-vous que ce Repo existe
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/genres")

public class GenreController {

    private final GenreRepo genreRepository;

    public GenreController(GenreRepo genreRepository) {
        this.genreRepository = genreRepository;
    }

    // GET all genres
    @GetMapping
    public ResponseEntity<Iterable<Genre>> getAllGenres() {
        Iterable<Genre> genres = genreRepository.findAll();
        return ResponseEntity.ok(genres);
    }

    // GET genre by ID
    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenreById(@PathVariable String id) {
        Optional<Genre> genreOptional = genreRepository.findById(id);
        return genreOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // POST (Create) a new genre
    @PostMapping
    public ResponseEntity<Genre> createGenre(@RequestBody Genre genre) {
        // Optionnel : Assigner un nouvel ID
        if (genre.getId() == null || genre.getId().isEmpty()) {
            genre.setId(UUID.randomUUID().toString());
        }
        // TODO: Ajouter la validation des champs (ex: nom unique ?)

        try {
            Genre savedGenre = genreRepository.save(genre);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedGenre);
        } catch (Exception e) {
            // TODO: Gérer les erreurs spécifiques (ex: nom dupliqué)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT (Update) an existing genre
    @PutMapping("/{id}")
    public ResponseEntity<Genre> updateGenre(@PathVariable String id, @RequestBody Genre updatedGenre) {
        Optional<Genre> existingGenreOptional = genreRepository.findById(id);

        if (existingGenreOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // S'assure que l'ID correspond
        updatedGenre.setId(id);
        // TODO: Ajouter la validation des champs

        try {
            Genre savedGenre = genreRepository.save(updatedGenre);
            return ResponseEntity.ok(savedGenre);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE a genre by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenre(@PathVariable String id) {
        if (!genreRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        try {
            // Attention : Supprimer un genre peut nécessiter de gérer les chansons associées
            // (mettre leur genreId à null, les supprimer aussi, ou interdire la suppression si utilisé)
            // Cette logique métier doit être implémentée ici ou dans un service.
            genreRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
