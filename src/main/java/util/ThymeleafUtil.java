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

    public TemplateEngine buildTemplateEngine(ServletContext servletContext){ // создание объекта TemplateEngine для обработки шаблонов

        IWebApplication webApplication = JakartaServletWebApplication.buildApplication(servletContext);
        var templateResolver = buildTemplateResolver(webApplication); // создаем TemplateResolver через наш метод
        var templateEngine = new TemplateEngine();             // Создаем TemplateEngine
        templateEngine.setTemplateResolver(templateResolver); // Применяем наш TemplateResolver
        return templateEngine;          // Возвращаем TemplateEngine
    }

    public WebContext getServletContext(HttpServletRequest request, HttpServletResponse response){ // получаем WebContext - контекст данных используемый для обработки шаблонов
        var servletContext = request.getServletContext(); // получаем контекст из запроса
        var webApplication = JakartaServletWebApplication.buildApplication(servletContext); // Этот объект используется для взаимодействия с Thymeleaf в контексте веб-приложения.
        var webExchange = webApplication.buildExchange(request,response); // Этот объект предоставляет информацию о текущем HTTP-запросе и HTTP-ответе
        return new WebContext(webExchange); // возвращаем контекст
    }

    public ITemplateResolver buildTemplateResolver (IWebApplication webApplication){ // Создаем объект Resolver, ITemplateResolver - это обстракция для всех Resolver, IWebApplication - абстракция для веб приложения
        var templateResolver = new WebApplicationTemplateResolver(webApplication);  //
        templateResolver.setTemplateMode(TemplateMode.HTML); // Устанвливаем модификацию
        templateResolver.setPrefix("/WEB-INF/templates/");  // Устанавливаем путь к шаблонам
        templateResolver.setSuffix(".html"); //  устанавливаем префикс
        templateResolver.setCacheTTLMs(3600000L); // Установка времени нахождения в кэш
        return templateResolver;                 // возвращаем созданный объект
    }



}
