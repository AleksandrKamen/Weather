package service;

import model.location.dto.LocationDto;
import model.location.mapper.LocationReadMapper;
import model.location.repository.LocationRepository;
import model.user.entity.UserEntity;

import java.util.List;

public class LocationService {
    private final LocationRepository locationsRepository = new LocationRepository();
    private final LocationReadMapper locationsMapper = new LocationReadMapper();
    public List<LocationDto> getLocationsByUserLogin(String login) {
            var locationsByUser = locationsRepository.findLocationsByUserLogin(login);
            return locationsByUser.stream().map(locationsMapper::mapFrom).toList();
    }
    public void deleteLocation(Long id){
        locationsRepository.delete(id);
    }
}
