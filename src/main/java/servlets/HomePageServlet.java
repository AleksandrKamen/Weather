package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import model.user.service.UserService;
import service.LocationService;
import service.WeatherAPIService;
import util.ThymeleafUtil;
import util.servlet.BaseServlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@WebServlet("")
@Slf4j
public class HomePageServlet extends BaseServlet {
    private final WeatherAPIService weatherAPIService = new WeatherAPIService();
    private final UserService userService = new UserService();
    private final LocationService locationsService = new LocationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var sessionId = context.getVariable("session_id");
        var userLogin = context.getVariable("userLogin");
        if (userLogin != null){
            log.info("User {} found, session id: {}", userLogin, sessionId);
            var userLocations = locationsService.getLocationsByUserLogin(userLogin.toString());
            userLocations.forEach(locationDto -> {
                var weatherForLocation = weatherAPIService.getWeatherForLocation(locationDto);
                locationDto.setWeatherDto(weatherForLocation);
            });
            context.setVariable("locations", userLocations);
            log.info("Processing home page");
        } else {
            log.info("User not found, processing home page");
        }
        templateEngine.process("home", context, resp.getWriter());


//        try {
//            var optionalCookie = findCookieByName(req.getCookies(), "session_id");
//            if (optionalCookie.isPresent()) {
//                var sessionId = optionalCookie.get().getValue();
//                var optionalUser = userService.getUserBySessionId(sessionId);
//                if (optionalUser.isPresent()) {
//                    var user = optionalUser.get();
//                    log.info("User {} found, session id: {}", user.getLogin(), sessionId);
//                    var locationsByUser = locationsService.getLocationsByUserLogin(user.getLogin());
//                    locationsByUser.forEach(locationDto -> {
//                        var weatherForLocation = weatherAPIService.getWeatherForLocation(locationDto);
//                        locationDto.setWeatherDto(weatherForLocation);
//                    });
//                    context.setVariable("userLogin", user.getLogin());
//                    context.setVariable("locations", locationsByUser);
//                }
//            }
//            log.info("Processing home page");
//            templateEngine.process("home", context, resp.getWriter());
//        } catch (Exception e) {
//            log.error("Error {} in home page, method doGet", e.getMessage());
//            templateEngine.process("error", context, resp.getWriter());
//        }
    }
}
