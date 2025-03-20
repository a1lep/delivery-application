package fujitsu.delivery.application.service;

import fujitsu.delivery.application.model.RegionalFees;
import fujitsu.delivery.application.repository.RegionalFeesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RegionalFeesService {

    private final RegionalFeesRepository regionalFeesRepository;

    public Optional<RegionalFees> getFeesByCityAndVehicle(String city, String vehicleType) {
        return regionalFeesRepository.getFeesByCityAndVehicle(city, vehicleType);
    }

    public void updateRegionalFees(RegionalFees updatedFee) {
        regionalFeesRepository.updateFees(updatedFee);
    }
}
