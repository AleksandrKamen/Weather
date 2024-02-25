package model.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;
import model.weather.WeatherDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LocationDto {
    Long id;
    @JsonProperty("name")
    String name;
    @JsonProperty("lat")
    Double latitude;
    @JsonProperty("lon")
    Double longitude;
    @JsonProperty("country")
    String country;
    @JsonProperty("state")
    String state;
    WeatherDto weatherDto;

}
