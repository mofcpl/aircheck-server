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

    public Optional<List<Station>> updateStationsAndGetAll() {
        try{
            ResponseEntity<List<Station>> responseEntity = restTemplate.exchange(
                    config.getOriginUrl()+ config.getStationEndpoint(),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Station>>() {}
            );
            Optional<List<Station>> stations = Optional.ofNullable(responseEntity.getBody());
            if(stations.isPresent()) {
                stationRepository.deleteAll();
                stationRepository.saveAll(stations.get());
                updateStationService.updateUpdateDate(LocalDate.now());
                return stations;
            }
            else {
                throw new NoDataFromOriginException("The response from origin was empty");
            }

        } catch(NoDataFromOriginException | RestClientException e) {
            System.out.println("Server wasn't able to fetch data from origin. Update failed!");
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<Station> findAll() {
        Iterable<Station> stations = this.stationRepository.findAll();
        List<Station> list = new ArrayList<>();
        stations.forEach(list::add);
        return list;
    }

    public boolean isStationsUpToDate() {
        Optional<LocalDate> updateDate = updateStationService.getUpdateDate();

        if(updateDate.isPresent()) {
            LocalDate currentDate = LocalDate.now();
            Period period = Period.between(updateDate.get(), currentDate);
            int stationPeriod = config.getExpirationStations();
            return period.getDays() < config.getExpirationStations();
        } else return false;
    }
}
