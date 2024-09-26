package mostowska.aleksandra.service.dto;

import mostowska.aleksandra.model.utils.Role;

/**
 * AuthorizationDto is a Data Transfer Object that encapsulates
 * the user's ID and role for authorization purposes.
 *
 * @param id   The unique identifier of the user.
 * @param role The role assigned to the user for authorization.
 */
public record AuthorizationDto(Long id, Role role) {
}
