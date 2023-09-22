package pl.aircheck.server.data;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.aircheck.server.NoDataFromOriginException;

import java.util.List;

@Controller
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @RequestMapping("/data/getData/{id}")
    @ResponseBody
    public ResponseEntity<String> getData(@PathVariable("id") long id) throws NoDataFromOriginException {
        return ResponseEntity.ok(dataService.getAll(id));
    }
}
