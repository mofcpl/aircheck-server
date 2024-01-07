package pl.aircheck.server.station;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import pl.aircheck.server.BufferService;
import pl.aircheck.server.ConfigProp;

@Service
public class StationsService extends BufferService<Stations> {
    public StationsService(StationsRepository bufferRepository, RestTemplateBuilder restTemplateBuilder, ConfigProp config) {
        super(bufferRepository, restTemplateBuilder, config);
    }
}
