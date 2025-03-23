package fujitsu.delivery.application.service;

import fujitsu.delivery.application.exception.RequestException;
import fujitsu.delivery.application.model.*;
import fujitsu.delivery.application.repository.RegionalFeeRepository;
import fujitsu.delivery.application.repository.WeatherFeeRepository;
import fujitsu.delivery.application.repository.WeatherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {

    @Mock
    private RegionalFeeRepository regionalFeeRepository;

    @Mock
    private WeatherFeeRepository weatherFeeRepository;

    @Mock
    private WeatherRepository weatherRepository;

    @InjectMocks
    private CalculatorService calculatorService;

    @Test
    public void calculateFee_shouldReturnCorrectFee_whenWeatherConditionsApply() {
        // Arrange
        String city = "Test City";
        VehicleType vehicleType = VehicleType.CAR;
        Weather weather = new Weather(city, 100.0, 25.0, 10.0, "Sunny", Timestamp.from(Instant.now()));
        RegionalFee baseFee = new RegionalFee(vehicleType, city, 10.0);
        WeatherFee tempFee = new WeatherFee(ConditionType.AIR_TEMPERATURE, 20.0, 30.0, null, vehicleType, 5.0);
        WeatherFee windFee = new WeatherFee(ConditionType.WIND_SPEED, 5.0, 15.0, null, vehicleType, 3.0);
        WeatherFee phenomFee = new WeatherFee(ConditionType.WEATHER_PHENOMENON, 0.0, 0.0, "sunny,rainy", vehicleType, 2.0);

        when(weatherRepository.getLatestByStationName(city)).thenReturn(Optional.of(weather));
        when(regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType)).thenReturn(Optional.of(baseFee));
        when(weatherFeeRepository.getWeatherFeeByVehicleAndCondition(vehicleType)).thenReturn(List.of(tempFee, windFee, phenomFee));

        // Act
        Double calculatedFee = calculatorService.calculateFee(city, vehicleType);

        // Assert
        assertEquals(20.0, calculatedFee);
        verify(weatherRepository).getLatestByStationName(city);
        verify(regionalFeeRepository).getFeesByCityAndVehicle(city, vehicleType);
        verify(weatherFeeRepository).getWeatherFeeByVehicleAndCondition(vehicleType);
    }

    @Test
    public void calculateFee_shouldThrowRequestException_whenWeatherNotSupported() {
        // Arrange
        String city = "Test City";
        VehicleType vehicleType = VehicleType.CAR;
        Weather weather = new Weather(city, 100.0, 25.0, 10.0, "Sunny", Timestamp.from(Instant.now()));
        RegionalFee baseFee = new RegionalFee(vehicleType, city, 10.0);
        WeatherFee windFee = new WeatherFee(ConditionType.WIND_SPEED, 5.0, 15.0, null, vehicleType, -1.0);

        when(weatherRepository.getLatestByStationName(city)).thenReturn(Optional.of(weather));
        when(regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType)).thenReturn(Optional.of(baseFee));
        when(weatherFeeRepository.getWeatherFeeByVehicleAndCondition(vehicleType)).thenReturn(List.of(windFee));

        // Act & Assert
        assertThrows(RequestException.class, () -> calculatorService.calculateFee(city, vehicleType));
        verify(weatherRepository).getLatestByStationName(city);
        verify(regionalFeeRepository).getFeesByCityAndVehicle(city, vehicleType);
        verify(weatherFeeRepository).getWeatherFeeByVehicleAndCondition(vehicleType);
    }

    @Test
    public void calculateFee_shouldThrowRequestException_whenRegionalFeeNotFound() {
        // Arrange
        String city = "Test City";
        VehicleType vehicleType = VehicleType.CAR;
        Weather weather = new Weather(city, 100.0, 25.0, 10.0, "Sunny", Timestamp.from(Instant.now()));

        when(weatherRepository.getLatestByStationName(city)).thenReturn(Optional.of(weather));
        when(regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RequestException.class, () -> calculatorService.calculateFee(city, vehicleType));
        verify(weatherRepository).getLatestByStationName(city);
        verify(regionalFeeRepository).getFeesByCityAndVehicle(city, vehicleType);
    }

    @Test
    public void calculateFee_shouldThrowRequestException_whenWeatherNotFound() {
        // Arrange
        String city = "Test City";
        VehicleType vehicleType = VehicleType.CAR;

        when(weatherRepository.getLatestByStationName(city)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RequestException.class, () -> calculatorService.calculateFee(city, vehicleType));
        verify(weatherRepository).getLatestByStationName(city);
    }

}
