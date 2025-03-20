package fujitsu.delivery.application.controller;

import fujitsu.delivery.application.model.RegionalFees;
import fujitsu.delivery.application.service.RegionalFeesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/regionalFees")
public class RegionalFeesController {

    private final RegionalFeesService regionalFeesService;

    @GetMapping("/{city}/{vehicleType}")
    public Optional<RegionalFees> getFees(@PathVariable String city, @PathVariable String vehicleType) {
        return regionalFeesService.getFeesByCityAndVehicle(city, vehicleType);
    }

    @PutMapping("/update")
    public void updateBaseFee(@RequestBody RegionalFees regionalFees) {
        regionalFeesService.updateRegionalFees(regionalFees);
    }

}
