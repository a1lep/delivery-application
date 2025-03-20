package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.Weather;

import java.util.Optional;

public interface WeatherRepository {

    void save(Weather weather);
    Optional<Weather> getLatestByStationName(String stationName);
}
