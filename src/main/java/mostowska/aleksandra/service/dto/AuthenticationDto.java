package mostowska.aleksandra.service.dto;

/**
 * AuthenticationDto is a Data Transfer Object that contains
 * the necessary information for user authentication.
 *
 * @param username The username of the user attempting to authenticate.
 * @param password The password of the user attempting to authenticate.
 */
public record AuthenticationDto(String username, String password) {
}
