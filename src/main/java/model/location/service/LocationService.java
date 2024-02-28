package model.location.service;

import model.location.dto.LocationDto;
import model.location.mapper.LocationMapper;
import model.location.repository.LocationRepository;
import util.PropertiesUtil;

import java.util.Collections;
import java.util.List;

public class LocationService {

    private static final Integer LOCATIONS_LIMIT_PER_PAGE = Integer.parseInt(PropertiesUtil.get("locations_limit_per_page"));
    private final LocationRepository locationsRepository = new LocationRepository();

    public List<LocationDto> getLocationsByUserLogin(String login) {
        var locationsByUser = locationsRepository.findLocationsByUserLogin(login);
        return locationsByUser.stream().map(LocationMapper.INSTANCE::locationToLocationDto).toList();
    }

    public void deleteLocation(Long id) {
        locationsRepository.delete(id);
    }

    public List<LocationDto> getLocationsByUserNameWithPagination(String login, Integer page) {
        var offset = page * LOCATIONS_LIMIT_PER_PAGE - LOCATIONS_LIMIT_PER_PAGE;
        var locations = locationsRepository.findLocationsByUserLoginWithPagination(login, offset, LOCATIONS_LIMIT_PER_PAGE);
        if (!locations.isEmpty()) {
            return locations.stream().map(LocationMapper.INSTANCE::locationToLocationDto).toList();
        }
        return Collections.emptyList();
    }

    public Integer getLastPage(int allUserLocations) {
        return (int) (Math.ceil(allUserLocations / (double) LOCATIONS_LIMIT_PER_PAGE));
    }
}
