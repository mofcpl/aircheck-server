package pl.aircheck.server.data;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.aircheck.server.BufferService;
import pl.aircheck.server.Config;
import pl.aircheck.server.NoDataFromOriginException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
public class DataService extends BufferService<Data> {

    public DataService(DataRepository bufferRepository, RestTemplateBuilder restTemplateBuilder, Config config) {
        super(bufferRepository, restTemplateBuilder, config);
    }
}
