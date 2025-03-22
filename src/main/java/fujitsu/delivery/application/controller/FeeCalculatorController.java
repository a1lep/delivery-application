package fujitsu.delivery.application.controller;

import fujitsu.delivery.application.service.CalculatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/fee-calculator", produces = MediaType.APPLICATION_JSON_VALUE)
public class FeeCalculatorController {

    private final CalculatorService calculatorService;

    @GetMapping("/{city}/{vehicleType}")
    public ResponseEntity<Double> calculateFee(@PathVariable String city, @PathVariable String vehicleType) {
        final Double calculatedFee = calculatorService.calculateFee(city, vehicleType);

        return ResponseEntity.ok(calculatedFee);
    }
}
