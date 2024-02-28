package util.servlet;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.session.service.SessionService;
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
    private SessionService sessionService = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = (TemplateEngine) config.getServletContext().getAttribute("templateEngine");
        userService = new UserService();
        sessionService = new SessionService();
        super.init(config);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        context = ThymeleafUtil.getServletContext(req, resp);
        setVariablesUserLoginAndSessionId(req);
        super.service(req, resp);
    }

    protected Optional<Cookie> findCookieByName(Cookie[] cookies, String cookieName) {
        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName)).findFirst();
    }

    private void handleSessionCookie(String sessionId) {
        sessionService.getSessionById(sessionId)
                .filter(sessionService::isSessionExpired)
                .flatMap(session -> userService.getUserBySessionId(sessionId))
                .ifPresent(user -> {
                    context.setVariable("session_id", sessionId);
                    context.setVariable("userLogin", user.getLogin());
                });
    }

    private void setVariablesUserLoginAndSessionId(HttpServletRequest req) throws IOException {
        try {
            findCookieByName(req.getCookies(), "session_id")
                    .ifPresent(sessionCookie -> handleSessionCookie(sessionCookie.getValue()));
        } catch (Exception e) {

        }
    }
}
