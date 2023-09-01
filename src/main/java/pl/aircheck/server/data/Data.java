package pl.aircheck.server.data;

import jakarta.persistence.*;
import pl.aircheck.server.data.update.DataUpdate;
import pl.aircheck.server.data.values.DataValues;
import pl.aircheck.server.sensor.Sensor;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Data {

    @Id
    private long id;

    @ManyToOne()
    @JoinColumn(name = "update_id")
    private DataUpdate update;
    @ManyToOne()
    @JoinColumn(name = "sensor_id")
    private Sensor sensor;
    @OneToMany(mappedBy = "data", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    private List<DataValues> values = new ArrayList<>();

    public Data() {
    }

    public Data(long id, DataUpdate update, Sensor sensor, List<DataValues> values) {
        this.id = id;
        this.update = update;
        this.sensor = sensor;
        this.values = values;
    }

    public void addValue(DataValues value) {
        values.add(value);
    }

    public List<DataValues> getValues() {
        return values;
    }

    public void setValues(List<DataValues> values) {
        this.values = values;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DataUpdate getUpdate() {
        return update;
    }

    public void setUpdate(DataUpdate update) {
        this.update = update;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }


}
