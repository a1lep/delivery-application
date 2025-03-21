package fujitsu.delivery.application.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WEATHER_NOT_SUPPORTED_FOR_VEHICLE("Weather not supported for the selected vehicle type: %s"),
    INVALID_CONDITION("Invalid weather condition provided: %s"),
    VALUE_NOT_FOUND("Value not found in our internal database."),
    INTERNAL_SERVER_ERROR("An internal server error occurred."),
    CITY_OR_VEHICLE_NOT_SUPPORTED("City or vehicle type not supported: %s, %s");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

    public String formatMessage(Object... args) {
        return String.format(this.message, args);
    }
}