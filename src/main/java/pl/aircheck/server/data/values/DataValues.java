package pl.aircheck.server.data.values;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import pl.aircheck.server.data.Data;


@Entity
public class DataValues {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "data_value")
    private float value;
    private String date;

    @ManyToOne()
    @JoinColumn(name = "data_id")
    private Data data;

    public DataValues() {
    }

    public DataValues(float value, String date) {
        this.value = value;
        this.date = date;
    }

    public float getDataValue() {
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
