package fujitsu.delivery.application.model;

import fujitsu.delivery.application.exception.ErrorCode;
import fujitsu.delivery.application.exception.RequestException;

public enum VehicleType {
    CAR,
    SCOOTER,
    BIKE;

    public static VehicleType fromString(String vehicleTypeString) {
        try {
            return VehicleType.valueOf(vehicleTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RequestException(ErrorCode.VEHICLE_NOT_SUPPORTED, vehicleTypeString);
        }
    }
}
