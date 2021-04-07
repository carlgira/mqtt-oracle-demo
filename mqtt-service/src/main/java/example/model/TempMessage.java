package example.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Cacheable(false)
@Table(name = "SENSOR")
public class TempMessage  implements Serializable{

    @Id
    @Column(name="SENSOR_ID")
    private String sensorId;

    @Column(name="TEMP")
    private double temp;

    @Column(name="CLIENT_ID")
    private String clientId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="MOD_TIMESTAMP")
    private Date modifiedTimestamp;

    public TempMessage() {}

    public TempMessage(String sensorId, double temp) {
        this.sensorId = sensorId;
        this.temp = temp;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public Date getModifiedTimestamp() {
        return modifiedTimestamp;
    }

    public void setModifiedTimestamp(Date modifiedTimestamp) {
        this.modifiedTimestamp = modifiedTimestamp;
    }

    @Override
    public String toString() {
        return "TempMessage{" +
                "sensorId='" + sensorId + '\'' +
                ", temp=" + temp +
                '}';
    }
}
