package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.model.VehicleType;
import fujitsu.delivery.application.repository.impl.RegionalFeeRepositoryImpl;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegionalFeeRepositoryImplTest {

    @Mock
    private NamedParameterJdbcTemplate jdbcTemplate;

    @InjectMocks
    private RegionalFeeRepositoryImpl regionalFeeRepository;

    @Captor
    private ArgumentCaptor<String> sqlCaptor;

    @Captor
    private ArgumentCaptor<Map<String, Object>> paramsCaptor;

    @Captor
    private ArgumentCaptor<RowMapper<RegionalFee>> rowMapperCaptor;

    @Test
    public void getFeesByCityAndVehicle_shouldReturnRegionalFee_whenRecordExists() {
        // Arrange
        String city = "Test City";
        VehicleType vehicleType = VehicleType.CAR;
        RegionalFee expectedFee = new RegionalFee(vehicleType, city, 10.0);

        when(jdbcTemplate.query(anyString(), anyMap(), any(RowMapper.class)))
                .thenReturn(List.of(expectedFee));

        // Act
        Optional<RegionalFee> actualFee = regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType);

        // Assert
        assertTrue(actualFee.isPresent());
        assertEquals(expectedFee, actualFee.get());
        verify(jdbcTemplate).query(sqlCaptor.capture(), paramsCaptor.capture(), rowMapperCaptor.capture());
        String sql = sqlCaptor.getValue();
        Map<String, Object> params = paramsCaptor.getValue();
        RowMapper<RegionalFee> rowMapper = rowMapperCaptor.getValue();

        assertTrue(sql.contains("""
                SELECT city, vehicle_type, base_fee
                FROM regional_fees
                WHERE city LIKE CONCAT('%', :city, '%') AND vehicle_type = :vehicleType
                """));

        assertEquals(city, params.get("city"));
        assertEquals(vehicleType, params.get("vehicleType"));

        try {
            ResultSet rs = mock(ResultSet.class);
            when(rs.getString("city")).thenReturn(city);
            when(rs.getString("vehicle_type")).thenReturn(vehicleType.name());
            when(rs.getDouble("base_fee")).thenReturn(10.0);
            RegionalFee mappedFee = rowMapper.mapRow(rs, 0);
            assertEquals(expectedFee, mappedFee);

        } catch (SQLException e) {
            fail("SQLException during row mapping");
        }
    }

    @Test
    public void getFeesByCityAndVehicle_shouldReturnEmptyOptional_whenRecordDoesNotExist() {
        // Arrange
        String city = "Nonexistent City";
        VehicleType vehicleType = VehicleType.CAR;

        when(jdbcTemplate.query(anyString(), anyMap(), any(RowMapper.class)))
                .thenReturn(List.of());

        // Act
        Optional<RegionalFee> actualFee = regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType);

        // Assert
        assertFalse(actualFee.isPresent());
        verify(jdbcTemplate).query(anyString(), anyMap(), any(RowMapper.class));
    }

    @Test
    public void updateFees_shouldExecuteUpdateQuery() {
        // Arrange
        RegionalFee regionalFee = new RegionalFee(VehicleType.BIKE, "Update City", 15.0);
        when(jdbcTemplate.update(anyString(), anyMap())).thenReturn(1);

        // Act
        regionalFeeRepository.updateFees(regionalFee);

        // Assert
        verify(jdbcTemplate).update(sqlCaptor.capture(), paramsCaptor.capture());
        String sql = sqlCaptor.getValue();
        Map<String, Object> params = paramsCaptor.getValue();

        assertTrue(sql.contains("""
                UPDATE regional_fees
                SET base_fee = :baseFee
                WHERE city = :city AND vehicle_type = :vehicleType
                """));

        assertEquals(regionalFee.city(), params.get("city"));
        assertEquals(regionalFee.vehicleType(), params.get("vehicleType"));
        assertEquals(regionalFee.baseFee(), params.get("baseFee"));
    }
}