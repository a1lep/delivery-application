package fujitsu.delivery.application.service;

import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.WeatherFee;
import fujitsu.delivery.application.repository.WeatherFeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherFeeServiceTest {

    @Mock
    private WeatherFeeRepository weatherFeeRepository;

    @InjectMocks
    private WeatherFeeService weatherFeeService;

    @Test
    public void saveOrUpdateWeatherFee_shouldCallRepositorySaveOrUpdate() {
        // Arrange
        WeatherFee weatherFee = new WeatherFee(ConditionType.AIR_TEMPERATURE, 10.0, 20.0, "Sunny", "Car", 5.0);

        // Act
        weatherFeeService.saveOrUpdateWeatherFee(weatherFee);

        // Assert
        verify(weatherFeeRepository).saveOrUpdateWeatherFee(weatherFee);
    }

    @Test
    public void getWeatherFeeByVehicleAndCondition_shouldReturnWeatherFeesFromRepository() {
        // Arrange
        String vehicleType = "Car";
        WeatherFee weatherFee = new WeatherFee(ConditionType.AIR_TEMPERATURE, 10.0, 20.0, "Sunny", vehicleType, 5.0);
        List<WeatherFee> expectedWeatherFees = List.of(weatherFee);
        when(weatherFeeRepository.getWeatherFeeByVehicleAndCondition(vehicleType)).thenReturn(expectedWeatherFees);

        // Act
        List<WeatherFee> actualWeatherFees = weatherFeeService.getWeatherFeeByVehicleAndCondition(vehicleType);

        // Assert
        assertEquals(expectedWeatherFees, actualWeatherFees);
        verify(weatherFeeRepository).getWeatherFeeByVehicleAndCondition(vehicleType);
    }

    @Test
    public void getWeatherFeeByVehicleAndCondition_shouldReturnEmptyList_whenRepositoryReturnsEmptyList() {
        // Arrange
        String vehicleType = "Truck";
        List<WeatherFee> expectedWeatherFees = List.of();
        when(weatherFeeRepository.getWeatherFeeByVehicleAndCondition(vehicleType)).thenReturn(expectedWeatherFees);

        // Act
        List<WeatherFee> actualWeatherFees = weatherFeeService.getWeatherFeeByVehicleAndCondition(vehicleType);

        // Assert
        assertEquals(expectedWeatherFees, actualWeatherFees);
        verify(weatherFeeRepository).getWeatherFeeByVehicleAndCondition(vehicleType);
    }
}
