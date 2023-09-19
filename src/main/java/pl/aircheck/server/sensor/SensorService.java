package pl.aircheck.server.sensor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.aircheck.server.Config;
import pl.aircheck.server.NoDataFromOriginException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SensorService {

    private final SensorRepository sensorRepository;
    private final RestTemplate restTemplate;
    private final Config config;


    public SensorService(SensorRepository sensorRepository, Config config, RestTemplateBuilder restTemplateBuilder) {
        this.sensorRepository = sensorRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public String getData(long id) throws NoDataFromOriginException {
        Optional<Sensor> buffer = sensorRepository.findById(id);
        if(buffer.isEmpty() || isOutdated(buffer.get())) {
            Optional<String> dataFromOrigin = getAllFromOrigin(id);
            dataFromOrigin.ifPresent((d) -> updateData(d, id));
            return dataFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        return buffer.get().getData();
    }

    public void updateData(String data, long id) {
        sensorRepository.deleteAll();

        Sensor buffer = new Sensor(id, LocalDateTime.now(), data);
        sensorRepository.save(buffer);
    }

    public Optional<String> getAllFromOrigin(long id) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                config.getOriginUrl()+ config.getSensorEndpoint() + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}
        );
        return Optional.ofNullable(responseEntity.getBody());
    }

    private boolean isOutdated(Sensor buffer) {
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(buffer.getUpdate(), currentDate);
        return duration.toDays() > config.getExpirationStations();
    }
}
