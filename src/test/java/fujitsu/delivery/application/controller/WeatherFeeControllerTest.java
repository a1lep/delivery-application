package fujitsu.delivery.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fujitsu.delivery.application.model.ConditionType;
import fujitsu.delivery.application.model.VehicleType;
import fujitsu.delivery.application.model.WeatherFee;
import fujitsu.delivery.application.service.WeatherFeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class WeatherFeeControllerTest {

    @Mock
    private WeatherFeeService weatherFeeService;

    @InjectMocks
    private WeatherFeeController weatherFeeController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void saveOrUpdateWeatherFee_shouldReturnNoContent() throws Exception {
        // Arrange
        WeatherFee weatherFee = new WeatherFee(ConditionType.AIR_TEMPERATURE, 10.0, 20.0, "Sunny", VehicleType.CAR, 5.0);
        String weatherFeeJson = objectMapper.writeValueAsString(weatherFee);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(weatherFeeController).build();

        // Act & Assert
        mockMvc.perform(post("/weather-fee/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(weatherFeeJson))
                .andExpect(status().isOk());

        verify(weatherFeeService).saveOrUpdateWeatherFee(weatherFee);
    }

    @Test
    public void getWeatherFee_shouldReturnWeatherFeeList() throws Exception {
        // Arrange
        VehicleType vehicleType = VehicleType.CAR;
        WeatherFee weatherFee = new WeatherFee(ConditionType.AIR_TEMPERATURE, 10.0, 20.0, "Sunny", vehicleType, 5.0);
        List<WeatherFee> expectedWeatherFees = List.of(weatherFee);
        when(weatherFeeService.getWeatherFeeByVehicleAndCondition(vehicleType)).thenReturn(expectedWeatherFees);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(weatherFeeController).build();

        // Act & Assert
        String json = mockMvc.perform(get("/weather-fee/" + vehicleType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<WeatherFee> result = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, WeatherFee.class));

        assertEquals(expectedWeatherFees, result);
        verify(weatherFeeService).getWeatherFeeByVehicleAndCondition(vehicleType);
    }

    @Test
    public void getWeatherFee_shouldReturnEmptyList() throws Exception {
        // Arrange
        VehicleType vehicleType = VehicleType.CAR;
        List<WeatherFee> expectedWeatherFees = List.of();
        when(weatherFeeService.getWeatherFeeByVehicleAndCondition(vehicleType)).thenReturn(expectedWeatherFees);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(weatherFeeController).build();

        // Act & Assert
        String json = mockMvc.perform(get("/weather-fee/" + vehicleType)
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andReturn().getResponse().getContentAsString();

        List<WeatherFee> result = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, WeatherFee.class));

        assertEquals(expectedWeatherFees, result);
        verify(weatherFeeService).getWeatherFeeByVehicleAndCondition(vehicleType);
    }

}
