package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.WeatherFee;
import fujitsu.delivery.application.repository.impl.WeatherFeeRepositoryImpl;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WeatherFeeRepositoryImplTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private WeatherFeeRepositoryImpl weatherFeeRepository;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> paramsCaptor;

    @Captor
    private ArgumentCaptor<RowMapper<WeatherFee>> rowMapperCaptor;

    @Test
    public void saveOrUpdateWeatherFee_shouldExecuteInsertOrUpdateQuery() {
        // Arrange
        WeatherFee weatherFee = new WeatherFee(ConditionType.AIR_TEMPERATURE, 10.0, 20.0, "Sunny", "Car", 5.0);
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        weatherFeeRepository.saveOrUpdateWeatherFee(weatherFee);

        // Assert
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramsCaptor.capture());
        String sql = sqlCaptor.getValue();
        Map<String, Object> params = paramsCaptor.getValue();

        assertTrue(sql.contains("""
                                INSERT INTO weather_fees (vehicle_type, condition_type, min_value, max_value, weather_phenomenon, extra_fee)
                                VALUES (:vehicleType, :conditionType, :minValue, :maxValue, :weatherPhenomenon, :extraFee)
                                ON DUPLICATE KEY UPDATE
                                    min_value = :minValue,
                                    max_value = :maxValue,
                                    weather_phenomenon = :weatherPhenomenon,
                                    extra_fee = :extraFee
                                """));

        assertEquals(weatherFee.vehicleType(), params.get("vehicleType"));
        assertEquals(weatherFee.conditionType().name(), params.get("conditionType"));
        assertEquals(weatherFee.minValue(), params.get("minValue"));
        assertEquals(weatherFee.maxValue(), params.get("maxValue"));
        assertEquals(weatherFee.weatherPhenomenon(), params.get("weatherPhenomenon"));
        assertEquals(weatherFee.extraFee(), params.get("extraFee"));
    }

    @Test
    public void getWeatherFeeByVehicleAndCondition_shouldReturnWeatherFeeList() {
        // Arrange
        String vehicleType = "Car";
        WeatherFee weatherFee = new WeatherFee(ConditionType.AIR_TEMPERATURE, 10.0, 20.0, "Sunny", vehicleType, 5.0);
        when(jdbcTemplate.query(anyString(), anyMap(), any(RowMapper.class)))
                .thenReturn(List.of(weatherFee));

        // Act
        List<WeatherFee> weatherFeeList = weatherFeeRepository.getWeatherFeeByVehicleAndCondition(vehicleType);

        // Assert
        assertFalse(weatherFeeList.isEmpty());
        assertEquals(1, weatherFeeList.size());
        assertEquals(weatherFee, weatherFeeList.get(0));

        verify(jdbcTemplate).query(sqlCaptor.capture(), paramsCaptor.capture(), rowMapperCaptor.capture());
        String sql = sqlCaptor.getValue();
        Map<String, Object> params = paramsCaptor.getValue();
        RowMapper<WeatherFee> rowMapper = rowMapperCaptor.getValue();

        assertTrue(sql.contains("""
                                SELECT id, vehicle_type, condition_type, min_value, max_value, weather_phenomenon, extra_fee 
                                FROM weather_fees
                                WHERE vehicle_type = :vehicleType
                                """));
        assertEquals(vehicleType, params.get("vehicleType"));

        try {
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString("condition_type")).thenReturn(ConditionType.AIR_TEMPERATURE.name());
            when(rs.getDouble("min_value")).thenReturn(10.0);
            when(rs.getDouble("max_value")).thenReturn(20.0);
            when(rs.getString("weather_phenomenon")).thenReturn("Sunny");
            when(rs.getString("vehicle_type")).thenReturn(vehicleType);
            when(rs.getDouble("extra_fee")).thenReturn(5.0);
            WeatherFee mappedFee = rowMapper.mapRow(rs,0);
            assertEquals(weatherFee, mappedFee);

        } catch (SQLException e) {
            fail("SQLException during row mapping");
        }
    }

    @Test
    public void getWeatherFeeByVehicleAndCondition_shouldReturnEmptyList_whenNoWeatherFeesFound() {
        // Arrange
        String vehicleType = "Truck";
        when(jdbcTemplate.query(anyString(), anyMap(), any(RowMapper.class)))
                .thenReturn(List.of());

        // Act
        List<WeatherFee> weatherFeeList = weatherFeeRepository.getWeatherFeeByVehicleAndCondition(vehicleType);

        // Assert
        assertTrue(weatherFeeList.isEmpty());
        verify(jdbcTemplate).query(anyString(), anyMap(), any(RowMapper.class));
    }
}