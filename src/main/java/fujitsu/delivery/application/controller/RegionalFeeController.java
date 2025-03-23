package fujitsu.delivery.application.controller;

import fujitsu.delivery.application.exception.ErrorCode;
import fujitsu.delivery.application.exception.RequestException;
import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.model.VehicleType;
import fujitsu.delivery.application.service.RegionalFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/regional-fee", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegionalFeeController {

    private final RegionalFeeService regionalFeeService;

    @GetMapping("/{city}/{vehicleType}")
    public ResponseEntity<RegionalFee> getFees(@PathVariable String city, @PathVariable String vehicleType) {
        VehicleType vehicle = VehicleType.fromString(vehicleType.toUpperCase());
        return ResponseEntity.ok( regionalFeeService.getFeesByCityAndVehicle(city, vehicle));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateBaseFee(@RequestBody RegionalFee regionalFee) {
        try{
            regionalFeeService.updateRegionalFees(regionalFee);
            return ResponseEntity.ok().build();
        } catch (Exception e){
            throw new RequestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
