package example.service.mqtt.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.model.TempMessage;
import example.service.mqtt.config.MqttConfiguration;
import example.service.mqtt.statistics.TempStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
public class TempMessageHandler implements MessageHandler {
    private static final Logger LOG = LoggerFactory.getLogger(TempMessageHandler.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private TempStatistics tempStats;

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        try {
            //LOG.info("Received Message: " + message.getPayload().toString());
            TempMessage tempMessage = mapper.readerFor(TempMessage.class).readValue(message.getPayload().toString());
            tempMessage.setClientId(MqttConfiguration.clientId);
            tempMessage.setModifiedTimestamp(new Date());
            tempStats.save(tempMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
