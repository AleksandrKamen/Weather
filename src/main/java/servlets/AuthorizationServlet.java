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
@WebServlet("/login")
public class AuthorizationServlet extends BaseServlet {
    private final RegistrationService registrationService = new RegistrationService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            templateEngine.process("authorization",servletContext,resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            var login = req.getParameter("login");
            var password = req.getParameter("password");
            var userDto = buildUserDto(login, password);
            var session = registrationService.authorization(userDto);
            registrationService.setCookies(req,resp,session);
            resp.sendRedirect("/");
        } catch (ValidationException validationException){
            var servletContext = ThymeleafUtil.getServletContext(req, resp);
            servletContext.setVariable("errors", validationException.getErrors());
            templateEngine.process("authorization", servletContext, resp.getWriter());
        } catch (Exception e){
            log.error(e.getMessage());
            resp.sendRedirect("authorization");
        }
    }

    private UserDto buildUserDto(String login, String password){
        return UserDto.builder()
                .login(login)
                .password(password)
                .build();
    }
}
