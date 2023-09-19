package pl.aircheck.server.station;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

interface StationsRepository extends CrudRepository<StationsBuffer, Long> {
    @Query(value = "SELECT * FROM STATIONS_BUFFER LIMIT 1", nativeQuery = true)
    public Optional<StationsBuffer> findFirst();
}
