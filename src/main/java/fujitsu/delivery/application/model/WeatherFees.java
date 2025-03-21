package fujitsu.delivery.application.model;

public record WeatherFees(
        ConditionType conditionType,
        String conditionValue,
        String vehicleType,
        Double extraFee
) {
}
