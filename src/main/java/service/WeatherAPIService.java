package service;

import com.fasterxml.jackson.core.type.TypeReference;
import model.Weather.WeatherDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.locations.dto.LocationsDto;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import util.PropertiesUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WeatherAPIService {
    private static final String URL_KEY = "openWeather_url";
    private static final String APPID_KEY = "openWeather_appid";
    private static final String DATA_KEY = "openWeather_data";
    private static final String GEO_KEY = "openWeather_geo";
    private static final String LIMIT_KEY = "openWeather_limit";
    private static final String UNITS_KEY = "openWeather_units";
    private static final String Q_KEY = "openWeather_q";
    private static final String LATITUDE_KEY = "openWeather_lat";
    private static final String LONGITUDE_KEY = "openWeather_lon";
    private final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherDto getWeatherForLocation(LocationsDto locationsDto) throws IOException { // Поиск локции по координатам

        var httpGet = new HttpGet(PropertiesUtil.get(List.of(URL_KEY, DATA_KEY, LATITUDE_KEY))
                + locationsDto.getLatitude() + PropertiesUtil.get(LONGITUDE_KEY) + locationsDto.getLongitude()
                + PropertiesUtil.get(List.of(APPID_KEY, UNITS_KEY)));
        var execute = closeableHttpClient.execute(httpGet);
        if (execute.getStatusLine().getStatusCode() != 200) {
            throw new NullPointerException();
        }
        var string = EntityUtils.toString(execute.getEntity(), StandardCharsets.UTF_8);
        return objectMapper.readValue(string, WeatherDto.class);
    }

    public List<LocationsDto> getLocationsByName(String name) throws IOException { // Поиск локаций по названию
        var httpGet = new HttpGet(PropertiesUtil.get(List.of(URL_KEY, GEO_KEY, Q_KEY)) + name
                + PropertiesUtil.get(List.of(LIMIT_KEY, APPID_KEY)));
        var execute = closeableHttpClient.execute(httpGet);
        if (execute.getStatusLine().getStatusCode() != 200) {
            throw new NullPointerException();
        }
        var string = EntityUtils.toString(execute.getEntity(), StandardCharsets.UTF_8);
        return objectMapper.readValue(string, new TypeReference<List<LocationsDto>>() {
        });

    }

    public String getWindDirection(WeatherDto weatherDto) {
        var deg = Double.valueOf(weatherDto.getWind().get("deg").toString());
        if (deg >= 0 && deg < 45) {
            return "северный";
        } else if (deg >= 45 && deg < 90) {
            return "северо-восточный";
        } else if (deg >= 90 && deg < 135) {
            return "восточный";
        } else if (deg >= 135 && deg < 180) {
            return "юго-восточный";
        } else if (deg >= 180 && deg < 225) {
            return "южный";
        } else if (deg >= 225 && deg < 270) {
            return "юго-западный";
        } else if (deg >= 270 && deg < 315) {
            return "западный";
        } else return "северо-западный";
    }
}
