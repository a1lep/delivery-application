package fujitsu.delivery.application.model;

public record RegionalFee(
        String vehicleType,
        String city,
        Double baseFee
) {
}
