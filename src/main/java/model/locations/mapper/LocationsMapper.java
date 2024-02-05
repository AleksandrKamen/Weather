package model.locations.mapper;

import model.locations.dto.LocationsDto;
import model.locations.entity.LocationsEntity;
import util.mapper.Mapper;

public class LocationsMapper implements Mapper<LocationsEntity, LocationsDto> {
    @Override
    public LocationsDto mapFrom(LocationsEntity object) {
        return LocationsDto.builder()
                .name(object.getName())
                .longitude(object.getLongitude())
                .latitude(object.getLatitude())
                .build();
    }
}
