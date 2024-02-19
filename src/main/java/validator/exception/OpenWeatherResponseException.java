package validator.exception;

public class OpenWeatherResponseException extends RuntimeException {
    public OpenWeatherResponseException(String message) {
        super(message);
    }
}
