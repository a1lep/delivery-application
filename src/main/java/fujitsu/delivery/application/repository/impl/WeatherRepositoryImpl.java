package fujitsu.delivery.application.repository.impl;

import fujitsu.delivery.application.model.Weather;
import fujitsu.delivery.application.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class WeatherRepositoryImpl implements WeatherRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public void save(Weather weather) {
        final String sql = """
                INSERT INTO weather(station_name, wmo_code, air_temperature, wind_speed, phenomenon, create_timestamp)
                VALUES(:stationName, :wmoCode, :airTemperature, :windSpeed, :phenomenon, :createTimestamp)
                """;
        jdbcTemplate.update(sql, Map.of(
                "stationName", weather.stationName(),
                "wmoCode", weather.wmoCode(),
                "airTemperature", weather.airTemperature(),
                "windSpeed", weather.windSpeed(),
                "phenomenon", weather.phenomenon(),
                "createTimestamp", weather.createTimestamp()
        ));
    }

    @Override
    public Optional<Weather> getLatestByStationName(String stationName) {
        final String sql = """
                SELECT station_name, wmo_code, air_temperature, wind_speed, phenomenon, create_timestamp
                FROM weather WHERE station_name = :stationName
                ORDER BY create_timestamp DESC
                LIMIT 1
                """;
        return jdbcTemplate.query(sql, (rs, _) -> mapWeather(rs)).stream().findFirst();
    }

    private Weather mapWeather(ResultSet rs) throws SQLException {
        return new Weather(
                rs.getString("station_name"),
                rs.getDouble("wmo_code"),
                rs.getDouble("air_temperature"),
                rs.getDouble("wind_speed"),
                rs.getString("phenomenon"),
                rs.getTimestamp("create_timestamp")
        );
    }
}
