package model.user.service;

import model.location.entity.Location;
import model.user.entity.User;
import model.user.repository.UserRepository;
import validator.exception.DataBaseException;
import validator.exception.LocationAlreadyExistsException;

import java.util.Optional;

public class UserService {
    private static final UserRepository userRepository = new UserRepository();

    public Optional<User> getUserBySessionId(String sessionId) {
        return userRepository.findUserBySession(sessionId);
    }
    public void addLocation(User user, Location location) {
        try {
            user.addLocation(location);
            userRepository.update(user);
        } catch (DataBaseException dataBaseException){
            throw new LocationAlreadyExistsException("Location has already been added");
        }
    }
    public User getUserWithLocations(String login) {
        return userRepository.findByLoginWithLocation(login).get();
    }
}
