package example.sensor.temp.controller;

import example.model.TempMessage;
import example.sensor.temp.Sensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.web.bind.annotation.*;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.web.bind.annotation.*;

@RestController
public class RequestGenerator {

    private static final Logger LOG = LoggerFactory.getLogger(RequestGenerator.class);

    @Autowired
    private IntegrationFlow mqttOutboundFlow;

    private Jackson2JsonObjectMapper mapper = new Jackson2JsonObjectMapper();

    @CrossOrigin(origins = "*")
    @GetMapping(value = "/generate")
    public ResponseEntity<?> generate(@RequestParam(value = "duration", required = true) Long duration, @RequestParam(value = "rps", required = false, defaultValue="100000") Integer rps) throws InterruptedException {

        LOG.info("Begin generator " + new Date());

        Disposable disposable = Flux.interval(Duration.ofSeconds(1))
                .map(tick -> ThreadLocalRandom.current().nextDouble(70.0, 72.0))
                .subscribe(temp -> {

                    long initTIme = System.currentTimeMillis();
                    for(int i=0;i<rps;i++){
                        String mqttClientId = "temp-" + UUID.randomUUID().toString().replace("-", "");
                        mqttOutboundFlow.getInputChannel().send(new Message<String>() {
                            @Override
                            public String getPayload() {
                                try {
                                    return mapper.toJson(new TempMessage(mqttClientId, temp));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    throw new RuntimeException("Exception occurred building mqtt message", e);
                                }
                            }
                            @Override
                            public MessageHeaders getHeaders() {
                                return new MessageHeaders(Collections.EMPTY_MAP);
                            }
                        });

                        if(System.currentTimeMillis() - initTIme > 1000){
                            break;
                        }
                    }
                });

        try { Thread.sleep(duration); }
        catch (InterruptedException e) { //e.printStackTrace();
            }

        disposable.dispose();

        LOG.info("End generator " + new Date());

        return ResponseEntity.ok("OK");
    }
}
