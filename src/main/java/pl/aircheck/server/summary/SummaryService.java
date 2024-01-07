package pl.aircheck.server.summary;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import pl.aircheck.server.BufferService;
import pl.aircheck.server.ConfigProp;

@Service
public class SummaryService extends BufferService<Summary> {
    public SummaryService(SummaryRepository bufferRepository, RestTemplateBuilder restTemplateBuilder, ConfigProp config) {
        super(bufferRepository, restTemplateBuilder, config);
    }
}
