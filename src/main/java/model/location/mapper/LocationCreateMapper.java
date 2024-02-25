package model.location.mapper;

import model.location.dto.LocationDto;
import model.location.entity.Location;
import util.mapper.Mapper;

public class LocationCreateMapper implements Mapper<LocationDto, Location> {
    @Override
    public Location mapFrom(LocationDto object) {
        return Location.builder()
                .name(object.getName())
                .latitude(object.getLatitude())
                .longitude(object.getLongitude())
                .build();
    }
}
