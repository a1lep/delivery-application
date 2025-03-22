package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.WeatherFee;

import java.util.List;

public interface WeatherFeeRepository {

    void saveOrUpdateWeatherFee(WeatherFee weatherFee);

    List<WeatherFee> getWeatherFeeByVehicleAndCondition(String vehicleType);
}
