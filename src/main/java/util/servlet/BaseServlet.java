package util.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.user.service.UserService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import util.ThymeleafUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

public abstract class BaseServlet extends HttpServlet {
    protected TemplateEngine templateEngine;
    protected WebContext context;
    private UserService userService = null;


    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = (TemplateEngine) config.getServletContext().getAttribute("templateEngine");
        userService = new UserService();
        super.init(config);
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context = ThymeleafUtil.getServletContext(req,resp);
        setVariablesUserLoginAndSessionId(req);
        super.service(req, resp);
    }

    protected Optional<Cookie> findCookieByName(Cookie[] cookies, String cookieName){
        if (cookies == null || cookies.length == 0){
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName)).findFirst();
    }
    private void setVariablesUserLoginAndSessionId(HttpServletRequest req) throws IOException {
        try {
            var optionalCookie = findCookieByName(req.getCookies(), "session_id");
            if (optionalCookie.isPresent()) {
                var sessionId = optionalCookie.get().getValue();
                var optionalUser = userService.getUserBySessionId(sessionId);
                if (optionalUser.isPresent()){
                    context.setVariable("session_id",sessionId);
                    context.setVariable("userLogin",optionalUser.get().getLogin());
                }
            }
        } catch (Exception e){

        }
    }
}
