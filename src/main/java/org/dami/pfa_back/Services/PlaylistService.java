package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.Playlist;
import org.dami.pfa_back.Repository.PlaylistRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    private final PlaylistRepo playlistRepo;

    public PlaylistService(PlaylistRepo playlistRepo) {
        this.playlistRepo = playlistRepo;
    }

    public List<Playlist> getPlaylistByOwnerId(String ownerId){
        return playlistRepo.findAllByOwnerId(ownerId);
    }

    public Playlist save(Playlist playlist) {
        return playlistRepo.save(playlist);
    }

    public Playlist addSongToPlaylist(String playlistId, String songId) {
        System.out.println("songId = " + songId);
        Optional<Playlist> optionalPlaylist = playlistRepo.findById(playlistId);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            playlist.getSongIds().add(songId);
            playlistRepo.save(playlist);
        }
        return null;
    }

    public void delete(String playlistId, String songId) {
        Playlist playlist = playlistRepo.findById(playlistId).get();
        playlist.getSongIds().remove(songId);
        playlistRepo.save(playlist);

    }

    public void deletePlaylistById(String playlistId, String userId) {
        playlistRepo.deleteById(playlistId);
    }

    public Playlist renamePlaylist(String playlistId, String name, String userId) {
        Optional<Playlist> byId = playlistRepo.findById(playlistId);
        if (byId.isPresent()) {
            Playlist playlist = byId.get();
            playlist.setName(name);
            playlistRepo.save(playlist);
            return playlist;
        }
        return null;
    }
}
