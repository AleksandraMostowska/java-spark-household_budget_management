package mostowska.aleksandra.service.security.impl;

import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.api.security.AuthorizationRole;
import mostowska.aleksandra.service.security.AuthorizationCheckService;
import mostowska.aleksandra.service.security.TokensService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * AuthorizationCheckServiceImpl checks whether a given access token has the appropriate permissions
 * for a specified URI based on the user's role and the defined authorization rules.
 */
@Service
@RequiredArgsConstructor
public class AuthorizationCheckServiceImpl implements AuthorizationCheckService {
    private final TokensService tokensService;

    @Value("${authorization.user.uris}")
    private String userUris;

    @Value("${authorization.admin.uris}")
    private String adminUris;

    @Value("${authorization.is_auth.uris}")
    private String isAuthUris;

    @Value("${authorization.public.uris}")
    private String allUrisProperty;

    private EnumMap<AuthorizationRole, List<String>> authorizationUris;
    private List<String> publicURIs;

    /**
     * Initializes the authorization URIs and public URIs after the bean has been constructed.
     */
    @PostConstruct
    public void init() {
        authorizationUris = new EnumMap<>(Map.of(
                AuthorizationRole.USER, Arrays.asList(userUris.split(",")),
                AuthorizationRole.ADMIN, Arrays.asList(adminUris.split(",")),
                AuthorizationRole.IS_AUTH, Arrays.asList(isAuthUris.split(","))
        ));
        publicURIs = Arrays.asList(allUrisProperty.split(","));
    }

    /**
     * Authorizes a request by checking the access token against the requested URI.
     *
     * @param accessToken The access token provided by the user.
     * @param uri The URI being accessed.
     * @return true if the token is authorized for the URI; false otherwise.
     */
    @Override
    public boolean authorize(String accessToken, String uri) {
        if (accessToken == null) {
            return publicURIs
                    .stream()
                    .anyMatch(u -> (u.endsWith("*") &&
                            uri.startsWith(u.substring(0, u.length() - 1))) ||
                            uri.equals(u));
        }

        if (uri.startsWith("/error") || uri.equals("/auth/refresh")) {
            return true;
        }

        var authorizationDto = tokensService.parseTokens(accessToken);
        var role = authorizationDto.role().toAuthorizationRole();
        var id = authorizationDto.id();

        return containsURI(role, id, uri);
    }


    private boolean containsURI(AuthorizationRole role, Long id, String uriToCheck) {
        return authorizationUris
                .get(role)
                .stream()
                .anyMatch(uri -> checkURI(uri, uriToCheck, id)) ||
                authorizationUris
                        .get(AuthorizationRole.IS_AUTH)
                        .stream()
                        .anyMatch(uri -> checkURI(uri, uriToCheck, id));
    }

    private boolean checkURI(String uri, String uriToCheck, Long id) {
        var processedURI = uri.contains("{id}") ? uri.replace("{id}", id.toString()) : uri;

        return processedURI.endsWith("*")
                ? uriToCheck.startsWith(processedURI.substring(0, processedURI.length() - 1))
                : uriToCheck.equals(processedURI);
    }
}
