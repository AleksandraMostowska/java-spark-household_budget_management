package mostowska.aleksandra.service.security;

import mostowska.aleksandra.service.dto.AuthenticationDto;
import mostowska.aleksandra.service.dto.AuthorizationDto;
import mostowska.aleksandra.service.dto.RefreshTokenDto;
import mostowska.aleksandra.service.dto.TokensDto;

/**
 * TokensService defines the contract for managing authentication tokens.
 * It includes methods for generating tokens, parsing existing tokens, and refreshing tokens.
 */
public interface TokensService {
    /**
     * Generates a new set of authentication tokens based on the provided authentication data.
     *
     * @param authenticationDto Data Transfer Object containing user authentication information.
     * @return Data Transfer Object containing generated tokens (access and refresh).
     */
    TokensDto generateToken(AuthenticationDto authenticationDto);

    /**
     * Parses the provided token to extract user authorization information.
     *
     * @param token The access token to be parsed.
     * @return Data Transfer Object containing authorization information extracted from the token.
     */
    AuthorizationDto parseTokens(String token);

    /**
     * Refreshes the authentication tokens based on the provided refresh token data.
     *
     * @param refreshTokenDto Data Transfer Object containing refresh token information.
     * @return Data Transfer Object containing new tokens (access and refresh).
     */
    TokensDto refreshTokens(RefreshTokenDto refreshTokenDto);
}
