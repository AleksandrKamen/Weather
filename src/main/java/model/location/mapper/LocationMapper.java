package model.location.mapper;

import model.location.dto.LocationDto;
import model.location.entity.Location;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LocationMapper {
    LocationMapper INSTANCE = Mappers.getMapper(LocationMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true)
    Location locationDtoToLocation(LocationDto locationDto);
    @Mapping(target = "country", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "weatherDto", ignore = true)
    LocationDto locationToLocationDto(Location location);
}
