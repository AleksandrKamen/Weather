package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import model.location.service.LocationService;
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
                var parameter = req.getParameter("page");
                var page = parameter == null || Integer.parseInt(parameter) < 1 ? 1 : Integer.parseInt(parameter);
                var allUserLocations = locationsService.getLocationsByUserLogin(userLogin.toString());
                var locationsWithPagination = locationsService.getLocationsByUserNameWithPagination(userLogin.toString(), page);
                locationsWithPagination.forEach(locationDto -> {
                    var weatherForLocation = weatherAPIService.getWeatherForLocation(locationDto);
                    locationDto.setWeatherDto(weatherForLocation);
                });

                context.setVariable("locations", locationsWithPagination);
                context.setVariable("lastPage", locationsService.getLastPage(allUserLocations.size()));
                context.setVariable("page", page);

                context.setVariable("locationRepeat",req.getSession().getAttribute("locationRepeat"));
                req.getSession().removeAttribute("locationRepeat");
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
