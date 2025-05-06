package org.dami.pfa_back.Services;

import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.Documents.Artist;
import org.dami.pfa_back.Documents.Genre;
import org.dami.pfa_back.Documents.Song; // Version modifiée (avec extensions)

import org.dami.pfa_back.Repository.ArtistRepo;
import org.dami.pfa_back.Repository.GenreRepo;
import org.dami.pfa_back.Repository.SongRepo;
import org.springframework.core.io.Resource; // Important pour renvoyer les fichiers
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport; // Utile si findAll retourne Iterable

@Service
public class SongService {

    private final SongRepo songRepository;
    private final GenreRepo genreRepository;
    private final ArtistRepo artistRepository;
    private final FileStorageService fileStorageService; // Service modifié

    public SongService(SongRepo songRepository,
                       GenreRepo genreRepository,
                       ArtistRepo artistRepository,
                       FileStorageService fileStorageService) {
        this.songRepository = songRepository;
        this.genreRepository = genreRepository;
        this.artistRepository = artistRepository;
        this.fileStorageService = fileStorageService;
    }

    // --- Helper pour mapper vers le DTO de liste ---
    private SongDto mapToSongListDto(Song song) {
        if (song == null) return null;
        SongDto dto = new SongDto();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());

        dto.setArtist(song.getArtist()); // Placeholder
        dto.setGenre(song.getGenre());   // Placeholder

        return dto;
    }

    // --- 1. Méthode pour récupérer les "recommandations" (toutes les chansons pour l'instant) ---
    /**
     * Récupère une liste d'informations de base sur les chansons.
     * Le token est reçu mais n'est pas utilisé dans cette implémentation de base
     * pour la personnalisation. La validation du token doit être faite en amont (Controller/Security).
     *
     * @param token Le token d'authentification de l'utilisateur (reçu mais non traité ici).
     * @return Une liste de SongListDto.
     */
    public List<SongDto> getRecommendations(String token) {

        System.out.println("Received token in getRecommendations (validation external): " + token); // Logging simple

        Iterable<Song> songsIterable = songRepository.findAll();
        return StreamSupport.stream(songsIterable.spliterator(), false) // Convertir Iterable en Stream
                .map(this::mapToSongListDto)
                .collect(Collectors.toList());
    }

    // --- 2. Méthode pour retourner la ressource AUDIO d'une chanson par ID ---
    /**
     * Charge et retourne la ressource binaire pour le fichier audio d'une chanson spécifique.
     * La validation du token doit être faite en amont (Controller/Security).
     *
     * @param id L'ID de la chanson.
     * @param token Le token d'authentification (reçu mais non traité ici pour l'autorisation interne).
     * @return La Resource Spring contenant les données du fichier audio.
     * @throws EntityNotFoundException Si la chanson ou la référence au fichier audio est manquante.
     * @throws MalformedURLException Si l'URL de la ressource ne peut être formée.
     */
    public Resource getAudioResourceById(String id, String token) throws IOException {
        System.out.println("Received token in getAudioResourceById for song " + id + " (validation external): " + token);

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        String audioExtension =  song.getAudioFileExtension();
        if (audioExtension == null || audioExtension.isBlank()) {
            throw new EntityNotFoundException("Audio file reference/extension missing for song: " + id);
        }
        String filename =id+song.getAudioFileExtension();

        return fileStorageService.loadFileAsResource(filename, true); // true pour audio
    }

    // --- 3. Méthode pour retourner la ressource IMAGE d'une chanson par ID ---
    /**
     * Charge et retourne la ressource binaire pour le fichier image de couverture d'une chanson spécifique.
     * La validation du token doit être faite en amont (Controller/Security).
     *
     * @param id L'ID de la chanson.
     * @param token Le token d'authentification (reçu mais non traité ici pour l'autorisation interne).
     * @return La Resource Spring contenant les données du fichier image.
     * @throws EntityNotFoundException Si la chanson ou la référence au fichier image est manquante.
     * @throws MalformedURLException Si l'URL de la ressource ne peut être formée.
     */
    public Resource getImageResourceById(String id, String token) throws IOException {

        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        String coverExtension = song.getCoverImageFileExtension();
        if (coverExtension == null || coverExtension.isBlank()) {

            throw new EntityNotFoundException("Cover image file reference/extension missing for song: " + id);
        }
        String filename = id + coverExtension;
        return fileStorageService.loadFileAsResource(filename, false); // false pour image
    }

    // --- Méthodes de support toujours nécessaires pour le contrôleur ---

    // Obtient le nom de fichier audio reconstruit (pour Content-Type)
    public String getAudioFilenameById(String id) {
        Song song = songRepository.findById(id)
               .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        String audioExtension =song.getAudioFileExtension();
        if (audioExtension == null || audioExtension.isBlank()) {
            throw new EntityNotFoundException("Audio file reference/extension missing for song: " + id);
        }
        return id+audioExtension;
    }

    // Obtient le nom de fichier image reconstruit (pour Content-Type)
    public String getImageFilenameById(String id) {
        Song song = songRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Song not found with id: " + id));
        String coverExtension = song.getCoverImageFileExtension();
        if (coverExtension == null || coverExtension.isBlank()) {
            throw new EntityNotFoundException("Cover image file reference/extension missing for song: " + id);
        }
        return id + coverExtension;
    }

    // --- Méthodes CRUD (gardées mais la logique d'upload est adaptée) ---

    // Helper pour extraire l'extension
    private String getFileExtension(String filename) {
        if (filename == null) return "";
        int lastDot = filename.lastIndexOf('.');
        return (lastDot >= 0) ? filename.substring(lastDot) : "";
    }

    @Transactional
    public Song uploadSong(String title, String artistName, String genreName, MultipartFile audioFile, MultipartFile coverFile) {

        if (audioFile == null || audioFile.isEmpty()) throw new IllegalArgumentException("Audio file is required.");
        String songId = UUID.randomUUID().toString();
        Genre genre = genreRepository.findByName(genreName).orElseGet(() -> genreRepository.save(new Genre(genreName)));
        Artist artist = artistRepository.findByName(artistName).orElseGet(() -> artistRepository.save(new Artist(artistName)));
        String audioTargetFilename = null; String coverTargetFilename = null;
        String audioExtension = null; String coverExtension = null;
        try {
            audioExtension = getFileExtension(audioFile.getOriginalFilename());
            if (audioExtension.isEmpty()) throw new IOException("Audio file is missing extension.");
            audioTargetFilename = songId + audioExtension;
            fileStorageService.storeFileAndGetExtension(audioFile, true, audioTargetFilename); // Utilise la version modifiée
            if (coverFile != null && !coverFile.isEmpty()) {
                coverExtension = getFileExtension(coverFile.getOriginalFilename());
                if (coverExtension.isEmpty()) throw new IOException("Cover file is missing extension.");
                coverTargetFilename = songId + coverExtension;
                fileStorageService.storeFileAndGetExtension(coverFile, false, coverTargetFilename); // Utilise la version modifiée
            }
            Song song = new Song();
            song.setId(songId); song.setTitle(title);
            song.setArtist(artist.getId()); song.setGenre(genre.getId());
            song.setAudioFileExtension(audioExtension);
            song.setCoverImageFileExtension(coverExtension);
            return songRepository.save(song);
        } catch (IOException e) {
            if (audioTargetFilename != null) fileStorageService.deleteFile(audioTargetFilename, true);
            if (coverTargetFilename != null) fileStorageService.deleteFile(coverTargetFilename, false);
            throw new RuntimeException("Failed to store files for the song.", e);
        } catch (Exception e) {
            if (audioTargetFilename != null) fileStorageService.deleteFile(audioTargetFilename, true);
            if (coverTargetFilename != null) fileStorageService.deleteFile(coverTargetFilename, false);
            throw new RuntimeException("Failed to upload song.", e);
        }
    }

    @Transactional
    public void deleteSong(String id) {

        Song song = songRepository.findById(id).orElseThrow(() -> new RuntimeException("Song not found with id: " + id));
        String audioExt = song.getAudioFileExtension(); String coverExt = song.getCoverImageFileExtension();
        if (audioExt != null && !audioExt.isEmpty()) fileStorageService.deleteFile(id + audioExt, true);
        if (coverExt != null && !coverExt.isEmpty()) fileStorageService.deleteFile(id + coverExt, false);
        songRepository.deleteById(id);
    }

    // findById retournant l'entité Song peut être utile en interne
    public Song findById(String id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found with id: " + id));
    }
}
