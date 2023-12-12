package pl.aircheck.server.station;

import jakarta.persistence.*;
import pl.aircheck.server.BufferEntity;

import java.time.LocalDateTime;

@Entity
@AttributeOverride(name = "data", column = @Column(length = 70000))
public class Stations extends BufferEntity {
}
