package mostowska.aleksandra.service.dto;

/**
 * RefreshTokenDto is a Data Transfer Object that holds a refresh token.
 *
 * @param token The JWT refresh token used to obtain a new access token.
 */
public record RefreshTokenDto(String token) {
}
