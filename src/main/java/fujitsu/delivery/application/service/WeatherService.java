package fujitsu.delivery.application.service;

import fujitsu.delivery.application.model.Weather;
import fujitsu.delivery.application.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WeatherService {
    private final WeatherRepository weatherRepository;



}
