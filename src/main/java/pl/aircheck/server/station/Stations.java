package pl.aircheck.server.station;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Stations {

    @Id
    private long id;

    private LocalDateTime update;
    @Lob
    private String data;

    public Stations() {
    }

    public Stations(long id, LocalDateTime update, String data) {
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

    public LocalDateTime getUpdate() {
        return update;
    }

    public void setUpdate(LocalDateTime update) {
        this.update = update;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
