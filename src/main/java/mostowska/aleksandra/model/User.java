package mostowska.aleksandra.model;

import lombok.*;
import mostowska.aleksandra.model.dto.user.GetUserDto;
import mostowska.aleksandra.model.utils.Role;
import mostowska.aleksandra.service.dto.AuthorizationDto;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Role role;
    private BigDecimal budget;
    private BigDecimal budgetAfterGoals;
    private boolean enabled;

    /**
     * Creates a new `User` object with the provided password.
     *
     * @param newPassword the new password to set for the user
     * @return a new `User` instance with the updated password
     */
    public User withPassword(String newPassword) {
        return User
                .builder()
                .id(id)
                .username(username)
                .email(email)
                .password(newPassword)
                .role(role)
                .budget(BigDecimal.ZERO)
                .budgetAfterGoals(BigDecimal.ZERO)
                .enabled(enabled)
                .build();
    }

    /**
     * Creates a new `User` object with the provided enabled status.
     *
     * @param newEnabled the new enabled status for the user
     * @return a new `User` instance with the updated enabled status
     */
    public User withEnabled(boolean newEnabled) {
        return User
                .builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .budget(budget)
                .budgetAfterGoals(budgetAfterGoals)
                .enabled(newEnabled)
                .build();
    }

    /**
     * Adds an amount to the user's budget and returns a new `User` instance with the updated budget.
     *
     * @param amount the amount to add to the current budget
     * @return a new `User` instance with the updated budget
     */
    public User withBudgetAdd(BigDecimal amount) {
        return withNewBudget(budget.add(amount));
    }

    /**
     * Subtracts an amount from the user's budget and returns a new `User` instance with the updated budget.
     *
     * @param amount the amount to subtract from the current budget
     * @return a new `User` instance with the updated budget
     */
    public User withBudgetCut(BigDecimal amount) {
        return withNewBudget(budget.subtract(amount));
    }

    /**
     * Adds an amount to the budget after goals and returns a new `User` instance with the updated budgetAfterGoals.
     *
     * @param amount the amount to add to the budgetAfterGoals
     * @return a new `User` instance with the updated budgetAfterGoals
     */
    public User withBudgetAfterGoalsAdd(BigDecimal amount) {
        return withNewBudgetAfterGoals(budgetAfterGoals.add(amount));
    }

    /**
     * Subtracts an amount from the budget after goals and returns a new `User` instance with the updated budgetAfterGoals.
     *
     * @param amount the amount to subtract from the budgetAfterGoals
     * @return a new `User` instance with the updated budgetAfterGoals
     */
    public User withBudgetAfterGoalsCut(BigDecimal amount) {
        return withNewBudgetAfterGoals(budgetAfterGoals.subtract(amount));
    }

    /**
     * Converts the `User` object into a `GetUserDto`, which contains only the necessary user data.
     *
     * @return a new `GetUserDto` instance representing the current user
     */
    public GetUserDto toGetUserDto() {
        return new GetUserDto(id, username, email, budget, budgetAfterGoals);
    }

    /**
     * Converts the `User` object into an `AuthorizationDto`, containing user id and role.
     *
     * @return a new `AuthorizationDto` instance representing the current user
     */
    public AuthorizationDto toAuthorizationDto() {
        return new AuthorizationDto(id, role);
    }

    /**
     * Checks if the user has enough budget to cover a given asset.
     *
     * @param asset the amount of the asset to compare against the user's budget
     * @return true if the user has enough budget, false otherwise
     */
    public boolean hasEnoughBudgetForAsset(BigDecimal asset) {
        return budget.compareTo(asset) >= 0;
    }

    /**
     * Checks if the provided raw password matches the stored (encoded) password.
     *
     * @param passwordEncoder the encoder used to match the raw password
     * @param rawPassword the raw password to check
     * @return true if the passwords match, false otherwise
     */
    public boolean matchesPassword(PasswordEncoder passwordEncoder, String rawPassword) {
        return passwordEncoder.matches(rawPassword, password);
    }

    /**
     * Helper method to update the user's budget and calculate the budget after goals accordingly.
     *
     * @param newBudget the new budget to set
     * @return a new `User` instance with the updated budget and calculated budgetAfterGoals
     */
    private User withNewBudget(BigDecimal newBudget) {
        var newBudgetAfterGoals = budget.compareTo(budgetAfterGoals) == 0
                ?  newBudget : budgetAfterGoals.divide(budget).multiply(newBudget);
        return User
                .builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .budget(newBudget)
                .budgetAfterGoals(newBudgetAfterGoals)
                .enabled(enabled)
                .build();
    }

    /**
     * Helper method to update the user's budget after goals.
     *
     * @param newBudgetAfterGoals the new budgetAfterGoals to set
     * @return a new `User` instance with the updated budgetAfterGoals
     */
    private User withNewBudgetAfterGoals(BigDecimal newBudgetAfterGoals) {
        return User
                .builder()
                .id(id)
                .username(username)
                .email(email)
                .password(password)
                .role(role)
                .budget(budget)
                .budgetAfterGoals(newBudgetAfterGoals)
                .enabled(enabled)
                .build();
    }
}
