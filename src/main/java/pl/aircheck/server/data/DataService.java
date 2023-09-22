package pl.aircheck.server.data;

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
public class DataService {

    private final DataRepository dataRepository;
    private final RestTemplate restTemplate;
    private final Config config;

    public DataService(DataRepository dataRepository, RestTemplateBuilder restTemplateBuilder, Config config) {
        this.dataRepository = dataRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public String getAll(long id) throws NoDataFromOriginException {
        Optional<Data> buffer = dataRepository.findById(id);
        if(buffer.isEmpty() || isOutdated(buffer.get())) {
            Optional<String> dataFromOrigin = getAllFromOrigin(id);
            dataFromOrigin.ifPresent((d) -> updateData(d, id));
            return dataFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        return buffer.get().getData();
    }

    public void updateData(String data, long id) {
        dataRepository.deleteById(id);

        Data buffer = new Data(id, LocalDateTime.now(), data);
        dataRepository.save(buffer);
    }

    public Optional<String> getAllFromOrigin(long id) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                config.getOriginUrl() + config.getDataEndpoint() + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}
        );
        return Optional.ofNullable(responseEntity.getBody());
    }

    public boolean isOutdated(Data buffer) {
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(buffer.getUpdate(), currentDate);
        return duration.toMinutes() > config.getExpirationData();
    }
}
