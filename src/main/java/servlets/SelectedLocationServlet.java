package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import model.location.dto.LocationDto;
import model.location.mapper.LocationCreateMapper;
import model.user.service.UserService;
import org.apache.http.client.HttpResponseException;
import service.WeatherAPIService;
import util.ThymeleafUtil;
import util.servlet.BaseServlet;
import validator.exception.LocationAlreadyExistsException;
import validator.exception.OpenWeatherResponseException;
import validator.exception.ValidationException;
import java.io.IOException;
import java.util.Arrays;

@WebServlet("/location")
@Slf4j
public class SelectedLocationServlet extends BaseServlet {
    private final WeatherAPIService weatherAPIService = new WeatherAPIService();
    private final UserService userService = new UserService();
    private final LocationCreateMapper locationCreateMapper = new LocationCreateMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userLogin = context.getVariable("userLogin");
        if (userLogin != null) {
            var locationDto = builLocationDto(req);
            var weatherForLocation = weatherAPIService.getWeatherForLocation(locationDto);
            context.setVariable("location", locationDto);
            context.setVariable("weather", weatherForLocation);
            log.info("location {} is found, processing location page", locationDto.getName());
            templateEngine.process("location", context, resp.getWriter());
        } else {
            log.info("location page - user is not found, redirect to home page");
            resp.sendRedirect(req.getContextPath() + "/");
        }


//           try {
//               var optionalCookie = findCookieByName(req.getCookies(), "session_id");
//               if (optionalCookie.isPresent()) {
//                   var sessionId = optionalCookie.get().getValue();
//                   var optionalUser = userService.getUserBySessionId(sessionId);
//                   if (optionalUser.isPresent()) {
//                       context.setVariable("userLogin", optionalUser.get().getLogin());
//                   } else {
//                       log.info("location page - user is not found, redirect to home page");
//                       resp.sendRedirect(req.getContextPath() + "/");
//                   }
//               } else {
//                   log.info("location page - user is not found, redirect to home page");
//                   resp.sendRedirect(req.getContextPath() + "/");
//               }
//               var locationDto = builLocationDto(req);
//               var weatherForLocation = weatherAPIService.getWeatherForLocation(locationDto);
//               context.setVariable("location", locationDto);
//               context.setVariable("weather",weatherForLocation);
//               log.info("location {} is found, processing location page", locationDto.getName());
//               templateEngine.process("location",context,resp.getWriter());
//           } catch (Exception e){
//               log.error("Error {} in search page, method doGet",  e.getMessage());
//               templateEngine.process("error", context, resp.getWriter());
//           }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var sessionId = context.getVariable("session_id");
        var userLogin = context.getVariable("userLogin");
        if (userLogin != null) {
            var user = userService.getUserWithLocations(userLogin.toString());
            var locationDto = builLocationDto(req);
            var newLocation = locationCreateMapper.mapFrom(locationDto);
            try {
                userService.addLocation(user, newLocation);
                log.info("location {} added to user {}, redirect to home page", newLocation.getName(), user.getLogin());
                resp.sendRedirect(req.getContextPath() + "/");
            } catch (LocationAlreadyExistsException existsException) {
                log.info("location {} has already been added by the user, redirect to home page", newLocation.getName());
                resp.sendRedirect(req.getContextPath() + "/");
            }

        } else {
            log.info("User not found, processing home page");
            resp.sendRedirect(req.getContextPath() + "/");
        }


//        var optionalCookie = findCookieByName(req.getCookies(), "session_id");
//        if (optionalCookie.isPresent()){
//            var sessionId = optionalCookie.get().getValue();
//            var optionalUser = userService.getUserBySessionId(sessionId);
//            if (optionalUser.isPresent()){
//                var user = userService.getUserWithLocations(optionalUser.get().getLogin());
//                var locationDto = builLocationDto(req);
//                var newLocation = locationCreateMapper.mapFrom(locationDto);
//                try {
//                    userService.addLocation(user, newLocation);
//                    log.info("location {} added to user {}, redirect to home page", newLocation.getName(), user.getLogin());
//                    resp.sendRedirect(req.getContextPath() + "/");
//                } catch (LocationAlreadyExistsException existsException){
//                    log.info("location {} has already been added by the user, redirect to home page", newLocation.getName());
//                    resp.sendRedirect(req.getContextPath() + "/");
//                }
//            } else {
//                log.info("location page - user is not found, redirect to home page");
//                resp.sendRedirect(req.getContextPath() + "/");
//            }
//        }
    }
    private LocationDto builLocationDto(HttpServletRequest req){
        var locationName = req.getParameter("name");
        var locationLatitude = req.getParameter("latitude");
        var locationLongitude = req.getParameter("longitude");


        var latitude = (locationLatitude != null) ? Double.valueOf(locationLatitude) : null;
        var longitude = (locationLongitude != null) ? Double.valueOf(locationLongitude) : null;

        return LocationDto.builder()
                .name(locationName)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
