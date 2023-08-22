package pl.aircheck.server.sensor.update;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class SensorUpdate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;

    public SensorUpdate() {
    }

    public SensorUpdate(LocalDate date) {
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
