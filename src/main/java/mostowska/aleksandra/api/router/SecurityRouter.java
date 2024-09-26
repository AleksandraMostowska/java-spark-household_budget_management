package mostowska.aleksandra.api.router;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mostowska.aleksandra.api.dto.ResponseDto;
import mostowska.aleksandra.model.dto.user.GetUserDto;
import mostowska.aleksandra.service.security.AuthorizationCheckService;
import mostowska.aleksandra.service.security.TokensService;
import mostowska.aleksandra.service.dto.AuthenticationDto;
import mostowska.aleksandra.service.dto.RefreshTokenDto;
import org.springframework.stereotype.Component;
import spark.ResponseTransformer;

import java.math.BigDecimal;

import static spark.Spark.*;

/**
 * SecurityRouter handles all routes related to authentication and authorization.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityRouter {
    private final TokensService tokensService;
    private final AuthorizationCheckService authorizationCheckService;
    private final ResponseTransformer responseTransformer;
    private final Gson gson;

    /**
     * Defines the routes for authentication and user information.
     */
    public void routes() {
        // Middleware to check authorization for each request
        before((request, response) -> {
            System.out.println("COOKIE ACCESS TOKEN: " + request.cookie("accessToken"));
            System.out.println(request.cookies());
            if (!authorizationCheckService.authorize(
                    request.cookie("accessToken"),
                    request.uri())) {
                halt(403, "Access denied!");
            }
        });

        path("/auth", () -> {
            post(
                    "/login",
                    (request, response) -> {
                        var authenticationDto = gson.fromJson(
                                request.body(),
                                AuthenticationDto.class
                        );
                        var tokens = tokensService
                                .generateToken(authenticationDto);

                        Utils.setResponse(response, 200);
                        response.cookie(
                                "accessToken",
                                tokens.accessToken(),
                                1000000,
                                false,
                                true);
                        response.cookie(
                                "refreshToken",
                                tokens.refreshToken(),
                                1000000,
                                false,
                                true);
                        return new ResponseDto<>(tokens);
                    },
                    responseTransformer
            );

            post(
                    "/refresh",
                    (request, response) -> {
                        System.out.println("INSIDE REFRESH");
                        var refreshTokenDto = gson.fromJson(
                                request.body(),
                                RefreshTokenDto.class
                        );
                        var tokens = tokensService
                                .refreshTokens(refreshTokenDto);

                        Utils.setResponse(response, 200);
                        response.cookie(
                                "accessToken",
                                tokens.accessToken(),
                                1000000,
                                false,
                                true);
                        response.cookie(
                                "refreshToken",
                                tokens.refreshToken(),
                                1000000,
                                false,
                                true);
                        return new ResponseDto<>(tokens);
                    },
                    responseTransformer
            );
        });

        path("/api", () -> {
            get(
                    "/user/info",
                    (request, response) -> {
                        Utils.setResponse(response, 200);
                        return new ResponseDto<>(new GetUserDto(1L, "USER", "user@email.com",
                                BigDecimal.ZERO, BigDecimal.ZERO));
                    },
                    responseTransformer
            );
            get(
                    "/admin/info",
                    (request, response) -> {
                        Utils.setResponse(response, 200);
                        return new ResponseDto<>(new GetUserDto(1L, "ADMIN", "admin@email.com",
                                BigDecimal.ZERO, BigDecimal.ZERO));
                    },
                    responseTransformer
            );
            get(
                    "/is_auth",
                    (request, response) -> {
                        Utils.setResponse(response, 200);
                        return new ResponseDto<>(new GetUserDto(1L, "AUTH", "auth@email.com",
                                BigDecimal.ZERO, BigDecimal.ZERO));
                    },
                    responseTransformer
            );
        });
    }
}
