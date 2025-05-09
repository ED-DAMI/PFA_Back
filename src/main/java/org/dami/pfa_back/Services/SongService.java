package org.dami.pfa_back.Services;

import org.dami.pfa_back.DTO.SongDto;
import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Exception.StorageException;
import org.dami.pfa_back.Repository.SongRepo;
// import org.dami.pfa_back.Repository.TagRepository; // Décommentez si SongService gère directement le filtrage par tag
 // Assurez-vous que cette exception existe
import org.elasticsearch.client.ResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class SongService {

    private static final Logger log = LoggerFactory.getLogger(SongService.class);
    private final SongRepo songRepository;
    private final FileStorageService fileStorageService;
    // private final TagRepository tagRepository; // Injectez si vous filtrez par tag ici
    // private final UserService userService; // Pour une gestion d'autorisation plus poussée

    public SongService(SongRepo songRepository,
                       FileStorageService fileStorageService
            /*, TagRepository tagRepository, UserService userService */) {
        this.songRepository = songRepository;
        this.fileStorageService = fileStorageService;
        // this.tagRepository = tagRepository;
        // this.userService = userService;
    }

    private SongDto mapToSongDto(Song song) {
        if (song == null) return null;
        SongDto dto = new SongDto();
        dto.setId(song.getId());
        dto.setTitle(song.getTitle());
        dto.setArtist(song.getArtist());
        dto.setGenre(song.getGenre());
        dto.setViewCount(song.getViewCount());
        dto.setTotalReactionCount(song.getTotalReactionCount());
        dto.setCommentCount(song.getCommentCount());
        // Optionnel: Construire des URLs complètes si le DTO doit les exposer
        // dto.setAudioUrl("/api/songs/" + song.getId() + "/audio");
        // dto.setCoverUrl("/api/songs/" + song.getId() + "/cover");
        return dto;
    }

    public List<SongDto> getAllSongsAsDto(String token) {
        log.debug("Service: getAllSongsAsDto. Token presence: {}", (token != null));
        Iterable<Song> songsIterable = songRepository.findAll();
        List<SongDto> dtoList = StreamSupport.stream(songsIterable.spliterator(), false)
                .map(this::mapToSongDto)
                .collect(Collectors.toList());
        log.info("Service: Fetched {} songs as DTOs.", dtoList.size());
        return dtoList;
    }

    public List<SongDto> getRecommendations(String token) {
        log.debug("Service: getRecommendations. Token presence: {}", (token != null));
        // Actuellement, les recommandations sont toutes les chansons.
        // Pour une logique réelle, vous pourriez appeler une méthode différente ici.
        return getAllSongsAsDto(token);
    }

    public List<SongDto> getSongsByTagId(String tagId, String token) {
        log.debug("Service: getSongsByTagId - TagID: '{}'. Token presence: {}", tagId, (token != null));
        // Assurez-vous que SongRepo a une méthode pour cela, ex: findByGenreIgnoreCase(String genre)
        // Ou si Song a un champ List<String> tagIds: findByTagIdsContaining(String tagId)
        // List<Song> songs = songRepository.findByGenreIgnoreCase(tagId); // Exemple
        // Pour la démo, si genre est le tag:
        List<Song> songs = StreamSupport.stream(songRepository.findAll().spliterator(), false)
                .filter(s -> s.getGenre() != null && s.getGenre().equalsIgnoreCase(tagId))
                .collect(Collectors.toList());

        if (songs.isEmpty()) {
            log.info("Service: No songs found for tag ID: '{}'", tagId);
            return Collections.emptyList();
        }
        log.info("Service: Found {} songs for tag ID: '{}'", songs.size(), tagId);
        return songs.stream().map(this::mapToSongDto).collect(Collectors.toList());
    }

    public Song findById(String id) {
        log.debug("Service: findById - ID: '{}'", id);
        return songRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Service: Song not found with ID: '{}'", id);
                    return new ResourceNotFoundException("Song not found with ID: " + id);
                });
    }

    @Transactional
    public void incrementViewCount(String songId, String token) {
        log.debug("Service: incrementViewCount - SongID: '{}'. Token presence: {}", songId, (token != null));
        Song song = findById(songId); // findById lance ResourceNotFoundException si non trouvé
        long currentViews = song.getViewCount();
        song.setViewCount(currentViews + 1);
        songRepository.save(song);
        log.info("Service: View count for song '{}' incremented to {}", songId, song.getViewCount());
    }

    public String getAudioFilenameById(String id) {
        log.debug("Service: getAudioFilenameById - SongID: '{}'", id);
        Song song = findById(id);
        String audioExtension = song.getAudioFileExtension();
        if (audioExtension == null || audioExtension.isBlank()) {
            log.warn("Service: Audio file reference/extension missing for song: '{}'", id);
            throw new ResourceNotFoundException("Audio file reference/extension missing for song: " + id);
        }
        return id + audioExtension;
    }

    public Resource getAudioResourceById(String id, String token) throws IOException {
        log.debug("Service: getAudioResourceById - SongID: '{}'. Token presence: {}", id, (token != null));
        String filename = getAudioFilenameById(id);
        try {
            Resource resource = fileStorageService.loadFileAsResource(filename, true); // true pour audio
            log.info("Service: Audio resource '{}' loaded for song ID: '{}'", filename, id);
            return resource;
        } catch (StorageException e) {
            log.error("Service: StorageException loading audio resource '{}' for song ID '{}': {}", filename, id, e.getMessage());
            throw new ResourceNotFoundException("Audio file not found in storage: " + filename+ e.getMessage());
        }
    }

    public String getImageFilenameById(String id) {
        log.debug("Service: getImageFilenameById - SongID: '{}'", id);
        Song song = findById(id);
        String coverExtension = song.getCoverImageFileExtension();
        if (coverExtension == null || coverExtension.isBlank()) {
            // Ce n'est pas une erreur si on peut servir une image par défaut.
            // Le contrôleur décidera de lancer une exception ou de servir le défaut.
            log.info("Service: No specific cover image extension for song ID: '{}'", id);
            throw new ResourceNotFoundException("No specific cover image for song: " + id); // Indique qu'il n'y a pas d'image spécifique
        }
        return id + coverExtension;
    }

    public Resource getImageResourceById(String id, String token, boolean forceSpecific) throws IOException {
        log.debug("Service: getImageResourceById - SongID: '{}', ForceSpecific: {}. Token presence: {}", id, forceSpecific, (token != null));
        if (forceSpecific) {
            try {
                String filename = getImageFilenameById(id); // Peut lancer ResourceNotFoundException
                Resource resource = fileStorageService.loadFileAsResource(filename, false); // false pour image
                log.info("Service: Specific cover resource '{}' loaded for song ID: '{}'", filename, id);
                return resource;
            } catch (ResourceNotFoundException e) { // getImageFilenameById a lancé
                log.warn("Service: Specific cover not found (no extension or file) for song '{}' (forceSpecific=true). Message: {}", id, e.getMessage());
                throw e; // Relancer si forceSpecific et non trouvé
            } catch (StorageException e) { // loadFileAsResource a lancé
               // log.error("Service: StorageException for specific cover '{}' for song ID '{}' (forceSpecific=true): {}", e., id, e.getMessage());
                throw new ResourceNotFoundException("Cover file not found in storage: " + e.getMessage()); // Relancer
            }
        } else {
            // Tenter de charger l'image spécifique, sinon la par défaut
            try {
                String filename = getImageFilenameById(id); // Peut lancer ResourceNotFoundException
                Resource resource = fileStorageService.loadFileAsResource(filename, false);
                log.info("Service: Specific cover resource '{}' loaded for song ID: '{}'", filename, id);
                return resource;
            } catch (ResourceNotFoundException | StorageException e) {
                throw new RuntimeException(e) ;
                //  log.info("Service: Could not load specific cover for song '{}' (forceSpecific=false). Loading default. Original error: {}", id, e.getMessage());
               // return getDefaultCoverImage();
            }
        }
    }

//    public Resource getDefaultCoverImage() throws IOException {
//        log.info("Service: Loading default cover image.");
//        try {
//            return fileStorageService.loadDefaultCoverImage();
//        } catch (StorageException e) {
//            log.error("Service: CRITICAL - Failed to load default cover image: {}", e.getMessage(), e);
//            throw new IOException("Could not load default cover image due to storage issue.", e);
//        }
//    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) return "";
        int lastDot = filename.lastIndexOf('.');
        return (lastDot >= 0 && lastDot < filename.length() - 1) ? filename.substring(lastDot) : "";
    }

    @Transactional
    public Song uploadSong(String title, String artistName, String genreName,
                           MultipartFile audioFile, MultipartFile coverFile /*, String uploaderId */) throws IOException {
        log.info("Service: Uploading song - Title: '{}', Artist: '{}'", title, artistName);

        if (audioFile == null || audioFile.isEmpty()) {
            log.warn("Service: Upload failed - Audio file is required.");
            throw new IllegalArgumentException("Audio file is required.");
        }

        String songId = UUID.randomUUID().toString();
        String audioTargetFilename = null;
        String coverTargetFilename = null;
        String audioExtension;
        String coverExtension = null;

        audioExtension = getFileExtension(audioFile.getOriginalFilename());
        if (audioExtension.isEmpty()) {
            log.warn("Service: Upload failed - Audio file '{}' is missing a valid extension.", audioFile.getOriginalFilename());
            throw new IllegalArgumentException("Audio file is missing a valid extension.");
        }
        audioTargetFilename = songId + audioExtension;

        if (coverFile != null && !coverFile.isEmpty()) {
            coverExtension = getFileExtension(coverFile.getOriginalFilename());
            if (coverExtension.isEmpty()) {
                log.warn("Service: Cover file '{}' provided but is missing an extension. It will be ignored.", coverFile.getOriginalFilename());
                coverExtension = null; // Ignorer la cover si pas d'extension
            } else {
                coverTargetFilename = songId + coverExtension;
            }
        }

        try {
          //  fileStorageService.storeFile(audioFile, true, audioTargetFilename);
            log.debug("Service: Audio file '{}' stored for song ID '{}'", audioTargetFilename, songId);

            if (coverTargetFilename != null && coverExtension != null) {
            //    fileStorageService.storeFile(coverFile, false, coverTargetFilename);
                log.debug("Service: Cover file '{}' stored for song ID '{}'", coverTargetFilename, songId);
            }

            Song song = new Song();
            song.setId(songId);
            song.setTitle(title);
            song.setArtist(artistName);
            song.setGenre(genreName);
            song.setAudioFileExtension(audioExtension);
            if (coverExtension != null) {
                song.setCoverImageFileExtension(coverExtension);
            }
            song.setViewCount(0L); // Initialiser le compteur de vues
            // if (uploaderId != null) song.setUploaderId(uploaderId);

            Song savedSong = songRepository.save(song);
            log.info("Service: Song uploaded successfully - ID: '{}', Title: '{}'", savedSong.getId(), savedSong.getTitle());
            return savedSong;

        } catch (StorageException e) {
            log.error("Service: Storage error during song upload for title '{}'. Attempting to roll back files. Error: {}", title, e.getMessage(), e);
            if (audioTargetFilename != null) fileStorageService.deleteFileQuietly(audioTargetFilename, true);
            if (coverTargetFilename != null) fileStorageService.deleteFileQuietly(coverTargetFilename, false);
            throw new IOException("Failed to store files for the song: " + e.getMessage(), e);
        } catch (Exception e) { // Autres exceptions (ex: base de données pendant save)
            log.error("Service: Unexpected error during song upload for title '{}'. Attempting to roll back files. Error: {}", title, e.getMessage(), e);
            if (audioTargetFilename != null) fileStorageService.deleteFileQuietly(audioTargetFilename, true);
            if (coverTargetFilename != null) fileStorageService.deleteFileQuietly(coverTargetFilename, false);
            // Si c'est une exception spécifique connue, la relancer telle quelle
            if (e instanceof IllegalArgumentException) throw e;
            throw new RuntimeException("An unexpected error occurred during song upload: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deleteSong(String id, String token) {
        log.info("Service: Attempting to delete song - ID: '{}'. Token presence: {}", id, (token != null));
        Song song = findById(id); // Lance ResourceNotFoundException si non trouvé

        // --- DÉBUT SECTION AUTORISATION (EXEMPLE) ---
        // Vous auriez besoin d'un UserService et/ou de l'intégration Spring Security
        // String currentUserId = userService.getUserIdFromToken(token);
        // if (currentUserId == null) {
        //     log.warn("Service: Delete denied for song '{}'. Invalid or missing token.", id);
        //     throw new SecurityException("Invalid token. Cannot verify user authorization.");
        // }
        // if (!song.getUploaderId().equals(currentUserId) && !userService.isAdmin(currentUserId)) {
        //     log.warn("Service: User '{}' not authorized to delete song '{}' (owner: '{}')", currentUserId, id, song.getUploaderId());
        //     throw new SecurityException("User not authorized to delete this song.");
        // }
        // log.debug("Service: User '{}' authorized to delete song '{}'", currentUserId, id);
        // --- FIN SECTION AUTORISATION (EXEMPLE) ---
        // Pour l'instant, on suppose que l'autorisation est gérée ou que le token est suffisant.

        String audioExt = song.getAudioFileExtension();
        String coverExt = song.getCoverImageFileExtension();

        // La suppression des fichiers doit se faire avant la suppression de l'entité
        // pour que si la suppression de fichier échoue, la transaction DB soit rollbackée.
        try {
            if (audioExt != null && !audioExt.isEmpty()) {
                fileStorageService.deleteFile(id + audioExt, true);
                log.debug("Service: Deleted audio file '{}{}' for song '{}'", id, audioExt, id);
            }
            if (coverExt != null && !coverExt.isEmpty()) {
                fileStorageService.deleteFile(id + coverExt, false);
                log.debug("Service: Deleted cover file '{}{}' for song '{}'", id, coverExt, id);
            }
        } catch (StorageException e) {
            log.error("Service: Failed to delete associated files for song ID '{}' during song deletion. Database transaction will be rolled back. Error: {}", id, e.getMessage(), e);
            throw new RuntimeException("Failed to delete song files, operation aborted.", e); // Provoque le rollback
        }

        songRepository.deleteById(id);
        log.info("Service: Song deleted successfully - ID: '{}'", id);
    }

    public Resource getDefaultCoverImage() throws IOException {
        return fileStorageService.loadFileAsResource("music.jpeg",false);
    }

    public void IncrementerView(String songId) {
        Optional<Song> Optsong = songRepository.findById(songId);
        if (Optsong.isPresent()) {
            Song song = Optsong.get();
            song.IncrementView();
            songRepository.save(song);
        }
    }

    public void IncmenterTotaleReaction(String sonngId) {
        Song song = songRepository.findById(sonngId).get();
        song.setTotalReactionCount(song.getTotalReactionCount()+1);
        songRepository.save(song);
    }

    public void IncrementCommentaire(String songId) {
        Song song = songRepository.findById(songId).get();
        song.setCommentCount(song.getCommentCount()+1);
    }
}
