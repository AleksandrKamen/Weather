package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import model.user.service.UserService;
import org.apache.http.client.HttpResponseException;
import service.WeatherAPIService;
import util.ThymeleafUtil;
import util.servlet.BaseServlet;
import validator.exception.OpenWeatherResponseException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@WebServlet("/search")
@Slf4j
public class SearchLocationsServlet extends BaseServlet {
   private final WeatherAPIService weatherAPIService = new WeatherAPIService();
   private final UserService userService = new UserService();

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
                resp.sendRedirect(req.getContextPath() + "/");
            } catch (Exception e){
                log.error("Error {} in search page, method doGet", e.getMessage());
                templateEngine.process("error", context, resp.getWriter());
            }
        } else {
            log.info("search page - user is not found, redirect to home page");
            resp.sendRedirect(req.getContextPath() + "/");
        }




//        var optionalCookie = findCookieByName(req.getCookies(), "session_id");
//        if (optionalCookie.isPresent()) {
//            var sessionId = optionalCookie.get().getValue();
//            var optionalUser = userService.getUserBySessionId(sessionId);
//            if (optionalUser.isPresent()) {
//                context.setVariable("userLogin", optionalUser.get().getLogin());
//            } else {
//                log.info("search page - user is not found, redirect to home page");
//                resp.sendRedirect(req.getContextPath() + "/");
//            }
//        } else {
//            log.info("search page - user is not found, redirect to home page");
//            resp.sendRedirect(req.getContextPath() + "/");
//        }
//        try {
//            var locationName = req.getParameter("locationName");
//            var foundLocations = weatherAPIService.getLocationsByName(locationName);
//            context.setVariable("foundLocations", foundLocations);
//            templateEngine.process("search_locations", context, resp.getWriter());
//        } catch (OpenWeatherResponseException responseException) {
//            resp.sendRedirect(req.getContextPath() + "/");
//        } catch (Exception e){
//            log.error("Error {} in search page, method doGet", e.getMessage());
//            templateEngine.process("error", context, resp.getWriter());
//        }
    }
}
