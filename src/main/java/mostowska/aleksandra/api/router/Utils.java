package mostowska.aleksandra.api.router;

import spark.Response;

/**
 * Utils interface provides utility methods for routing operations.
 */
public interface Utils {
    /**
     * Sets the response headers and status code for a given response.
     *
     * @param response   The Spark response object to be modified.
     * @param statusCode The HTTP status code to be set on the response.
     */
    static void setResponse(Response response, int statusCode) {
        response.header("Content-Type", "application/json;charset=utf-8");
        response.status(statusCode);
    }
}
