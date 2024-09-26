package mostowska.aleksandra.model.dto.user;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) representing basic user information.
 * This DTO is used to transfer user data in a simple and immutable format.
 *
 * @param id The unique identifier of the user.
 * @param username The username of the user.
 * @param email The email address of the user.
 * @param budget The current budget of the user.
 * @param budgetAfterGoals The budget remaining after accounting for savings goals.
 */
public record GetUserDto (Long id, String username, String email, BigDecimal budget, BigDecimal budgetAfterGoals) { }
