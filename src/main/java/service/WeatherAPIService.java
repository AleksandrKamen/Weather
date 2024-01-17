package service;

import Weather.WeatherEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import locations.dto.LocationsDto;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import util.PropertiesUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WeatherAPIService {
    private final String URL_KEY = "openWeather_url";
    private final String APPID_KEY = "openWeather_appid";
    private final String DATA_KEY = "openWeather_data";
    private final String GEO_KEY = "openWeather_geo";
    private final String LIMIT_KEY = "openWeather_limit";
    private final String UNITS_KEY = "openWeather_units";
    private final String Q_KEY = "openWeather_q";
    private final CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherEntity getWeatherByLocationName(String locationName) throws IOException {

        var httpGet = new HttpGet(PropertiesUtil.get(List.of(URL_KEY,DATA_KEY,Q_KEY))+locationName
                +PropertiesUtil.get(List.of(APPID_KEY,UNITS_KEY)));
        var execute = closeableHttpClient.execute(httpGet);
        if (execute.getStatusLine().getStatusCode() != 200) {
            throw new NullPointerException();
        }
        var string = EntityUtils.toString(execute.getEntity(), StandardCharsets.UTF_8);
        return objectMapper.readValue(string, WeatherEntity.class);
    }

//    public List<LocationsDto> getLocationsByName(String name){
//
//    }

}
