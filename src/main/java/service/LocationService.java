package service;

import model.location.dto.LocationDto;
import model.location.mapper.LocationMapper;
import model.location.repository.LocationRepository;
import java.util.List;

public class LocationService {
    private final LocationRepository locationsRepository = new LocationRepository();

    public List<LocationDto> getLocationsByUserLogin(String login) {
        var locationsByUser = locationsRepository.findLocationsByUserLogin(login);
        return locationsByUser.stream().map(LocationMapper.INSTANCE::locationToLocationDto).toList();
    }

    public void deleteLocation(Long id) {
        locationsRepository.delete(id);
    }
}
