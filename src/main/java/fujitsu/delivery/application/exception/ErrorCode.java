package fujitsu.delivery.application.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    WEATHER_NOT_SUPPORTED_FOR_VEHICLE("Weather not supported for the selected vehicle type."),
    INVALID_CONDITION("Invalid weather condition provided."),
    VALUE_NOT_FOUND("Value not found in our internal database."),
    INTERNAL_SERVER_ERROR("An internal server error occurred.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }

}