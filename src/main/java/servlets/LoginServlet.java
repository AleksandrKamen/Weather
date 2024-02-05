package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.session.entity.SessionEntity;
import model.users.dto.UserDto;
import service.RegistrationService;
import validator.exception.ValidationException;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/registration.html");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var login = req.getParameter("login");
        var password = req.getParameter("password");
        try {
            var newSession = registrationService.registration(UserDto.builder()
                    .login(login)
                    .password(password)
                    .build());
            registrationService.setCookies(req,resp,newSession);
            req.getRequestDispatcher("/WEB-INF/authorization.html");
        } catch (ValidationException validationException){
            resp.sendRedirect("/WEB-INF/registration.html");
        }

    }
}
