package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.RegistrationService;

import java.io.IOException;
@WebServlet("/logOut")
public class LogOutServler extends HttpServlet {
    private final RegistrationService registrationService = new RegistrationService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //удаляем сессию и перебрасываем на главную страницу
        super.doPost(req, resp);
    }
}
