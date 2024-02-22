package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import model.user.dto.UserDto;
import service.RegistrationService;
import util.servlet.BaseServlet;
import validator.exception.ValidationException;
import java.io.IOException;
@Slf4j
@WebServlet("/registration")
public class RegistrationServlet extends BaseServlet {
    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                log.info("Processing registration page");
                templateEngine.process("registration", context, resp.getWriter());
            } catch (Exception e){
                log.error("Error {} in registration page, method doGet",  e.getMessage());
                templateEngine.process("error", context, resp.getWriter());
            }
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var userDto = buildUserDto(req);
            var newSession = registrationService.registration(userDto);
            log.info("user {} is registered, session with id: {} created", userDto.getLogin(), newSession.getId());
            registrationService.setCookies(resp, newSession);
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (ValidationException validationException) {
            log.info("Validation error during registration: {}", validationException.getErrors());
            context.setVariable("errors", validationException.getErrors());
            templateEngine.process("registration", context, resp.getWriter());
        } catch (Exception e){
            log.error("Error {} in registration page, method doPost",  e.getMessage());
            templateEngine.process("error", context, resp.getWriter());
        }
    }

    private UserDto buildUserDto(HttpServletRequest req) {
        var userLogin = req.getParameter("login");
        var password = req.getParameter("password");
        var confirmPassword = req.getParameter("confirmPassword");

        return UserDto.builder()
                .login(userLogin)
                .password(password)
                .confirmPassword(confirmPassword)
                .build();
    }
}
