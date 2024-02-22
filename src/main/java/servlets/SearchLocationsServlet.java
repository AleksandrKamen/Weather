package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import service.WeatherAPIService;
import util.servlet.BaseServlet;
import validator.exception.OpenWeatherResponseException;
import java.io.IOException;

@WebServlet("/search")
@Slf4j
public class SearchLocationsServlet extends BaseServlet {
   private final WeatherAPIService weatherAPIService = new WeatherAPIService(HttpClients.createDefault());
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userLogin = context.getVariable("userLogin");
        if (userLogin != null){
            try {
                var locationName = req.getParameter("name");
                var foundLocations = weatherAPIService.getLocationsByName(locationName);
                context.setVariable("foundLocations", foundLocations);
                templateEngine.process("search_locations", context, resp.getWriter());
            } catch (OpenWeatherResponseException responseException) {
                log.info("Openweathermap response error, redirect to home page", responseException.getMessage());
                resp.sendRedirect(req.getContextPath() + "/");
            } catch (Exception e){
                log.error("Error {} in search page, method doGet", e.getMessage());
                templateEngine.process("error", context, resp.getWriter());
            }
        } else {
            log.info("search page - user is not found, redirect to home page");
            resp.sendRedirect(req.getContextPath() + "/");
        }
    }
}
