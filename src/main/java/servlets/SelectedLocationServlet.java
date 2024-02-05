package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import service.WeatherAPIService;

import java.io.IOException;

@WebServlet("/location")
public class SelectedLocationServlet extends HttpServlet {
   private final WeatherAPIService weatherAPIService = new WeatherAPIService();
    // страница конкретной лакции - содержит погоду и данные о локации, есть кнопка назад на главную страницу
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doGet(req, resp);
    }
}
