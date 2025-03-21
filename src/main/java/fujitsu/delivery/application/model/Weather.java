package fujitsu.delivery.application.model;

import java.sql.Timestamp;

public record Weather(
       String stationName,
       Double wmoCode,
       Double airTemperature,
       Double windSpeed,
       String phenomenon,
       Timestamp createTimestamp
) {
}
