package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.RegionalFees;

import java.util.Optional;

public interface RegionalFeesRepository{

    Optional<RegionalFees> getFeesByCityAndVehicle(String city, String vehicleType);

    void updateFees(RegionalFees regionalFees);

}
