package model.location.mapper;

import model.location.dto.LocationDto;
import model.location.entity.Location;
import util.mapper.Mapper;

public class LocationReadMapper implements Mapper<Location, LocationDto> {
    @Override
    public LocationDto mapFrom(Location object) {
        return LocationDto.builder()
                .id(object.getId())
                .name(object.getName())
                .longitude(object.getLongitude())
                .latitude(object.getLatitude())
                .build();
    }
}
