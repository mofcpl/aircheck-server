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
import pl.aircheck.server.data.update.DataUpdateService;
import pl.aircheck.server.sensor.Sensor;
import pl.aircheck.server.sensor.SensorService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


@Service
public class DataService {

    private final DataRepository dataRepository;
    private final DataUpdateService dataUpdateService;
    private final SensorService sensorService;
    private final RestTemplate restTemplate;
    private final Config config;

    public DataService(DataRepository dataRepository, DataUpdateService dataUpdateService, SensorService sensorService, RestTemplateBuilder restTemplateBuilder, Config config) {
        this.dataRepository = dataRepository;
        this.dataUpdateService = dataUpdateService;
        this.sensorService = sensorService;
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public Data getAll(long id) throws NoDataFromOriginException {
        //Trzeba rozdzielić sprawdzanie na, czy dane istnieją w bazie i czy są aktualne
        if(isAnUpdateNeeded(id)) {
            Optional<Data> dataFromOrigin = getAllFromOrigin(id);
            dataFromOrigin.ifPresent((s) -> updateData(s, id));
            return dataFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        return dataRepository.findById(id).orElse(null);
    }

    public void updateData(Data data, long id) {
        dataUpdateService.deleteUpdateDate(data.getUpdate());
        dataRepository.deleteById(id);

        DataUpdate update = new DataUpdate(LocalDateTime.now());
        dataUpdateService.saveUpdateDate(update);
        Sensor sensor = sensorService.getSingle(id);

        data.setUpdate(update);
        data.setSensor(sensor);

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
        Optional<Data> Data = dataRepository.findById(id);
        Optional<LocalDateTime> updateDate = Data.map((d) -> d.getUpdate().getDate());
        if(updateDate.isPresent()) {
            LocalDateTime currentDateTime = LocalDateTime.now();
            Duration duration = Duration.between(updateDate.get(), currentDateTime);
            return (duration.getSeconds() / 60) > config.getExpirationData();
        } else return true;
    }
}
