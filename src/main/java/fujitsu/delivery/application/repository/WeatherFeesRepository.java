package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.WeatherFees;

public interface WeatherFeesRepository {

    void updateFees(WeatherFees weatherFees);

    WeatherFees getWeatherFeesByVehicle(String vehicle);
}
