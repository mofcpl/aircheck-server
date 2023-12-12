package pl.aircheck.server.sensor;

import jakarta.persistence.*;
import pl.aircheck.server.BufferEntity;

@Entity
@AttributeOverride(name = "data", column = @Column(length = 1000))
public class Sensor extends BufferEntity {
}
