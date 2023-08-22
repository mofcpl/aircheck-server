package pl.aircheck.server.data.update;

import org.springframework.stereotype.Service;
import pl.aircheck.server.sensor.update.SensorUpdate;
import pl.aircheck.server.sensor.update.SensorUpdateRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class DataUpdateService {

    private final DataUpdateRespository dataUpdateRepository;

    public DataUpdateService(DataUpdateRespository dataUpdateRepository) {
        this.dataUpdateRepository = dataUpdateRepository;
    }

    public Optional<LocalDateTime> getUpdateDate(long id) {
        return dataUpdateRepository.findById(id).map(DataUpdate::getDate);
    }

    public void deleteUpdateDate(DataUpdate dataUpdate) {
        dataUpdateRepository.delete()
    }

    public void saveUpdateDate(DataUpdate update) {
        dataUpdateRepository.save(update);
    }
}
