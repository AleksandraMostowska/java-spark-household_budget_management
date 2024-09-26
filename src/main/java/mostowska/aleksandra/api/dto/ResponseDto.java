package mostowska.aleksandra.api.dto;

/**
 * ResponseDto is a generic record used for API responses.
 *
 * @param <T> The type of data included in the response.
 */
public record ResponseDto<T>(T data, String error) {

    /**
     * Constructor to create a ResponseDto with data and no error.
     *
     * @param data The data to be included in the response.
     */
    public ResponseDto(T data) {
        this(data, null);
    }

    /**
     * Constructor to create a ResponseDto with an error and no data.
     *
     * @param error The error message to be included in the response.
     */
    public ResponseDto(String error) { // """" Constructor that initializes the response with error and null data """"
        this(null, error);
    }
}
