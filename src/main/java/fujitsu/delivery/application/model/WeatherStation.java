package fujitsu.delivery.application.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherStation {

    @JacksonXmlProperty(localName = "name")
    private String stationName;

    @JacksonXmlProperty(localName = "wmocode")
    private Double wmoCode;

    @JacksonXmlProperty(localName = "airtemperature")
    private Double airTemperature;

    @JacksonXmlProperty(localName = "windspeed")
    private Double windSpeed;

    @JacksonXmlProperty(localName = "phenomenon")
    private String phenomenon;

}
