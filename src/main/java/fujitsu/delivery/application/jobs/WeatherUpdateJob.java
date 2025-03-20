package fujitsu.delivery.application.jobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import fujitsu.delivery.application.model.Weather;
import fujitsu.delivery.application.model.WeatherDataXML;
import fujitsu.delivery.application.model.WeatherStation;
import fujitsu.delivery.application.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class WeatherUpdateJob {

    private static final Set<String> TARGET_CITIES = Set.of("Tallinn-Harku", "Tartu-Tõravere", "Pärnu");

    private static final String WEATHER_URL =
            "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";
    private final RestTemplate restTemplate = new RestTemplate();
    private final WeatherRepository weatherRepository;
    private final XmlMapper xmlMapper = new XmlMapper();

    @Scheduled(cron = "0 15 * * * *")
    public void updateWeather() throws JsonProcessingException {
            final String response = restTemplate.getForObject(WEATHER_URL, String.class);

            final WeatherDataXML weatherData = xmlMapper.readValue(response, WeatherDataXML.class);
            final List<Weather> weatherList = weatherData.getStations().stream()
                    .filter(station -> TARGET_CITIES.contains(station.getStationName()))
                    .map(this::mapToWeather)
                    .toList();

            weatherList.forEach(weatherRepository::save);
            System.out.println("Weather data updated successfully.");
    }
    private Weather mapToWeather(final WeatherStation station) {
        return new Weather(
                station.getStationName(),
                station.getWmoCode(),
                station.getAirTemperature(),
                station.getWindSpeed(),
                station.getPhenomenon(),
                new Timestamp(System.currentTimeMillis())
        );
    }
}
