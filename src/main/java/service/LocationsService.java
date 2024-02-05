package service;

import model.locations.dto.LocationsDto;
import model.locations.entity.LocationsEntity;
import model.locations.mapper.LocationsMapper;
import model.locations.repository.LocationsRepository;
import model.users.repository.UserRepository;
import java.util.Collections;
import java.util.List;

public class LocationsService {
    private final LocationsRepository locationsRepository = new LocationsRepository();
    private final LocationsMapper locationsMapper = new LocationsMapper();
    private final UserRepository userRepository = new UserRepository();


    public List<LocationsDto> getLocationsByUser(Long id) {
        var user = userRepository.findById(id);
        if (user.isPresent()) {
            return user.get().getLocations().stream().map(locationsMapper::mapFrom).toList();
        }
        return Collections.emptyList();
    }


    public void deleteLocation(Long id, LocationsEntity locations) {
        var user = userRepository.findById(id).get();
        user.getLocations().remove(locations);
    }
}
