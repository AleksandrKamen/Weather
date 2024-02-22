package model.user.service;

import model.location.entity.LocationEntity;
import model.user.entity.UserEntity;
import model.user.repository.UserRepository;
import validator.Error;
import validator.exception.LocationAlreadyExistsException;
import validator.exception.ValidationException;
import java.util.List;
import java.util.Optional;

public class UserService {
    private static final UserRepository userRepository = new UserRepository();

    public Optional<UserEntity> getUserBySessionId(String sessionId) {
        return userRepository.findUserBySession(sessionId);
    }
    public void addLocation(UserEntity user, LocationEntity location) {
        var optionalLocation = user.getLocations().stream().filter(
                locationEntity -> locationEntity.getLatitude().equals(location.getLatitude())
                  && locationEntity.getLongitude().equals(location.getLongitude())
                ).findFirst();
        if (!optionalLocation.isPresent()){
            user.addLocation(location);
            userRepository.update(user);
        } else {
            throw new LocationAlreadyExistsException("Location has already been added");
        }
    }
    public UserEntity getUserWithLocations(String login) {
        return userRepository.findByLoginWithLocation(login).get();
    }


}
