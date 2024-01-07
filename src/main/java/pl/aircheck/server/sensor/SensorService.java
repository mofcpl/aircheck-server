package pl.aircheck.server.sensor;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import pl.aircheck.server.BufferService;
import pl.aircheck.server.ConfigProp;

@Service
public class SensorService extends BufferService<Sensor> {

    public SensorService(SensorRepository bufferRepository, RestTemplateBuilder restTemplateBuilder, ConfigProp config) {
        super(bufferRepository, restTemplateBuilder, config);
    }
}
