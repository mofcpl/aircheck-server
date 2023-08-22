package pl.aircheck.server.station.update;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
class StationUpdate {

    @Id
    private final long id = 1L;
    private LocalDate date;

    public StationUpdate() {
    }

    public StationUpdate(LocalDate time) {
        this.date = time;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }
}
