package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Documents.Song;
import org.dami.pfa_back.Documents.User;
import org.dami.pfa_back.Repository.PlaylistRepo;
import org.dami.pfa_back.Repository.SongRepo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepo playlistRepository;
    private final SongRepo songRepository;
    public PlaylistService(PlaylistRepo playlistRepository, SongRepo songRepository) {
        this.playlistRepository = playlistRepository;
        this.songRepository = songRepository;
    }
    public Iterable<Playlist> findAll() {
        return playlistRepository.findAll();
    }
    public Playlist findById(String id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Playlist not found"));
    }

    public Playlist createPlaylist(User user, Playlist request) {
        Playlist playlist = new Playlist();
        playlist.setName(request.getName());
        playlist.setUserID(user.getId());
        playlist.setSongs(new ArrayList<>());
        return playlistRepository.save(playlist);
    }
    public void addSongToPlaylist(String playlistId, String songId) {
        Playlist playlist = findById(playlistId);
        Song song = songRepository.findById(songId)
                .orElseThrow(() -> new RuntimeException("Song not found"));

        playlist.getSongs().add(song.getId());
        playlistRepository.save(playlist);
    }

    public void removeSongFromPlaylist(String playlistId, String songId) {
        Playlist playlist = findById(playlistId);
        playlist.getSongs().removeIf(song -> song.equals(songId));
        playlistRepository.save(playlist);
    }

    public void deletePlaylist(String id) {
        playlistRepository.deleteById(id);
    }

    public List<Playlist> findByUser(User user) {
        return playlistRepository.findByuserID(user.getId());
    }
}

