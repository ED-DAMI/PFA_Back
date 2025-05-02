package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.Artist;
import org.dami.pfa_back.Documents.Genre;
import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Repository.ArtistRepo;
import org.dami.pfa_back.Repository.GenreRepo;
import org.dami.pfa_back.Repository.SongRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
public class SongService {

    @Autowired
    private SongRepo songRepository;

    @Autowired
    private GenreRepo genreRepository;

    @Autowired
    private ArtistRepo artistRepository;

    public Iterable<Song> findAll() {
        return songRepository.findAll();
    }

    public Song findById(String id) {
        return songRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Song not found"));
    }

    public Song uploadSong(String title, String artistName, String genreName, MultipartFile audio, MultipartFile cover) {
        Genre genre = genreRepository.findByName(genreName)
                .orElseGet(() -> genreRepository.save(new Genre(genreName)));

        Artist artist = artistRepository.findByName(artistName)
                .orElseGet(() -> artistRepository.save(new Artist(artistName)));

        // Assume file storage handled separately — mock filenames for now
        String audioUrl = "/uploads/audio/" + UUID.randomUUID() + "_" + audio.getOriginalFilename();
        String coverUrl = "/uploads/images/" + UUID.randomUUID() + "_" + cover.getOriginalFilename();

        Song song = new Song();
        song.setTitle(title);
        song.setUrlAudio(audioUrl);
        song.setCoverImage(coverUrl);
        song.setGenre(genre.getId());
        song.setArtist(artist.getId());
        return songRepository.save(song);
    }

    public void deleteSong(String id) {
        songRepository.deleteById(id);
    }
}

