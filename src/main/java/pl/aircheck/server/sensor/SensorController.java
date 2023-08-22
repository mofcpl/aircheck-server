package pl.aircheck.server.sensor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.aircheck.server.NoDataFromOriginException;

import java.util.List;

@Controller
class SensorController {

    private final SensorService sensorService;

    SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @RequestMapping("/station/sensors/{id}")
    @ResponseBody
    public ResponseEntity<List<Sensor>> getSensors(@PathVariable("id") long id) throws NoDataFromOriginException {
        return ResponseEntity.ok(sensorService.getAll(id));
    }
}