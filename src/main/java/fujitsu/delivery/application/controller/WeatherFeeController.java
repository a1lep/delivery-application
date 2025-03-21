package fujitsu.delivery.application.controller;

import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.WeatherFee;
import fujitsu.delivery.application.service.WeatherFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/weather-fee")
@RequiredArgsConstructor
public class WeatherFeeController {

    private final WeatherFeeService weatherFeeService;

    @PostMapping("/add")
    public ResponseEntity<Void> saveOrUpdateWeatherFee(@RequestBody WeatherFee request) {
        weatherFeeService.saveOrUpdateWeatherFee(request);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{vehicleType}/{conditionType}")
    public ResponseEntity<List<WeatherFee>> getWeatherFee(@PathVariable String vehicleType, @PathVariable String conditionType) {
        final ConditionType conditionTypeEnum = ConditionType.valueOf(conditionType.toUpperCase());
        final List<WeatherFee> response = weatherFeeService.getWeatherFeeByVehicleAndCondition(vehicleType, conditionTypeEnum);
        return ResponseEntity.ok(response);
    }
}