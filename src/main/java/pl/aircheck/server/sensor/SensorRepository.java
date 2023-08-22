package pl.aircheck.server.sensor;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface SensorRepository extends CrudRepository<Sensor, Long> {

    @Query(value = "SELECT u.date FROM Sensor s JOIN Sensor_Update u WHERE s.station_id = :id LIMIT 1", nativeQuery = true)
    public Optional<LocalDate> getUpdateDate(@Param("id")long id);

    @Query("SELECT s FROM Sensor s WHERE s.station.id = :id")
    public List<Sensor> getAllByStationId(long id);
}
