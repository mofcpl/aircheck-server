package pl.aircheck.server.station;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.aircheck.server.NoDataFromOriginException;

import java.util.List;

@Controller
class StationController {

    private final StationService stationService;

    public StationController(StationService stationService) {
        this.stationService = stationService;
    }

    @RequestMapping("/station/findAll")
    @ResponseBody
    public ResponseEntity<List<Station>> findAll() throws NoDataFromOriginException {
        return ResponseEntity.ok(stationService.getAll());
    }
}
