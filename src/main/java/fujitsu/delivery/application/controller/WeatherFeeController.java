package fujitsu.delivery.application.controller;

import fujitsu.delivery.application.model.VehicleType;
import fujitsu.delivery.application.model.WeatherFee;
import fujitsu.delivery.application.service.WeatherFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/weather-fee", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class WeatherFeeController {

    private final WeatherFeeService weatherFeeService;

    @PostMapping("/add")
    public ResponseEntity<Void> saveOrUpdateWeatherFee(@RequestBody WeatherFee request) {
        weatherFeeService.saveOrUpdateWeatherFee(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{vehicleType}")
    public ResponseEntity<List<WeatherFee>> getWeatherFee(@PathVariable String vehicleType) {
        VehicleType vehicle = VehicleType.fromString(vehicleType.toUpperCase());
        final List<WeatherFee> response = weatherFeeService.getWeatherFeeByVehicleAndCondition(vehicle);
        return ResponseEntity.ok(response);
    }
}