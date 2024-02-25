package model.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class WeatherDto {
    @JsonProperty("name")
    String name;
    @JsonProperty("weather")
    Map<String, Object>[] weather;
    @JsonProperty("main")
    Map<String, Object> main;
    @JsonProperty("wind")
    Map<String, Object> wind;
    @JsonProperty("timezone")
    Long timezone;
    String windDirection;
    String currentTime;
}
