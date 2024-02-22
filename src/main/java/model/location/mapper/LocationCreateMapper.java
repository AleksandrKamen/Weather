package model.location.mapper;

import model.location.dto.LocationDto;
import model.location.entity.LocationEntity;
import util.mapper.Mapper;

public class LocationCreateMapper implements Mapper<LocationDto, LocationEntity> {
    @Override
    public LocationEntity mapFrom(LocationDto object) {
        return LocationEntity.builder()
                .name(object.getName())
                .latitude(object.getLatitude())
                .longitude(object.getLongitude())
                .build();
    }
}
