package pl.aircheck.server.summary;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import pl.aircheck.server.BufferService;
import pl.aircheck.server.Config;

@Service
public class SummaryService extends BufferService<Summary> {
    public SummaryService(SummaryRepository bufferRepository, RestTemplateBuilder restTemplateBuilder, Config config) {
        super(bufferRepository, restTemplateBuilder, config);
    }
}
