package pl.aircheck.server;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Function;

public abstract class BufferService<T extends BufferEntity> {
    private final CrudRepository<T, Long> bufferRepository;
    private final RestTemplate restTemplate;
    private final ConfigProp config;

    private int expirationTime;
    private Function<Duration, Long> durationUnit;
    private String endpoint;
    private boolean addIdToEndpoint = true;

    public BufferService(CrudRepository<T, Long> bufferRepository, RestTemplateBuilder restTemplateBuilder, ConfigProp config) {
        this.bufferRepository = bufferRepository;
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public String getData(long id, T objectInstance) throws NoDataFromOriginException {
        Optional<T> buffer = bufferRepository.findById(id);
        if(buffer.isEmpty() || isOutdated(buffer.get())) {
            Optional<String> dataFromOrigin = getDataFromOrigin(id);
            dataFromOrigin.ifPresent((d) -> updateData(d, id, objectInstance));
            return dataFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        return buffer.get().getData();
    }

    public void setExpirationTime(int expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setDurationUnit(Function<Duration, Long> durationUnit) {
        this.durationUnit = durationUnit;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public void setAddIdToEndpoint(boolean addIdToEndpoint) {
        this.addIdToEndpoint = addIdToEndpoint;
    }

    private void updateData(String data, long id, T objectInstance) {
        objectInstance.setId(id);
        objectInstance.setUpdateTime(LocalDateTime.now());
        objectInstance.setData(data);
        bufferRepository.save(objectInstance);
    }

    //nie obsługiwany wyjątek tutaj jest
    private Optional<String> getDataFromOrigin(long id) {
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                config.getOriginUrl() + endpoint + (addIdToEndpoint ? id : ""),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<String>() {}
        );
        return Optional.ofNullable(responseEntity.getBody());
    }

    private boolean isOutdated(T buffer) {
        LocalDateTime currentDate = LocalDateTime.now();
        Duration duration = Duration.between(buffer.getUpdateTime(), currentDate);
        return durationUnit.apply(duration) > expirationTime;
    }

}
