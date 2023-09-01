package pl.aircheck.server.data.values;

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

    @ManyToOne()
    @JoinColumn(name = "data_id", nullable=false)
    private Data data;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public DataValues() {
    }

    public DataValues(float value, String date, Data data) {
        this.value = value;
        this.date = date;
        this.data = data;
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
