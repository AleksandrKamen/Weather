package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.weather.WeatherDto;
import model.location.dto.LocationDto;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import util.PropertiesUtil;
import validator.exception.OpenWeatherExceedingRequestsException;
import validator.exception.OpenWeatherResponseException;
import validator.exception.OpenWeatherUserKeyException;
import java.net.SocketTimeoutException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
    private static final String LANG_KEY = "openWeather_lang";
    private CloseableHttpClient closeableHttpClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WeatherAPIService(CloseableHttpClient closeableHttpClient){
        this.closeableHttpClient = closeableHttpClient;
    }

    public WeatherDto getWeatherForLocation(LocationDto locationsDto) throws OpenWeatherResponseException {
        try {
            var httpGet = createWeatherRequest(locationsDto);
            var execute = closeableHttpClient.execute(httpGet);
            checkResponseCode(execute.getCode());
            var string = EntityUtils.toString(execute.getEntity(), StandardCharsets.UTF_8);
            var weatherDto = objectMapper.readValue(string, WeatherDto.class);
            weatherDto.setWindDirection(getWindDirection(weatherDto));
            weatherDto.setCurrentTime(getCurrentTimeForLocation(weatherDto));
            return weatherDto;
        } catch (SocketTimeoutException timeoutException){
            throw  new OpenWeatherResponseException("Server timeout");
        }
        catch (Exception e){
            throw new OpenWeatherResponseException("Error requesting weather for location: " + locationsDto.getId());
        }
    }

    public List<LocationDto> getLocationsByName(String name) throws OpenWeatherResponseException {

       try {
           var httpGet = createWeatherRequest(name);
           var execute = closeableHttpClient.execute(httpGet);
           checkResponseCode(execute.getCode());
           var string = EntityUtils.toString(execute.getEntity(), StandardCharsets.UTF_8);
           return objectMapper.readValue(string, new TypeReference<List<LocationDto>>() {
           });
       } catch (SocketTimeoutException timeoutException){
           throw  new OpenWeatherResponseException("Server timeout");
       }
       catch (Exception e){
           throw new OpenWeatherResponseException("Request error for location with name : " + e.getMessage());
       }
    }

    private void checkResponseCode(int code){
        switch (code){
            case 401 -> throw new OpenWeatherUserKeyException("User key error");
            case 429 -> throw new OpenWeatherExceedingRequestsException("Number of requests exceeded");
            case 404 -> throw new OpenWeatherExceedingRequestsException("Invalid request format");
            case 500,502,503,504 -> throw new OpenWeatherResponseException("OpenWeather server error");
        }
    }

    private HttpGet createWeatherRequest(LocationDto locationDto) {
        var requestConfig = RequestConfig.custom()
                .setConnectTimeout(10, TimeUnit.SECONDS)
                .build();
        var httpGet = new HttpGet(PropertiesUtil.get(List.of(URL_KEY, DATA_KEY, LATITUDE_KEY))
                + locationDto.getLatitude() + PropertiesUtil.get(LONGITUDE_KEY) + locationDto.getLongitude()
                + PropertiesUtil.get(List.of(APPID_KEY, UNITS_KEY, LANG_KEY)));
        httpGet.setConfig(requestConfig);

        return httpGet;
    }
    private HttpGet createWeatherRequest(String name) {
        var requestConfig = RequestConfig.custom()
                .setConnectTimeout(10, TimeUnit.SECONDS)
                .build();
        var encodeName = URLEncoder.encode(name, StandardCharsets.UTF_8);
        var httpGet = new HttpGet(PropertiesUtil.get(List.of(URL_KEY, GEO_KEY, Q_KEY)) + encodeName
                + PropertiesUtil.get(List.of(LIMIT_KEY, APPID_KEY)));
        httpGet.setConfig(requestConfig);
        return httpGet;
    }

    private String getWindDirection(WeatherDto weatherDto) {
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

    private String getCurrentTimeForLocation(WeatherDto weatherDto){
        var currentUTC = Instant.now();
        var currentWithOffset = currentUTC.plusSeconds(weatherDto.getTimezone());
        var zonedDateTime = ZonedDateTime.ofInstant(currentWithOffset, ZoneId.of("UTC"));
        return zonedDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

}
