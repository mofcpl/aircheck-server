package pl.aircheck.server.sensor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.aircheck.server.ConfigProp;
import pl.aircheck.server.NoDataFromOriginException;

import java.time.Duration;

@Controller
class SensorController {

    private final SensorService sensorService;

    SensorController(SensorService sensorService, ConfigProp config) {
        this.sensorService = sensorService;
        this.sensorService.setExpirationTime(config.getExpirationSensors());
        this.sensorService.setEndpoint(config.getSensorEndpoint());
        this.sensorService.setDurationUnit(Duration::toDays);
    }

    @RequestMapping("/station/sensors/{id}")
    @ResponseBody
    public ResponseEntity<String> getSensors(@PathVariable("id") long id) throws NoDataFromOriginException {
        return ResponseEntity.ok(sensorService.getData(id, new Sensor()));
    }
}