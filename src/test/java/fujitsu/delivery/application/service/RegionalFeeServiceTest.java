package fujitsu.delivery.application.service;

import fujitsu.delivery.application.exception.ErrorCode;
import fujitsu.delivery.application.exception.RequestException;
import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.model.VehicleType;
import fujitsu.delivery.application.repository.RegionalFeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RegionalFeeServiceTest {

    @Mock
    private RegionalFeeRepository regionalFeeRepository;

    @InjectMocks
    private RegionalFeeService regionalFeeService;

    @Test
    public void getFeesByCityAndVehicle_shouldReturnRegionalFee_whenRecordExists() {
        // Arrange
        String city = "Test City";
        VehicleType vehicleType = VehicleType.CAR;
        RegionalFee expectedFee = new RegionalFee(vehicleType, city, 10.0);
        when(regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType)).thenReturn(Optional.of(expectedFee));

        // Act
        RegionalFee actualFee = regionalFeeService.getFeesByCityAndVehicle(city, vehicleType);

        // Assert
        assertEquals(expectedFee, actualFee);
        verify(regionalFeeRepository).getFeesByCityAndVehicle(city, vehicleType);
    }

    @Test
    public void getFeesByCityAndVehicle_shouldThrowRequestException_whenRecordDoesNotExist() {
        // Arrange
        String city = "Nonexistent City";
        VehicleType vehicleType = VehicleType.SCOOTER;
        when(regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType)).thenReturn(Optional.empty());

        // Act & Assert
        RequestException exception = assertThrows(RequestException.class, () -> regionalFeeService.getFeesByCityAndVehicle(city, vehicleType));
        assertEquals(ErrorCode.CITY_NOT_SUPPORTED, exception.getErrorCode());
        assertEquals(String.format(ErrorCode.CITY_NOT_SUPPORTED.formatMessage(city, vehicleType)), exception.getMessage());
        verify(regionalFeeRepository).getFeesByCityAndVehicle(city, vehicleType);
    }

    @Test
    public void updateRegionalFees_shouldCallRepositoryUpdateFees() {
        // Arrange
        RegionalFee updatedFee = new RegionalFee( VehicleType.BIKE, "Update City",15.0);

        // Act
        regionalFeeService.updateRegionalFees(updatedFee);

        // Assert
        verify(regionalFeeRepository).updateFees(updatedFee);
    }

}
