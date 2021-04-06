package example.sensor.temp;

import example.model.TempMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.json.Jackson2JsonObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Mimics a temperature sensor that records temperature data every 1 second.
 */
@Component
public class Sensor implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(Sensor.class);

    @Autowired
    private IntegrationFlow mqttOutboundFlow;

    private Jackson2JsonObjectMapper mapper = new Jackson2JsonObjectMapper();

    @Override
    public void run(String... args) throws Exception {

        Flux.interval(Duration.ofSeconds(5))
                .map(tick -> ThreadLocalRandom.current().nextDouble(70.0, 72.0))
                .subscribe(temp -> {
                    String mqttClientId = "temp-" + UUID.randomUUID().toString().replace("-", "");
                    LOG.info("Temp: " + temp);
                    mqttOutboundFlow.getInputChannel().send(new Message<String>() {
                        @Override
                        public String getPayload() {
                            try {
                                return mapper.toJson(new TempMessage(mqttClientId, temp));
                            } catch (Exception e) {
                                throw new RuntimeException("Exception occurred building mqtt message", e);
                            }
                        }

                        @Override
                        public MessageHeaders getHeaders() {
                            return new MessageHeaders(Collections.EMPTY_MAP);
                        }
                    });
                });

        Thread.currentThread().join();
    }
}
