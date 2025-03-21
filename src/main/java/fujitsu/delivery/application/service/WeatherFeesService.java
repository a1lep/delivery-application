package fujitsu.delivery.application.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fujitsu.delivery.application.controller.dto.WeatherFeeDto;
import fujitsu.delivery.application.exception.ErrorCode;
import fujitsu.delivery.application.exception.RequestException;
import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.WeatherFees;
import fujitsu.delivery.application.repository.WeatherFeesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class WeatherFeesService {

    private final WeatherFeesRepository weatherFeesRepository;
    private final ObjectMapper objectMapper;

    public void saveOrUpdateWeatherFee(WeatherFeeDto weatherFeeDto) {
        WeatherFees weatherFees = mapToWeatherFeesEntity(weatherFeeDto);
        weatherFeesRepository.saveOrUpdateWeatherFee(weatherFees);
    }

    public WeatherFeeDto getWeatherFeeByVehicleAndCondition(String vehicleType, ConditionType conditionType) {
        final WeatherFees weatherFees = weatherFeesRepository
                .getWeatherFeeByVehicleAndCondition(vehicleType, conditionType)
                .orElseThrow(() -> new RequestException(ErrorCode.VALUE_NOT_FOUND));

        return mapToWeatherFeeDto(weatherFees);
    }

    private WeatherFees mapToWeatherFeesEntity(WeatherFeeDto weatherFeeDto) {
        String conditionValueJson = "";

        if (weatherFeeDto.conditionType() == ConditionType.PHENOMENON) {
            conditionValueJson = serializePhenomenon(weatherFeeDto.phenomenon());
        } else if (weatherFeeDto.conditionType() == ConditionType.AIR_TEMPERATURE || weatherFeeDto.conditionType() == ConditionType.WIND_SPEED) {
            conditionValueJson = serializeMinMax(weatherFeeDto.minValue(), weatherFeeDto.maxValue());
        }

        return new WeatherFees(
                weatherFeeDto.conditionType(),
                conditionValueJson,
                weatherFeeDto.vehicleType(),
                weatherFeeDto.extraFee()
        );
    }

    private WeatherFeeDto mapToWeatherFeeDto(WeatherFees weatherFees) {
        String conditionValue = weatherFees.conditionValue();

        if (weatherFees.conditionType() == ConditionType.PHENOMENON) {
            return new WeatherFeeDto(
                    weatherFees.vehicleType(),
                    weatherFees.conditionType(),
                    null,
                    null,
                    deserializePhenomenon(conditionValue),
                    weatherFees.extraFee()
            );
        } else {
            MinMax minMax = deserializeMinMax(conditionValue);
            return new WeatherFeeDto(
                    weatherFees.vehicleType(),
                    weatherFees.conditionType(),
                    minMax.min(),
                    minMax.max(),
                    null,
                    weatherFees.extraFee()
            );
        }
    }

    private String serializeMinMax(Double minValue, Double maxValue) {
        Map<String, Double> minMaxMap = new HashMap<>();
        minMaxMap.put("min", minValue);
        minMaxMap.put("max", maxValue);
        try {
            return objectMapper.writeValueAsString(minMaxMap);
        } catch (JsonProcessingException e) {
            log.error("Error during serialization: ", e);
            throw new RequestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private MinMax deserializeMinMax(String conditionValue) {
        try {
            TypeReference<Map<String, Double>> typeReference = new TypeReference<>() {};
            Map<String, Double> minMaxMap = objectMapper.readValue(conditionValue, typeReference);
            return new MinMax(minMaxMap.get("min"), minMaxMap.get("max"));
        } catch (JsonProcessingException e) {
            log.error("Error during deserialization: ", e);
            throw new RequestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String serializePhenomenon(String phenomenon) {
        return phenomenon;
    }

    private String deserializePhenomenon(String conditionValue) {
        return conditionValue;
    }

    private record MinMax(Double min, Double max) {
    }
}