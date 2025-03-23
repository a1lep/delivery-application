package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.model.VehicleType;

import java.util.Optional;

public interface RegionalFeeRepository {

    Optional<RegionalFee> getFeesByCityAndVehicle(String city, VehicleType vehicleType);

    void updateFees(RegionalFee regionalFee);

}
