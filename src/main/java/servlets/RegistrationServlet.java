package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import model.users.dto.UserDto;
import model.users.service.UserService;
import service.RegistrationService;
import util.ThymeleafUtil;
import validator.exception.ValidationException;

import java.io.IOException;
@Slf4j
@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {
    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            templateEngine.process("registration", servletContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var userLogin = req.getParameter("login");
            var password = req.getParameter("password");
            var confirmPassword = req.getParameter("confirmPassword");
            var userDto = buildUserDto(userLogin, password, confirmPassword);
            var newSession = registrationService.registration(userDto);
            registrationService.setCookies(req, resp, newSession);
            resp.sendRedirect("/");

        } catch (ValidationException validationException) {
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            servletContext.setVariable("errors", validationException.getErrors());
            templateEngine.process("registration", servletContext, resp.getWriter());
        } catch (Exception e){
            log.error(e.getMessage());
            resp.sendRedirect("registration");
        }
    }

    private UserDto buildUserDto(String login, String password, String confirmPassword) {
        return UserDto.builder()
                .login(login)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
    }
}
