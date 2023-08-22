package pl.aircheck.server.sensor;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import pl.aircheck.server.sensor.update.SensorUpdate;
import pl.aircheck.server.station.Station;

import java.time.LocalDate;

@Entity
public class Sensor {

    @Id
    private long id;

    private String paramName;

    @ManyToOne()
    @JoinColumn(name = "update_id")
    private SensorUpdate update;
    @ManyToOne()
    @JoinColumn(name = "station_id")
    private Station station;


    public Sensor() {
    }

    public Sensor(long id, String paramName) {
        this.id = id;
        this.paramName = paramName;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public SensorUpdate getUpdate() {
        return update;
    }

    public void setUpdate(SensorUpdate update) {
        this.update = update;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    @JsonProperty("param")
    private void unpackParam(JsonNode param) {
        this.paramName = param.get("paramName").asText();
    }
}
