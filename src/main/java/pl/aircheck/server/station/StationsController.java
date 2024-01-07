package pl.aircheck.server.station;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.aircheck.server.ConfigProp;
import pl.aircheck.server.NoDataFromOriginException;

import java.time.Duration;

@Controller
class StationsController {

    private final StationsService stationService;
    final long OnlyOneRecordId = 1L;

    public StationsController(StationsService stationService, ConfigProp config) {
        this.stationService = stationService;
        this.stationService.setExpirationTime(config.getExpirationStations());
        this.stationService.setEndpoint(config.getStationEndpoint());
        this.stationService.setDurationUnit(Duration::toDays);
        this.stationService.setAddIdToEndpoint(false);
    }

    @RequestMapping("/station/findAll")
    @ResponseBody
    public ResponseEntity<String> findAll() throws NoDataFromOriginException {
        return ResponseEntity.ok(stationService.getData(OnlyOneRecordId, new Stations()));
    }
}
