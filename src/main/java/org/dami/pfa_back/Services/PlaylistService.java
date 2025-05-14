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
        Optional<Playlist> optionalPlaylist = playlistRepo.findById(playlistId);
        if (optionalPlaylist.isPresent()) {
            Playlist playlist = optionalPlaylist.get();
            playlist.getSongIds().add(songId);
            playlistRepo.save(playlist);
        }
        return null;
    }
}
