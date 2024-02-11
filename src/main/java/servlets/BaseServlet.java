package servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import org.thymeleaf.TemplateEngine;

public abstract class BaseServlet extends HttpServlet {
    protected TemplateEngine templateEngine;
    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = (TemplateEngine) config.getServletContext().getAttribute("templateEngine");
    }
}
