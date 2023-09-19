package pl.aircheck.server.station;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class StationsBuffer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "update_id")
    private StationsUpdate update;

    @OneToMany(cascade = {CascadeType.ALL}, orphanRemoval = true)
    @JoinColumn(name = "station_id")
    private List<Station> data;

    public StationsBuffer() {
    }

    public StationsBuffer(StationsUpdate update, List<Station> data) {
        this.update = update;
        this.data = data;
    }

    public StationsBuffer(long id, StationsUpdate update, List<Station> data) {
        this.id = id;
        this.update = update;
        this.data = data;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StationsUpdate getUpdate() {
        return update;
    }

    public void setUpdate(StationsUpdate update) {
        this.update = update;
    }

    public List<Station> getData() {
        return data;
    }

    public void setData(List<Station> data) {
        this.data = data;
    }
}
