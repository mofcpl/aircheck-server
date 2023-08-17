package pl.aircheck.server.station;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Optional;

@Controller
class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @RequestMapping("/station/findAll")
    @ResponseBody
    public List<Station> findAll() {

        if(stationService.isStationsUpToDate()) {
            return stationService.findAll();
        }
        else {
            Optional<List<Station>> stations = stationService.updateStationsAndGetAll();
            return stations.orElseGet(stationService::findAll);
        }
    }

}
