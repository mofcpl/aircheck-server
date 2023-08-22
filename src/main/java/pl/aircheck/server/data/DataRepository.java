package pl.aircheck.server.data;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DataRepository extends CrudRepository<Data, Long> {

    @Query(value = "SELECT u.date FROM Data d JOIN Data_Update u WHERE d.sensor_id = :id LIMIT 1", nativeQuery = true)
    public Optional<LocalDate> getUpdateDate(@Param("id")long id);

    @Query("SELECT d FROM Data d WHERE d.sensor.id = :id")
    public List<Data> getAllBySensorId(long id);
}
