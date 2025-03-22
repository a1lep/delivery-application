package fujitsu.delivery.application.service;

import fujitsu.delivery.application.exception.ErrorCode;
import fujitsu.delivery.application.exception.RequestException;
import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.model.Weather;
import fujitsu.delivery.application.model.WeatherFee;
import fujitsu.delivery.application.repository.RegionalFeeRepository;
import fujitsu.delivery.application.repository.WeatherFeeRepository;
import fujitsu.delivery.application.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorService {
    private final WeatherRepository weatherRepository;
    private final WeatherFeeRepository weatherFeeRepository;
    private final RegionalFeeRepository regionalFeeRepository;

    public Double calculateFee(final String city, final String vehicleType) {

        Weather weather = weatherRepository.getLatestByStationName(city).orElseThrow(() -> new RequestException(ErrorCode.CITY_OR_VEHICLE_NOT_SUPPORTED, city, vehicleType));

        RegionalFee baseFee = regionalFeeRepository.getFeesByCityAndVehicle(city, vehicleType).orElseThrow(() -> new RequestException(ErrorCode.CITY_OR_VEHICLE_NOT_SUPPORTED, city, vehicleType));

        List<WeatherFee> allWeatherFees = weatherFeeRepository.getWeatherFeeByVehicleAndCondition(vehicleType);

        double extraFee = 0.0;

        for (WeatherFee weatherFee : allWeatherFees) {
            switch (weatherFee.conditionType()) {
                case AIR_TEMPERATURE -> {
                    if ((weatherFee.minValue() <= weather.airTemperature()) && (weather.airTemperature()<= weatherFee.maxValue())) {
                        extraFee += weatherFee.extraFee();
                    }
                }
                case WIND_SPEED -> {
                    if (weatherFee.minValue() <= weather.windSpeed() && weather.windSpeed()<= weatherFee.maxValue()) {
                        if (weatherFee.extraFee() == -1) {
                            throw new RequestException(ErrorCode.WEATHER_NOT_SUPPORTED_FOR_VEHICLE);
                        }
                        extraFee += weatherFee.extraFee();
                    }
                }
                case WEATHER_PHENOMENON -> {
                    if (weatherFee.weatherPhenomenon() != null &&
                            Arrays.asList(weatherFee.weatherPhenomenon().split(",")).contains(weather.phenomenon().toLowerCase())) {
                        if (weatherFee.extraFee() == -1) {
                            throw new RequestException(ErrorCode.WEATHER_NOT_SUPPORTED_FOR_VEHICLE);
                        }
                        extraFee += weatherFee.extraFee();
                    }
                }
            }
        }
        return baseFee.baseFee() + extraFee;
    }

}
