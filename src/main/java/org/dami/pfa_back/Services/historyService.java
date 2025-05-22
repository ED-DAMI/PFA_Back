package org.dami.pfa_back.Services;

import org.dami.pfa_back.Documents.history;
import org.dami.pfa_back.Repository.historyRepo;
import org.springframework.stereotype.Service;

@Service
public class historyService {
    private final historyRepo historyRepo;

    public historyService(historyRepo historyRepo) {
        this.historyRepo = historyRepo;
    }

    public void save(history history) {
        String songId = history.getSongId();
        String userId = history.getUserId();
        if (historyRepo.existsBySongIdAndUserId(songId,userId).isPresent())
            return;
        
         historyRepo.save(history);
    }
}
