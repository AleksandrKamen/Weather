package model.location.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import model.Weather.WeatherDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(of = {"latitude","longitude"})
public class LocationDto {
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("lat")
    private Double latitude;
    @JsonProperty("lon")
    private Double longitude;
    @JsonProperty("country")
    private String country;
    @JsonProperty("state")
    private String state;
    private WeatherDto weatherDto;

}
