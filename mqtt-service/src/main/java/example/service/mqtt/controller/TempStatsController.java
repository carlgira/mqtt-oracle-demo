package example.service.mqtt.controller;

import example.service.mqtt.controller.model.GetTempAverageResponse;
import example.service.mqtt.controller.model.GetTempCountResponse;
import example.service.mqtt.controller.model.GetTempsResponse;
import example.service.mqtt.statistics.TempStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for accessing temperature data.
 */
@RestController
public class TempStatsController {

    @Autowired
    private TempStatistics tempStats;

    @GetMapping(value = "/temps/stats",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTemps() {
        GetTempsResponse response = new GetTempsResponse();
        response.setCount(tempStats.count());
        //response.setAverage(tempStats.getAverage());

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/temps/stats/count",
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getTempCount() {
        return ResponseEntity.ok(new GetTempCountResponse(tempStats.count()));
    }

}
