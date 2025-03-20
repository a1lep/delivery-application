package fujitsu.delivery.application.model;

public record RegionalFees(
        String vehicleType,
        String city,
        Double baseFee
) {
}
