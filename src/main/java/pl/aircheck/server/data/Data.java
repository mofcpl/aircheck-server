package pl.aircheck.server.data;

import jakarta.persistence.*;
import pl.aircheck.server.BufferEntity;

import java.time.LocalDateTime;

@Entity
@AttributeOverride(name = "data", column = @Column(length = 5000))
public class Data extends BufferEntity {
}

