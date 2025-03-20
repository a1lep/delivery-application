package fujitsu.delivery.application.jobs;

import fujitsu.delivery.application.model.Weather;
import fujitsu.delivery.application.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@RequiredArgsConstructor
public class WeatherUpdateJob {

    private static final String WEATHER_URL =
            "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private final RestTemplate restTemplate = new RestTemplate();
    private final WeatherRepository weatherRepository;

    @Scheduled(cron = "0 15 * * * *")
    public void updateWeather(){
        try {
            final String response = restTemplate.getForObject(WEATHER_URL, String.class);

            System.out.println("Weather API Response: " + response);
        } catch (Exception e) {
            System.err.println("Failed to fetch weather data: " + e.getMessage());
        }
    }
}
