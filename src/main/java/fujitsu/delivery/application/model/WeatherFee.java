package fujitsu.delivery.application.model;

public record WeatherFee(
        ConditionType conditionType,
        Double minValue,  // For AIR_TEMPERATURE and WIND_SPEED
        Double maxValue,  // For AIR_TEMPERATURE and WIND_SPEED
        String weatherPhenomenon,  // For WEATHER_PHENOMENON like "snow", "rain"
        String vehicleType,
        Double extraFee
) {
}