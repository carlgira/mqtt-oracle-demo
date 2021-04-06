package example.service.mqtt.statistics;

import example.model.TempMessage;
import org.springframework.data.repository.CrudRepository;


public interface TempStatistics  extends CrudRepository<TempMessage, String> {
}
