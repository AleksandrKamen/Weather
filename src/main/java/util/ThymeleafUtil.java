package util;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

@UtilityClass
public class ThymeleafUtil {

    public TemplateEngine buildTemplateEngine(ServletContext servletContext){

        IWebApplication webApplication = JakartaServletWebApplication.buildApplication(servletContext);
        var templateResolver = buildTemplateResolver(webApplication);
        var templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        return templateEngine;
    }

    public WebContext getServletContext(HttpServletRequest request, HttpServletResponse response){
        var servletContext = request.getServletContext();
        var webApplication = JakartaServletWebApplication.buildApplication(servletContext);
        var webExchange = webApplication.buildExchange(request,response);
        return new WebContext(webExchange);
    }

    public ITemplateResolver buildTemplateResolver (IWebApplication webApplication){
        var templateResolver = new WebApplicationTemplateResolver(webApplication);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(0L);
        templateResolver.setCharacterEncoding("UTF-8");
        return templateResolver;
    }
}
