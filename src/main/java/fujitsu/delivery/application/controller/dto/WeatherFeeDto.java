package fujitsu.delivery.application.controller.dto;

import fujitsu.delivery.application.model.ConditionType;

public record WeatherFeeDto(
        String vehicleType,
        ConditionType conditionType,
        Double minValue,  // For AIR_TEMPERATURE and WIND_SPEED
        Double maxValue,  // For AIR_TEMPERATURE and WIND_SPEED
        String phenomenon,  // For WEATHER_PHENOMENON like "snow", "rain"
        Double extraFee
) {
}
