package fujitsu.delivery.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fujitsu.delivery.application.model.RegionalFee;
import fujitsu.delivery.application.service.RegionalFeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class RegionalFeeControllerTest {

    @Mock
    private RegionalFeeService regionalFeeService;

    @InjectMocks
    private RegionalFeeController regionalFeeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void getFees_shouldReturnRegionalFee() throws Exception {
        // Arrange
        String city = "Test City";
        String vehicleType = "Car";
        RegionalFee regionalFee = new RegionalFee(city, vehicleType, 10.0);
        when(regionalFeeService.getFeesByCityAndVehicle(city, vehicleType)).thenReturn(regionalFee);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(regionalFeeController).build();

        // Act & Assert
        mockMvc.perform(get("/api/regional-fee/" + city + "/" + vehicleType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(regionalFee)));

        verify(regionalFeeService).getFeesByCityAndVehicle(city, vehicleType);
    }

    @Test
    public void updateBaseFee_shouldReturnNoContent() throws Exception {
        // Arrange
        RegionalFee regionalFee = new RegionalFee("Update City", "Bike", 15.0);
        String regionalFeeJson = objectMapper.writeValueAsString(regionalFee);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(regionalFeeController).build();

        // Act & Assert
        mockMvc.perform(put("/api/regional-fee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(regionalFeeJson))
                .andExpect(status().isNoContent());

        verify(regionalFeeService).updateRegionalFees(regionalFee);
    }
}