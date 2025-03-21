package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.RegionalFee;

import java.util.Optional;

public interface RegionalFeeRepository {

    Optional<RegionalFee> getFeesByCityAndVehicle(String city, String vehicleType);

    void updateFees(RegionalFee regionalFee);

}
