package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import service.LocationService;
import service.WeatherAPIService;
import util.servlet.BaseServlet;
import java.io.IOException;

@WebServlet("")
@Slf4j
public class HomePageServlet extends BaseServlet {
    private final WeatherAPIService weatherAPIService = new WeatherAPIService(HttpClients.createDefault());
    private final LocationService locationsService = new LocationService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
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
        } catch (Exception e) {
            log.error("Error {} in home page, method doGet", e.getMessage());
            templateEngine.process("error", context, resp.getWriter());
        }
    }
}
