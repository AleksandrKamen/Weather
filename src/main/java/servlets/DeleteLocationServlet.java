package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import service.LocationService;
import util.servlet.BaseServlet;
import java.io.IOException;

@WebServlet("/delete")
@Slf4j
public class DeleteLocationServlet extends BaseServlet {
    private final LocationService locationsService = new LocationService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var userLogin = context.getVariable("userLogin");
            var locations = locationsService.getLocationsByUserLogin(userLogin.toString());
            var locationId = req.getParameter("locationId");

            if (locations.stream()
                    .filter(locationDto -> locationDto.getId() == Long.valueOf(locationId))
                    .findFirst()
                    .isPresent()){
                log.info("location with id: {} deleted",locationId);
                locationsService.deleteLocation(Long.valueOf(locationId));
            } else {
                log.info("user {} cannot delete location {}",userLogin, locationId);
            }
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (NumberFormatException numberFormatException){
            log.error("Error parsing location ID: {}", req.getParameter("locationId"));
            resp.sendRedirect(req.getContextPath() + "/");
        }
        catch (Exception e){
            log.error("Error {} in delete page, method doPost",  e.getMessage());
            templateEngine.process("error", context, resp.getWriter());
        }
    }
}
