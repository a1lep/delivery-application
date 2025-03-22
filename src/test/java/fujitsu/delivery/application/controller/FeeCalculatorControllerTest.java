package fujitsu.delivery.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fujitsu.delivery.application.service.CalculatorService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class FeeCalculatorControllerTest {

    @Mock
    private CalculatorService calculatorService;

    @InjectMocks
    private FeeCalculatorController feeCalculatorController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void calculateFee_shouldReturnCalculatedFee() throws Exception {
        // Arrange
        String city = "Test City";
        String vehicleType = "Car";
        Double calculatedFee = 25.0;
        when(calculatorService.calculateFee(city, vehicleType)).thenReturn(calculatedFee);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(feeCalculatorController).build();

        // Act & Assert
        mockMvc.perform(get("/api/fee-calculator/" + city + "/" + vehicleType)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                        .andExpect(content().string(objectMapper.writeValueAsString(calculatedFee)));

        verify(calculatorService).calculateFee(city, vehicleType);
    }

}
