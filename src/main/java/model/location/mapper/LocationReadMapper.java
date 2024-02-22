package model.location.mapper;

import model.location.dto.LocationDto;
import model.location.entity.LocationEntity;
import util.mapper.Mapper;

public class LocationReadMapper implements Mapper<LocationEntity, LocationDto> {
    @Override
    public LocationDto mapFrom(LocationEntity object) {
        return LocationDto.builder()
                .id(object.getId())
                .name(object.getName())
                .longitude(object.getLongitude())
                .latitude(object.getLatitude())
                .build();
    }
}
