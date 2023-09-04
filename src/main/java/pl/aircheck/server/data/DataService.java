package pl.aircheck.server.data;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.aircheck.server.Config;
import pl.aircheck.server.NoDataFromOriginException;
import pl.aircheck.server.data.update.DataUpdate;

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

    public Data getAll(long id) throws NoDataFromOriginException {
        if(isAnUpdateNeeded(id)) {
            Optional<Data> dataFromOrigin = getAllFromOrigin(id);
            dataFromOrigin.ifPresent((s) -> updateData(s, id));
            return dataFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        return dataRepository.findById(id).orElse(null);
    }

    public void updateData(Data data, long id) {
        dataRepository.deleteById(id);
        DataUpdate update = new DataUpdate(LocalDateTime.now());
        data.setId(id);
        data.setUpdate(update);
        dataRepository.save(data);
    }

    public Optional<Data> getAllFromOrigin(long id) {
        ResponseEntity<Data> responseEntity = restTemplate.exchange(
                config.getOriginUrl() + config.getDataEndpoint() + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Data>() {}
        );
        return Optional.ofNullable(responseEntity.getBody());
    }

    public boolean isAnUpdateNeeded(long id) {
        Optional<LocalDateTime> updateDate = dataRepository.findById(id)
                .map((d) -> d.getUpdate().getDate());
        if(updateDate.isPresent()) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(updateDate.get(), currentDateTime);
            return (duration.getSeconds() / 60) > config.getExpirationData();
        } else return true;
    }
}
