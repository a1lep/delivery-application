package fujitsu.delivery.application.repository;

import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.WeatherFees;

import java.util.Optional;

public interface WeatherFeesRepository {

    void saveOrUpdateWeatherFee(WeatherFees weatherFees);

    Optional<WeatherFees> getWeatherFeeByVehicleAndCondition(String vehicleType, ConditionType conditionType);
}
