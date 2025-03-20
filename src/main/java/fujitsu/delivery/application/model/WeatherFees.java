package fujitsu.delivery.application.model;

public record WeatherFees(
        String conditionType,
        String conditionValue,
        String vehicleType,
        Double extraFee
) {
}
