package pl.aircheck.server.station.update;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class StationUpdateService {

    StationUpdateRepository updateRepository;

    public StationUpdateService(StationUpdateRepository updateRepository) {
        this.updateRepository = updateRepository;
    }

    public Optional<LocalDate> getUpdateDate() {
        return updateRepository.findById(1L).map(StationUpdate::getTime);
    }


    public void updateUpdateDate(LocalDate date) {
        updateRepository.save(new StationUpdate(date));
    }

}
