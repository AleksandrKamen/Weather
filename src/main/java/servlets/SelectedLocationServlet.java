package servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.locations.dto.LocationsDto;
import org.thymeleaf.TemplateEngine;
import service.WeatherAPIService;
import util.ThymeleafUtil;

import java.io.IOException;

@WebServlet("/location")
public class SelectedLocationServlet extends HttpServlet {
    private final WeatherAPIService weatherAPIService = new WeatherAPIService();
    private TemplateEngine templateEngine;
    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = (TemplateEngine) config.getServletContext().getAttribute("templateEngine");
    }
    // страница конкретной лакции - содержит погоду и данные о локации, есть кнопка назад на главную страницу
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (templateEngine != null){
            var name = req.getParameter("name");
            var latitude = req.getParameter("latitude");
            var longitude = req.getParameter("longitude");
            var locationDto = builLocationDto(name, latitude, longitude);
            var weatherForLocation = weatherAPIService.getWeatherForLocation(locationDto);
            req.setAttribute("location", locationDto);
            req.setAttribute("weather",weatherForLocation);
            req.setAttribute("windDirection",weatherAPIService.getWindDirection(weatherForLocation));
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            templateEngine.process("location",servletContext,resp.getWriter());
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "TemplateEngine is not initialized");
        }
    }

    private LocationsDto builLocationDto(String name, String latitude, String longitude){
        return LocationsDto.builder()
                .name(name)
                .latitude(Double.valueOf(latitude))
                .longitude(Double.valueOf(longitude))
                .build();
    }
}
