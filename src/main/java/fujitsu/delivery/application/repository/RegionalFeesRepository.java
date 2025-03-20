package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.RegionalFees;

public interface RegionalFeesRepository {

    RegionalFees getFeesByCityAndVehicle(String vehicle);

    void updateFees(RegionalFees regionalFees);

}
