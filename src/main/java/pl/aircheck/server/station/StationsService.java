package pl.aircheck.server.station;

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
import java.util.List;
import java.util.Optional;

@Service
public
class StationsService {

    private final StationsRepository stationsRepository;
    private final RestTemplate restTemplate;
    private final Config config;


    public StationsService(StationsRepository stationRepository, Config config, RestTemplateBuilder restTemplateBuilder) {
        this.stationsRepository = stationRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    //Trzeba wawalić magic number
    public String getData() throws NoDataFromOriginException {
        Optional<Stations> buffer = stationsRepository.findById(1L);
        if(buffer.isEmpty() || isOutdated(buffer.get())) {
            Optional<String> dataFromOrigin = getAllFromOrigin();
            dataFromOrigin.ifPresent(this::updateData);
            return dataFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        return buffer.get().getData();
    }

    public void updateData(String data) {
        stationsRepository.deleteAll();

        Stations buffer = new Stations(1L, LocalDateTime.now(), data);
        stationsRepository.save(buffer);
    }
    public Optional<String> getAllFromOrigin() {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                config.getOriginUrl()+ config.getStationEndpoint(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}
        );
        return Optional.ofNullable(responseEntity.getBody());
    }

    private boolean isOutdated(Stations buffer) {
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(buffer.getUpdate(), currentDate);
        return duration.toDays() > config.getExpirationStations();
    }
}
