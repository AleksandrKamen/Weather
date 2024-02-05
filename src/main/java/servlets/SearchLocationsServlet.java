package servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import service.LocationsService;

@WebServlet("/search")
public class SearchLocationsServlet extends HttpServlet {
   private final LocationsService locationsService = new LocationsService();
    //Выводит найденные локации по поиску, локации содердат кнопку добавить, также есть поле поиска, после добавления переносит на главную страницу
}
