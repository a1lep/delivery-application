package fujitsu.delivery.application.service;

import fujitsu.delivery.application.exception.ErrorCode;
import fujitsu.delivery.application.exception.RequestException;
import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.model.VehicleType;
import fujitsu.delivery.application.repository.RegionalFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegionalFeeService {

    private final RegionalFeeRepository regionalFeeRepository;

    public RegionalFee getFeesByCityAndVehicle(String city, VehicleType vehicleType) {
        return regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType).
                orElseThrow(() -> new RequestException(ErrorCode.CITY_NOT_SUPPORTED, city));
    }

    public void updateRegionalFees(RegionalFee updatedFee) {
        regionalFeeRepository.updateFees(updatedFee);
    }
}
