package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.location.dto.LocationDto;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WeatherAPIServiceTest {


  WeatherAPIService weatherAPIService;
  CloseableHttpClient closeableHttpClient;
  @BeforeEach
    void init(){
    closeableHttpClient = mock(CloseableHttpClient.class);
    weatherAPIService = new WeatherAPIService(closeableHttpClient);
  }
  @Test
  void searchLocationsByNameTest() throws IOException {
    var contentResponse = Files.readString(Path.of(  "src/test/resources/search_locations_response.json"));
    var httpEntity = new StringEntity(contentResponse,ContentType.APPLICATION_JSON);
    var closeableHttpResponse = mock(CloseableHttpResponse.class);

    when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
    when(closeableHttpClient.execute(Mockito.any(HttpGet.class))).thenReturn(closeableHttpResponse);

    var locations = weatherAPIService.getLocationsByName("London");
    var locationDto = LocationDto.builder().name("City of London").latitude(51.5156177).longitude(-0.0919983).build();

    assertThat(locations).hasSize(5);
    assertThat(locations).contains(locationDto);

  }

  @Test
  void searchWeatherForLocationTest() throws IOException {
    var contentResponse = Files.readString(Path.of(  "src/test/resources/weather_for_location_response.json"));
    var httpEntity = new StringEntity(contentResponse,ContentType.APPLICATION_JSON);
    var closeableHttpResponse = mock(CloseableHttpResponse.class);

    when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
    when(closeableHttpClient.execute(Mockito.any(HttpGet.class))).thenReturn(closeableHttpResponse);
    var locataion = LocationDto.builder().name("City of London").latitude(51.5156177).longitude(-0.0919983).build();
    var weatherForLocation = weatherAPIService.getWeatherForLocation(locataion);
    assertAll(
            ()-> assertThat(weatherForLocation.getWeather()[0].get("icon")).isEqualTo("04d"),
            ()-> assertThat(weatherForLocation.getMain().get("temp")).isEqualTo(13.27),
            ()-> assertThat(weatherForLocation.getMain().get("feels_like")).isEqualTo(13.08),
            ()-> assertThat(weatherForLocation.getMain().get("temp_min")).isEqualTo(12.21),
            ()-> assertThat(weatherForLocation.getMain().get("temp_max")).isEqualTo(14.49),
            ()-> assertThat(weatherForLocation.getWeather()[0].get("description")).isEqualTo("пасмурно"),
            ()-> assertThat(weatherForLocation.getMain().get("humidity")).isEqualTo(93),
            ()-> assertThat(weatherForLocation.getMain().get("pressure")).isEqualTo(1023),
            ()-> assertThat(weatherForLocation.getWind().get("speed")).isEqualTo(5.66),
            ()-> assertThat(weatherForLocation.getWind().get("gust")).isNull()
    );
  }
}