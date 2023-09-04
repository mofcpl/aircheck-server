package pl.aircheck.server.data.values;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import pl.aircheck.server.data.Data;


@Entity
public class DataValues {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "data_value")
    private float value;
    private String date;

    public DataValues() {
    }

    public DataValues(long id, float value, String date) {
        this.id = id;
        this.value = value;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
