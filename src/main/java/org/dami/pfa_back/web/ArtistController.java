package org.dami.pfa_back.web;

import lombok.RequiredArgsConstructor; // Plus concis pour l'injection via constructeur final
import org.dami.pfa_back.Documents.Artist;
import org.dami.pfa_back.Repository.ArtistRepo; // Assurez-vous que ce Repo existe
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/artists")

public class ArtistController {

    private final ArtistRepo artistRepository;

    public ArtistController(ArtistRepo artistRepository) {
        this.artistRepository = artistRepository;
    }

    // GET all artists
    @GetMapping
    public ResponseEntity<Iterable<Artist>> getAllArtists() {
        Iterable<Artist> artists = artistRepository.findAll();
        return ResponseEntity.ok(artists);
    }

    // GET artist by ID
    @GetMapping("/{id}")
    public ResponseEntity<Artist> getArtistById(@PathVariable String id) {
        Optional<Artist> artistOptional = artistRepository.findById(id);
        return artistOptional
                .map(ResponseEntity::ok) // Si trouvé, retourne 200 OK avec l'artiste
                .orElseGet(() -> ResponseEntity.notFound().build()); // Sinon, retourne 404 Not Found
    }

    // POST (Create) a new artist
    @PostMapping
    public ResponseEntity<Artist> createArtist(@RequestBody Artist artist) {
        // Optionnel : Assigner un nouvel ID si non fourni ou pour garantir l'unicité côté serveur
        if (artist.getId() == null || artist.getId().isEmpty()) {
            artist.setId(UUID.randomUUID().toString());
        }
        // TODO: Ajouter la validation des champs de l'artiste ici si nécessaire
        try {
            Artist savedArtist = artistRepository.save(artist);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedArtist); // Retourne 201 Created
        } catch (Exception e) {
            // TODO: Gérer les erreurs spécifiques (ex: violation de contrainte)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // PUT (Update) an existing artist
    @PutMapping("/{id}")
    public ResponseEntity<Artist> updateArtist(@PathVariable String id, @RequestBody Artist updatedArtist) {
        Optional<Artist> existingArtistOptional = artistRepository.findById(id);

        if (existingArtistOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retourne 404 si l'artiste n'existe pas
        }

        // S'assure que l'ID de l'objet à mettre à jour correspond à l'ID du chemin
        updatedArtist.setId(id);
        // TODO: Ajouter la validation des champs de updatedArtist

        try {
            Artist savedArtist = artistRepository.save(updatedArtist);
            return ResponseEntity.ok(savedArtist); // Retourne 200 OK avec l'artiste mis à jour
        } catch (Exception e) {
            // TODO: Gérer les erreurs spécifiques
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // DELETE an artist by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable String id) {
        if (!artistRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Retourne 404 si l'artiste n'existe pas
        }
        try {
            artistRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Retourne 204 No Content en cas de succès
        } catch (Exception e) {
            // TODO: Gérer les erreurs spécifiques
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
