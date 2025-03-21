package fujitsu.delivery.application.controller;

import fujitsu.delivery.application.controller.dto.WeatherFeeDto;
import fujitsu.delivery.application.exception.ErrorResponse;
import fujitsu.delivery.application.exception.RequestException;
import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.service.WeatherFeesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weatherFees")
@RequiredArgsConstructor
public class WeatherFeeController {

    private final WeatherFeesService weatherFeesService;
//todo check if we can change the ? to the correct data type
    @PostMapping("/add")
    public ResponseEntity<?> saveOrUpdateWeatherFee(@RequestBody WeatherFeeDto request) {
        try {
            weatherFeesService.saveOrUpdateWeatherFee(request);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getErrorCode().toString(), e.getMessage()));
        }
    }

    @GetMapping("/{vehicleType}/{conditionType}")
    public ResponseEntity<?> getWeatherFee(@PathVariable String vehicleType, @PathVariable String conditionType) {
        try {
            final ConditionType conditionTypeEnum = ConditionType.valueOf(conditionType.toUpperCase());
            final WeatherFeeDto response = weatherFeesService.getWeatherFeeByVehicleAndCondition(vehicleType, conditionTypeEnum);
            return ResponseEntity.ok(response);
        } catch (RequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(e.getErrorCode().toString(), e.getMessage()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("INVALID_CONDITION", "Invalid condition type provided"));
        }
    }
}
