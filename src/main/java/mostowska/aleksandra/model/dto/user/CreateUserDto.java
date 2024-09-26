package mostowska.aleksandra.model.dto.user;

import mostowska.aleksandra.model.utils.Role;
import mostowska.aleksandra.model.User;

/**
 * A Data Transfer Object (DTO) used for creating a new user.
 * It contains the necessary fields for user creation including username, email, password, and role.
 *
 * @param username The desired username for the new user.
 * @param email The email address of the new user.
 * @param password The password for the new user.
 * @param passwordConfirmation The confirmation of the password for validation.
 * @param role The role of the new user (ADMIN or USER).
 */
public record CreateUserDto(
        String username,
        String email,
        String password,
        String passwordConfirmation,
        Role role
) {
    /**
     * Converts the CreateUserDto object into a User entity.
     * This method is used to map the DTO to the User domain model.
     *
     * @return a User entity built from the CreateUserDto fields.
     */
    public User toUser() {
        return User
                .builder()
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .enabled(false) // New users are disabled by default until activation.
                .build();
    }
}
