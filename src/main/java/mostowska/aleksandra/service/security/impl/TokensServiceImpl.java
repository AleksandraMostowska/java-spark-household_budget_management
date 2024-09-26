package mostowska.aleksandra.service.security.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.security.TokensService;
import mostowska.aleksandra.service.dto.AuthenticationDto;
import mostowska.aleksandra.service.dto.AuthorizationDto;
import mostowska.aleksandra.service.dto.RefreshTokenDto;
import mostowska.aleksandra.service.dto.TokensDto;
import mostowska.aleksandra.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * TokensServiceImpl provides methods for generating, parsing, and refreshing JWT tokens.
 * It validates user credentials and manages token expiration.
 */
@Service
@RequiredArgsConstructor
public class TokensServiceImpl implements TokensService {

    @Value("${tokens.access.expiration_time_ms}")
    private Long accessTokenExpirationTimeMs;

    @Value("${tokens.refresh.expiration_time_ms}")
    private Long refreshTokenExpirationTimeMs;

    @Value("${tokens.refresh.access_token_expiration_time_ms_property}")
    private String refreshTokenProperty;

    @Value("${tokens.prefix}")
    private String tokensPrefix;

    private final UserRepository userRepository;
    private final SecretKey secretKey;
    private final PasswordEncoder passwordEncoder;

    /**
     * Generates JWT tokens for authenticated users.
     *
     * @param authenticationDto Data Transfer Object containing authentication data.
     * @return TokensDto containing the generated access and refresh tokens.
     */
    @Override
    public TokensDto generateToken(AuthenticationDto authenticationDto) {
        var userFromDb = userRepository
                .findByUsername(authenticationDto.username())
                .orElseThrow(() -> new IllegalStateException("Authentication failed [1]!"));

        if (!userFromDb.matchesPassword(passwordEncoder, authenticationDto.password())) {
            throw new IllegalStateException("Authentication failed [2]!");
        }

        var userId = userFromDb.toGetUserDto().id();
        var currentDate = new Date();
        var accessTokenExpirationDate = new Date(currentDate.getTime() + accessTokenExpirationTimeMs);
        var refreshTokenExpirationDate = new Date(currentDate.getTime() + refreshTokenExpirationTimeMs);


        var accessToken = Jwts
                .builder()
                .setSubject(userId.toString())
                .setExpiration(accessTokenExpirationDate)
                .setIssuedAt(currentDate)
                .signWith(secretKey)
                .compact();


        var refreshToken = Jwts
                .builder()
                .setSubject(userId.toString())
                .setExpiration(refreshTokenExpirationDate)
                .setIssuedAt(currentDate)
                .claim(refreshTokenProperty, accessTokenExpirationDate.getTime())
                .signWith(secretKey)
                .compact();

        return new TokensDto(accessToken, refreshToken);
    }

    /**
     * Parses the given token to extract user authorization information.
     *
     * @param token The access token to be parsed.
     * @return AuthorizationDto containing user authorization details.
     */
    @Override
    public AuthorizationDto parseTokens(String token) {
        if (token == null) {
            throw new IllegalStateException("Token is null");
        }

        if (!isTokenValid(token)) {
            throw new IllegalStateException("Token has been expired");
        }

        var userId = id(token);
        return userRepository
                .findById(userId)
                .map(User::toAuthorizationDto)
                .orElseThrow(() -> new IllegalStateException("Authorization failed"));
    }

    /**
     * Refreshes the access and refresh tokens based on the provided refresh token.
     *
     * @param refreshTokenDto Data Transfer Object containing the refresh token.
     * @return TokensDto containing the new access and refresh tokens.
     */
    @Override
    public TokensDto refreshTokens(RefreshTokenDto refreshTokenDto) {
        var token = refreshTokenDto.token();

        if (!isTokenValid(token)) {
            throw new IllegalStateException("Refresh token has been expired");
        }

        if (accessTokenExpirationDateMsInRefreshToken(token) < System.currentTimeMillis()) {
            throw new IllegalStateException("Access token has been expired");
        }

        var userId = id(token);
        var currentDate = new Date();
        var accessTokenExpirationDate = new Date(currentDate.getTime() + accessTokenExpirationTimeMs);
        var refreshTokenExpirationDate = expirationDate(token);


        var accessToken = Jwts
                .builder()
                .setSubject(userId.toString())
                .setExpiration(accessTokenExpirationDate)
                .setIssuedAt(currentDate)
                .signWith(secretKey)
                .compact();


        var refreshToken = Jwts
                .builder()
                .setSubject(userId.toString())
                .setExpiration(refreshTokenExpirationDate)
                .setIssuedAt(currentDate)
                .claim(refreshTokenProperty, accessTokenExpirationDate.getTime())
                .signWith(secretKey)
                .compact();

        return new TokensDto(accessToken, refreshToken);
    }


    private Claims claims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Long id(String token) {
        return Long.parseLong(claims(token).getSubject());
    }

    private Date expirationDate(String token) {
        return claims(token).getExpiration();
    }

    private boolean isTokenValid(String token) {
        return expirationDate(token).after(new Date());
    }

    private Long accessTokenExpirationDateMsInRefreshToken(String token) {
        return claims(token).get(refreshTokenProperty, Long.class);
    }
}
