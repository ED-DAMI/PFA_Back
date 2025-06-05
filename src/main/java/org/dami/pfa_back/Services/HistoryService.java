package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.History;
import org.dami.pfa_back.Repository.HistoryRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class HistoryService {
    private final HistoryRepo historyRepo;

    public HistoryService(HistoryRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    public History save(History history) {
        String songId = history.getSongId();
        String userId = history.getUserId();
        if (!historyRepo.existsBySongIdAndUserId(songId,userId))
        {
            return historyRepo.save(history);
        }
        return getHistory(history);
    }




    public void UpdateListenDuration(String songId, String userId, int durationListenedSeconds) {
        History history = historyRepo.findBySongIdAndUserId(songId,userId);
        int DureeIntiale = history.getDurationListenedSeconds();
        history.setDurationListenedSeconds(DureeIntiale+durationListenedSeconds);
        history.setDate(new Date());
        historyRepo.save(history);
    }


    private History getHistory(History history) {
        History historySaved = historyRepo.findBySongIdAndUserId(history.getSongId(), history.getUserId());
        historySaved.setDate(history.getDate());
        historyRepo.save(historySaved);
        return historySaved;
    }
}
