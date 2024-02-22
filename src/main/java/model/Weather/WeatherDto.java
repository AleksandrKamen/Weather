package model.Weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherDto {
    @JsonProperty("name")
    private String name;
    @JsonProperty("weather")
    private Map<String, Object>[] weather;
    @JsonProperty("main")
    private Map<String, Object> main;
    @JsonProperty("wind")
    private Map<String, Object> wind;
    @JsonProperty("timezone")
    private Long timezone;
    private String windDirection;
    private String currentTime;
}
