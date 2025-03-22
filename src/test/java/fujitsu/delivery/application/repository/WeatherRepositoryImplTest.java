package fujitsu.delivery.application.repository;


import fujitsu.delivery.application.model.Weather;
import fujitsu.delivery.application.repository.impl.WeatherRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherRepositoryImplTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private WeatherRepositoryImpl weatherRepository;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> paramsCaptor;

    @Captor
    private ArgumentCaptor<RowMapper<Weather>> rowMapperCaptor;

    @Test
    public void save_shouldExecuteInsertQuery() {
        // Arrange
        Weather weather =  new Weather("Keila",27987.0,-14.5,5.0, "clear", Timestamp.from(Instant.now()));
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        weatherRepository.save(weather);

        // Assert
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramsCaptor.capture());
        String sql = sqlCaptor.getValue();
        Map<String, Object> params = paramsCaptor.getValue();

        assertTrue(sql.contains("""
                                INSERT INTO weather(station_name, wmo_code, air_temperature, wind_speed, phenomenon)
                                VALUES(:stationName, :wmoCode, :airTemperature, :windSpeed, :phenomenon)
                                """));

        assertEquals(weather.stationName(), params.get("stationName"));
        assertEquals(weather.wmoCode(), params.get("wmoCode"));
        assertEquals(weather.airTemperature(), params.get("airTemperature"));
        assertEquals(weather.windSpeed(), params.get("windSpeed"));
        assertEquals(weather.phenomenon(), params.get("phenomenon"));
    }

    @Test
    public void getLatestByStationName_shouldReturnLatestWeather_whenRecordExists() {
        // Arrange
        String stationName = "Station";
        Timestamp createTimestamp = Timestamp.from(Instant.now());
        Weather expectedWeather = new Weather(stationName, 100.0, 25.0, 10.0, "Sunny", createTimestamp);
        when(jdbcTemplate.query(anyString(), anyMap(), any(RowMapper.class)))
                .thenReturn(List.of(expectedWeather));

        // Act
        Optional<Weather> actualWeather = weatherRepository.getLatestByStationName(stationName);

        // Assert
        assertTrue(actualWeather.isPresent());
        assertEquals(expectedWeather, actualWeather.get());

        verify(jdbcTemplate).query(sqlCaptor.capture(), paramsCaptor.capture(), rowMapperCaptor.capture());
        String sql = sqlCaptor.getValue();
        Map<String, Object> params = paramsCaptor.getValue();
        RowMapper<Weather> rowMapper = rowMapperCaptor.getValue();

        assertTrue(sql.contains("""
                                SELECT station_name, wmo_code, air_temperature, wind_speed, phenomenon, create_timestamp
                                FROM weather
                                WHERE station_name LIKE CONCAT('%', :stationName, '%')
                                ORDER BY create_timestamp DESC
                                LIMIT 1
                                """));

        assertEquals(stationName, params.get("stationName"));

        try {
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString("station_name")).thenReturn(stationName);
            when(rs.getDouble("wmo_code")).thenReturn(100.0);
            when(rs.getDouble("air_temperature")).thenReturn(25.0);
            when(rs.getDouble("wind_speed")).thenReturn(10.0);
            when(rs.getString("phenomenon")).thenReturn("Sunny");
            when(rs.getTimestamp("create_timestamp")).thenReturn(createTimestamp);
            Weather mappedWeather = rowMapper.mapRow(rs, 0);
            assertEquals(expectedWeather, mappedWeather);

        } catch (SQLException e) {
            fail("SQLException during row mapping");
        }
    }

}
