package fujitsu.delivery.application.service;


import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.WeatherFee;
import fujitsu.delivery.application.repository.WeatherFeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherFeeService {

    private final WeatherFeeRepository weatherFeeRepository;

    public void saveOrUpdateWeatherFee(WeatherFee weatherFee) {
        weatherFeeRepository.saveOrUpdateWeatherFee(weatherFee);
    }

    public List<WeatherFee> getWeatherFeeByVehicleAndCondition(String vehicleType, ConditionType conditionType) {
        return weatherFeeRepository
                .getWeatherFeeByVehicleAndCondition(vehicleType, conditionType);
    }

}