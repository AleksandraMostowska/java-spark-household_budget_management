package mostowska.aleksandra.service.security;

/**
 * AuthorizationCheckService defines the contract for checking authorization of requests.
 * It includes methods for validating access tokens against specific URIs.
 */
public interface AuthorizationCheckService {
    /**
     * Checks whether the provided access token is authorized for the specified URI.
     *
     * @param accessToken The access token to be validated.
     * @param uri The URI for which access is being checked.
     * @return true if the token is authorized for the given URI, false otherwise.
     */
    boolean authorize(String accessToken, String uri);
}
