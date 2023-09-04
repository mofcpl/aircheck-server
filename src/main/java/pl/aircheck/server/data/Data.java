package pl.aircheck.server.data;

import jakarta.persistence.*;
import pl.aircheck.server.data.update.DataUpdate;
import pl.aircheck.server.data.values.DataValues;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Data {

    @Id
    private long id;

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "update_id")
    private DataUpdate update;
    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "data_id")
    private List<DataValues> values = new ArrayList<>();

    public Data() {
    }

    public Data(long id, DataUpdate update, List<DataValues> values) {
        this.id = id;
        this.update = update;
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

}
