package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import service.RegistrationService;

import java.io.IOException;

@WebServlet("/logOut")
@Slf4j
public class SignOutServler extends HttpServlet {
    private final RegistrationService registrationService = new RegistrationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        registrationService.logOut(req, resp);
        resp.sendRedirect(req.getContextPath() + "/");
    }
}
