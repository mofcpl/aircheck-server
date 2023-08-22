package pl.aircheck.server.station;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import pl.aircheck.server.Config;
import pl.aircheck.server.NoDataFromOriginException;
import pl.aircheck.server.station.update.StationUpdateService;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public
class StationService {

    private final StationRepository stationRepository;
    private final StationUpdateService updateStationService;
    private final RestTemplate restTemplate;
    private final Config config;

    public StationService(StationRepository stationRepository, StationUpdateService updateStationService, Config config, RestTemplateBuilder restTemplateBuilder) {
        this.stationRepository = stationRepository;
        this.updateStationService = updateStationService;
        this.restTemplate = restTemplateBuilder.build();
        this.config = config;
    }

    public Station getSingle(long id){
        if(isAnUpdateNeeded()) {
            Optional<List<Station>> stationsFromOrigin = getAllFromOrigin();
            if(stationsFromOrigin.isPresent()) {
                updateData(stationsFromOrigin.get());
                //I tak potrzebuje tutaj tego kodu więc można to wydzielić do osobnej metody i używać zamiast sqlek
                return stationsFromOrigin.get().stream()
                        .filter(((station) -> station.getId() == id))
                        .findFirst()
                        .orElse(null);
            }
        }
        return stationRepository.findById(id).orElse(null);
    }

    public List<Station> getAll() throws NoDataFromOriginException {
        if(isAnUpdateNeeded()) {
            Optional<List<Station>> stationsFromOrigin = getAllFromOrigin();
            stationsFromOrigin.ifPresent(this::updateData);
            return stationsFromOrigin.orElseThrow(() -> new NoDataFromOriginException("Data is outdated and fetch from origin failed"));
        }
        Iterable<Station> stations = stationRepository.findAll();
        List<Station> stationsFromDatabase = new ArrayList<>();
        stations.forEach(stationsFromDatabase::add);
        return stationsFromDatabase;
    }

    public void updateData(List<Station> stations) {
        stationRepository.deleteAll();
        stationRepository.saveAll(stations);
        updateStationService.updateUpdateDate(LocalDate.now());
    }
    public Optional<List<Station>> getAllFromOrigin() {
        ResponseEntity<List<Station>> responseEntity = restTemplate.exchange(
                config.getOriginUrl()+ config.getStationEndpoint(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Station>>() {}
        );
        return Optional.ofNullable(responseEntity.getBody());
    }

    public boolean isAnUpdateNeeded() {
        Optional<LocalDate> updateDate = updateStationService.getUpdateDate();
        if(updateDate.isPresent()) {
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(updateDate.get(), currentDate);
            return period.getDays() > config.getExpirationStations();
        } else return true;
    }
}
