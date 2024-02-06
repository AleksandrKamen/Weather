package servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.users.dto.UserDto;
import model.users.service.UserService;
import org.thymeleaf.TemplateEngine;
import util.ThymeleafUtil;
import validator.exception.ValidationException;

import java.io.IOException;

@WebServlet("/registration")
public class RegistrationServlet extends HttpServlet {
    private TemplateEngine templateEngine;
    private UserService userService = new UserService();
    @Override
    public void init(ServletConfig config) throws ServletException {
        templateEngine = (TemplateEngine) config.getServletContext().getAttribute("templateEngine");
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (templateEngine != null){
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            templateEngine.process("registration",servletContext,resp.getWriter());
        } else {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "TemplateEngine is not initialized");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var userLogin = req.getParameter("login");
        var password = req.getParameter("password");
        var confirmPassword = req.getParameter("confirmPassword");

        var userDto = buildUserDto(userLogin, password, confirmPassword);

        try {
            var newUser = userService.createUser(userDto);
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            templateEngine.process("home_page",servletContext,resp.getWriter());
        } catch (ValidationException validationException){
            // TODO: 06.02.2024 ошибки ввода пользователя
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            templateEngine.process("registration",servletContext,resp.getWriter());
        }


    }

    private UserDto buildUserDto(String login, String password, String confirmPassword){
        return UserDto.builder()
                .login(login)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
    }
}
