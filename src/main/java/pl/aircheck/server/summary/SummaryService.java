package pl.aircheck.server.summary;

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
public class SummaryService {

    private final SummaryRepository summaryRepository;
    private final RestTemplate restTemplate;
    private final Config config;

    public SummaryService(SummaryRepository summaryRepository, RestTemplateBuilder restTemplateBuilder, Config config) {
        this.summaryRepository = summaryRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public String getData(long id) throws NoDataFromOriginException {
        Optional<Summary> buffer = summaryRepository.findById(id);
        if(buffer.isEmpty() || isOutdated(buffer.get())) {
            Optional<String> dataFromOrigin = getDataFromOrigin(id);
            dataFromOrigin.ifPresent((d) -> updateData(d, id));
            return dataFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        return buffer.get().getData();
    }

    private void updateData(String data, long id) {
        summaryRepository.deleteById(id);

        Summary buffer = new Summary(id, LocalDateTime.now(), data);
        summaryRepository.save(buffer);
    }

    private Optional<String> getDataFromOrigin(long id) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                config.getOriginUrl()+ config.getSummaryEndpoint() + id,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}
        );
        return Optional.ofNullable(responseEntity.getBody());
    }

    private boolean isOutdated(Summary buffer) {
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(buffer.getUpdate(), currentDate);
        return duration.toMinutes() > config.getExpirationSummary();
    }
}
