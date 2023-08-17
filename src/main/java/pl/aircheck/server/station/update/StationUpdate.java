package pl.aircheck.server.station.update;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
class StationUpdate {

    @Id
    private final long id = 1L;
    private LocalDate time;

    public StationUpdate() {
    }

    public StationUpdate(LocalDate time) {
        this.time = time;
    }

    public void setTime(LocalDate time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public LocalDate getTime() {
        return time;
    }
}
