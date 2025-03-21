package fujitsu.delivery.application.controller;

import fujitsu.delivery.application.exception.ErrorCode;
import fujitsu.delivery.application.exception.RequestException;
import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.service.RegionalFeesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regional-fee")
public class RegionalFeeController {

    private final RegionalFeesService regionalFeesService;

    @GetMapping("/{city}/{vehicleType}")
    public ResponseEntity<RegionalFee> getFees(@PathVariable String city, @PathVariable String vehicleType) {
        return ResponseEntity.ok( regionalFeesService.getFeesByCityAndVehicle(city, vehicleType));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateBaseFee(@RequestBody RegionalFee regionalFee) {
        try{
            regionalFeesService.updateRegionalFees(regionalFee);
            return ResponseEntity.noContent().build();
        } catch (Exception e){
            throw new RequestException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }
}
