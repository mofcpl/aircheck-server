package pl.aircheck.server.data;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import pl.aircheck.server.BufferService;
import pl.aircheck.server.ConfigProp;


@Service
public class DataService extends BufferService<Data> {

    public DataService(DataRepository bufferRepository, RestTemplateBuilder restTemplateBuilder, ConfigProp config) {
        super(bufferRepository, restTemplateBuilder, config);
    }
}
