package pl.aircheck.server.sensor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.aircheck.server.Config;
import pl.aircheck.server.NoDataFromOriginException;
import pl.aircheck.server.sensor.update.SensorUpdate;
import pl.aircheck.server.sensor.update.SensorUpdateService;
import pl.aircheck.server.station.Station;
import pl.aircheck.server.station.StationService;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final SensorUpdateService sensorUpdateService;
    private final StationService stationService;
    private final RestTemplate restTemplate;
    private final Config config;

    public SensorService(SensorRepository sensorRepository, SensorUpdateService sensorUpdateService, StationService stationService, Config config, RestTemplateBuilder restTemplateBuilder) {
        this.sensorRepository = sensorRepository;
        this.sensorUpdateService = sensorUpdateService;
        this.stationService = stationService;
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public Sensor getSingle(long id) {
        if(isAnUpdateNeeded(id)) {
            Optional<List<Sensor>> sensorFromOrigin = getAllFromOrigin(id);
            if(sensorFromOrigin.isPresent()) {
                updateData(sensorFromOrigin.get(), id);
                return sensorFromOrigin.get().stream()
                        .filter(((station) -> station.getId() == id))
                        .findFirst()
                        .orElse(null);
            }
        }
        return sensorRepository.findById(id).orElse(null);
    }

    public List<Sensor> getAll(long id) throws NoDataFromOriginException {
        if(isAnUpdateNeeded(id)) {
            Optional<List<Sensor>> sensorsFromOrigin = getAllFromOrigin(id);
            sensorsFromOrigin.ifPresent((s) -> updateData(s, id));
            return sensorsFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        return sensorRepository.getAllByStationId(id);
    }

    public void updateData(List<Sensor> sensors, long id) {
        sensorUpdateService.deleteUpdateDate(sensors.get(0).getUpdate());
        sensorRepository.deleteAllById(Stream.of(id).toList());

        SensorUpdate update = new SensorUpdate(LocalDate.now());
        sensorUpdateService.saveUpdateDate(update);
        Station station = stationService.getSingle(id);

        for(Sensor sensor: sensors) {
            sensor.setUpdate(update);
            sensor.setStation(station);
        }
        sensorRepository.saveAll(sensors);
    }

    public Optional<List<Sensor>> getAllFromOrigin(long id) {
        ResponseEntity<List<Sensor>> responseEntity = restTemplate.exchange(
                config.getOriginUrl() + config.getSensorEndpoint() + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Sensor>>() {}
        );
        return Optional.ofNullable(responseEntity.getBody());
    }

    public boolean isAnUpdateNeeded(long id) {
        Optional<LocalDate> updateDate = sensorRepository.getUpdateDate(id);
        if(updateDate.isPresent()) {
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(updateDate.get(), currentDate);
            return period.getDays() > config.getExpirationStations();
        } else return true;
    }

}
