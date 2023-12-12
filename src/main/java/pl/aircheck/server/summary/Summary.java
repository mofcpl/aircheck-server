package pl.aircheck.server.summary;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import pl.aircheck.server.BufferEntity;


@Entity
@AttributeOverride(name = "data", column = @Column(length = 1000))
public class Summary extends BufferEntity {
}
