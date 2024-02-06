package servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import service.WeatherAPIService;
import util.ThymeleafUtil;

import java.io.IOException;

@WebServlet("")
public class HomePageServlet extends HttpServlet {
    private final WeatherAPIService weatherAPIService = new WeatherAPIService();
    private TemplateEngine templateEngine;
    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = (TemplateEngine) config.getServletContext().getAttribute("templateEngine");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (templateEngine != null){
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            templateEngine.process("home_page",servletContext,resp.getWriter());
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "TemplateEngine is not initialized");
        }
    }

}
