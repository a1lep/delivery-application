package fujitsu.delivery.application.model;

public record RegionalFee(
        VehicleType vehicleType,
        String city,
        Double baseFee
) {
}
