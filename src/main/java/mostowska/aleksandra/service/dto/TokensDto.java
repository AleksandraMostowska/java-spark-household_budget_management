package mostowska.aleksandra.service.dto;

/**
 * TokensDto is a Data Transfer Object that holds access and refresh tokens.
 *
 * @param accessToken  The JWT access token for user authentication.
 * @param refreshToken The JWT refresh token used to obtain a new access token.
 */
public record TokensDto(String accessToken, String refreshToken) {
}
