package locations.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class LocationsDto {
    private String name;
    private Long latitude;
    private Long longitude;
}
