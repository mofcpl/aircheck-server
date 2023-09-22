package pl.aircheck.server.summary;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.aircheck.server.Config;
import pl.aircheck.server.NoDataFromOriginException;

import java.time.Duration;

@Controller
public class SummaryController {

    private final SummaryService summaryService;

    public SummaryController(SummaryService summaryService, Config config) {
        this.summaryService = summaryService;
        summaryService.setExpirationTime(config.getExpirationSummary());
        summaryService.setEndpoint(config.getSummaryEndpoint());
        summaryService.setDurationUnit(Duration::toMinutes);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/aqindex/getIndex/{id}")
    @ResponseBody
    public ResponseEntity<String> getSummary(@PathVariable("id") long id) throws NoDataFromOriginException {
        return ResponseEntity.ok(summaryService.getData(id, new Summary()));
    }
}
