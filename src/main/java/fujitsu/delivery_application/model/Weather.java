package fujitsu.delivery_application.model;

public record Weather(
       String stationName,
       Double wmoCode,
       Double airTemperature,
       Double windSpeed,
       String phenomenon,
       Integer timestamp

) {
}
