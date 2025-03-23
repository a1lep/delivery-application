package fujitsu.delivery.application.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WEATHER_NOT_SUPPORTED_FOR_VEHICLE("Weather not supported for the selected vehicle type: %s"),
    INVALID_CONDITION("Invalid weather condition provided: %s"),
    VALUE_NOT_FOUND("Value not found in our internal database."),
    INTERNAL_SERVER_ERROR("An internal server error occurred."),
    VEHICLE_NOT_SUPPORTED("Vehicle is not supported for the selected vehicle type: %s"),
    CITY_NOT_SUPPORTED("City not supported: %s");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        return String.format(this.message, args);
    }
}