package pl.aircheck.server.sensor.update;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SensorUpdateService {

    private final SensorUpdateRepository sensorUpdateRepository;

    public SensorUpdateService(SensorUpdateRepository sensorUpdateRepository) {
        this.sensorUpdateRepository = sensorUpdateRepository;
    }

    public Optional<LocalDate> getUpdateDate(long id) {
        return sensorUpdateRepository.findById(id).map(SensorUpdate::getDate);
    }

    public void deleteUpdateDate(SensorUpdate sensorUpdate) {
        sensorUpdateRepository.delete(sensorUpdate);
    }

    public void saveUpdateDate(SensorUpdate update) {
        sensorUpdateRepository.save(update);
    }
}
