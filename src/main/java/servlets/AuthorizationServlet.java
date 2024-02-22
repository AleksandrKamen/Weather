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
@WebServlet("/authorization")
public class AuthorizationServlet extends BaseServlet {
    private final RegistrationService registrationService = new RegistrationService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            try {
                log.info("Processing authorization page");
                templateEngine.process("authorization",context,resp.getWriter());
            } catch (Exception e){
                log.error("Error {} in authorization page, method doGet",  e.getMessage());
                templateEngine.process("error", context, resp.getWriter());
            }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var userDto = buildUserDto(req);
            var session = registrationService.authorization(userDto);
            registrationService.setCookies(resp,session);
            log.info("{} is logged in, session with id: {} created", userDto.getLogin(), session.getId());
            resp.sendRedirect(req.getContextPath() + "/");
        } catch (IllegalArgumentException illegalArgumentException){
            log.info("Missing login or password parameters, processing authorization page");
            templateEngine.process("authorization", context, resp.getWriter());
        }
        catch (ValidationException validationException){
            context.setVariable("errors", validationException.getErrors());
            log.info("Validation error during authorization: {}", validationException.getErrors());
            templateEngine.process("authorization", context, resp.getWriter());
        } catch (Exception e){
            log.error("Error {} in authorization page, method doPost",  e.getMessage());
            templateEngine.process("error", context, resp.getWriter());
        }
    }

    private UserDto buildUserDto(HttpServletRequest req){
        var login = req.getParameter("login");
        var password = req.getParameter("password");
        if (login == null || password == null){
            throw new  IllegalArgumentException("Missing login or password parameters");
        }
        return UserDto.builder()
                .login(login)
                .password(password)
                .build();
    }
}
