package mostowska.aleksandra.model.utils;

import mostowska.aleksandra.api.security.AuthorizationRole;

/**
 * Enum representing the different user roles within the system.
 * These roles determine the user's level of access and permissions.
 */
public enum Role {
    ADMIN,
    USER;

    /**
     * Converts the enum role to the corresponding authorization role
     * used in security checks.
     *
     * @return the corresponding `AuthorizationRole` based on the role
     */
    public AuthorizationRole toAuthorizationRole() {
        return switch (this) {
            case USER -> AuthorizationRole.USER;
            case ADMIN -> AuthorizationRole.ADMIN;
        };
    }
}
