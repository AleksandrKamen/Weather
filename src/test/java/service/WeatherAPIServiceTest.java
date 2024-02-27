package service;

import model.location.dto.LocationDto;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import validator.exception.OpenWeatherResponseException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherAPIServiceTest {

    @InjectMocks
    WeatherAPIService weatherAPIService;
    @Mock
    CloseableHttpClient closeableHttpClient;
    @Mock
    CloseableHttpResponse closeableHttpResponse;

    @Test
    void searchLocationsByNameTest() throws IOException {
        var contentResponse = Files.readString(Path.of("src/test/resources/search_locations_response.json"));
        var httpEntity = new StringEntity(contentResponse, ContentType.APPLICATION_JSON);
        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
        when(closeableHttpResponse.getCode()).thenReturn(200);
        when(closeableHttpClient.execute(any(HttpGet.class))).thenReturn(closeableHttpResponse);
        var locations = weatherAPIService.getLocationsByName("London");
        assertThat(locations).hasSize(5);
        assertTrue(locations.stream()
                .filter(locationDto1 -> locationDto1.getName().equals("City of London"))
                .findFirst()
                .isPresent());
    }
    @Test
    void searchWeatherForLocationTest() throws IOException {
        var contentResponse = Files.readString(Path.of("src/test/resources/weather_for_location_response.json"));
        var httpEntity = new StringEntity(contentResponse, ContentType.APPLICATION_JSON);

        when(closeableHttpResponse.getEntity()).thenReturn(httpEntity);
        when(closeableHttpResponse.getCode()).thenReturn(200);
        when(closeableHttpClient.execute(any(HttpGet.class))).thenReturn(closeableHttpResponse);
        var locataion = LocationDto.builder().name("City of London").latitude(51.5156177).longitude(-0.0919983).build();
        var weatherForLocation = weatherAPIService.getWeatherForLocation(locataion);
        assertAll(
                () -> assertThat(weatherForLocation.getWeather()[0].get("icon")).isEqualTo("04d"),
                () -> assertThat(weatherForLocation.getMain().get("temp")).isEqualTo(13.27),
                () -> assertThat(weatherForLocation.getMain().get("feels_like")).isEqualTo(13.08),
                () -> assertThat(weatherForLocation.getMain().get("temp_min")).isEqualTo(12.21),
                () -> assertThat(weatherForLocation.getMain().get("temp_max")).isEqualTo(14.49),
                () -> assertThat(weatherForLocation.getWeather()[0].get("description")).isEqualTo("пасмурно"),
                () -> assertThat(weatherForLocation.getMain().get("humidity")).isEqualTo(93),
                () -> assertThat(weatherForLocation.getMain().get("pressure")).isEqualTo(1023),
                () -> assertThat(weatherForLocation.getWind().get("speed")).isEqualTo(5.66),
                () -> assertThat(weatherForLocation.getWind().get("gust")).isNull()
        );
    }
    @Nested
    class OpenWeatherInvalidResponse {
        @Test
        void userKeyException() throws IOException {
            when(closeableHttpResponse.getCode()).thenReturn(401);
            when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
            assertAll(
                    () -> assertThrows(OpenWeatherResponseException.class, () -> weatherAPIService.getLocationsByName("London")),
                    () -> assertThrows(OpenWeatherResponseException.class, () -> weatherAPIService.getWeatherForLocation(
                            LocationDto.builder().name("City of London").latitude(51.5156177).longitude(-0.0919983).build()
                    ))
            );
        }

        @Test
        void exceedingRequestsException() throws IOException {
            when(closeableHttpResponse.getCode()).thenReturn(429);
            when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
            assertAll(
                    () -> assertThrows(OpenWeatherResponseException.class, () -> weatherAPIService.getLocationsByName("London")),
                    () -> assertThrows(OpenWeatherResponseException.class, () -> weatherAPIService.getWeatherForLocation(
                            LocationDto.builder().name("City of London").latitude(51.5156177).longitude(-0.0919983).build()
                    ))
            );
        }

        @ParameterizedTest
        @ValueSource(ints = {500, 502, 503, 504})
        void exceedingRequestsException(int code) throws IOException {
            when(closeableHttpResponse.getCode()).thenReturn(code);
            when(closeableHttpClient.execute(any())).thenReturn(closeableHttpResponse);
            assertAll(
                    () -> assertThrows(OpenWeatherResponseException.class, () -> weatherAPIService.getLocationsByName("London")),
                    () -> assertThrows(OpenWeatherResponseException.class, () -> weatherAPIService.getWeatherForLocation(
                            LocationDto.builder().name("City of London").latitude(51.5156177).longitude(-0.0919983).build()
                    ))
            );
        }
    }
}