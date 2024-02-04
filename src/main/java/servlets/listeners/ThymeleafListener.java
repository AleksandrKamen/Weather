package servlets.listeners;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import util.ThymeleafUtil;
@WebListener
public class ThymeleafListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        var servletContext = sce.getServletContext();
        var templateEngine = ThymeleafUtil.buildTemplateEngine(servletContext);
        servletContext.setAttribute("templateEngine",templateEngine);
    }
}
