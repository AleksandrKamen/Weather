package validator.exception;

public class OpenWeatherExceedingRequestsException extends RuntimeException {
    public OpenWeatherExceedingRequestsException(String message) {
        super(message);
    }
}
