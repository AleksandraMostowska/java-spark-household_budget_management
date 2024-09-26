package mostowska.aleksandra.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;
import mostowska.aleksandra.model.dto.savings_goal.GetSavingGoalDto;
import mostowska.aleksandra.model.utils.SavingsGoalType;
import mostowska.aleksandra.service.user.UserService;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavingsGoal {
    private Long id;
    private SavingsGoalType goalType;
    private String customGoalType;
    private BigDecimal percentage;
    private Long userId;

    /**
     * Converts the `SavingsGoal` object to a `GetSavingGoalDto`, which contains relevant details about the savings goal.
     *
     * @return a new `GetSavingGoalDto` instance representing this savings goal
     */
    public GetSavingGoalDto toGetSavingGoalDto() {
        return new GetSavingGoalDto(goalType, customGoalType, percentage);
    }

    /**
     * Calculates the amount of savings based on the user's budget and the goal's percentage.
     *
     * @param userService the service used to fetch the user's budget
     * @param userId the ID of the user whose budget is used for the calculation
     * @return the calculated savings as a percentage of the user's budget
     */
    public BigDecimal calculateSavingsFromUsersBudget(UserService userService, Long userId) {
        return userService.getBudget(userId).multiply(percentage);
    }

    /**
     * Validates if the percentage of the savings goal is within the correct range (0 to 1, inclusive).
     *
     * @return true if the percentage is valid, false otherwise
     */
    public boolean hasCorrectPercentage() {
        return percentage.compareTo(BigDecimal.ZERO) >= 0 && percentage.compareTo(BigDecimal.ONE) <= 0;
    }

    /**
     * Checks if this savings goal belongs to a specific user by comparing user IDs.
     *
     * @param userIdToCheck the user ID to check against the savings goal's user ID
     * @return true if the user IDs match, false otherwise
     */
    public boolean hasUserId(Long userIdToCheck) {
        return Objects.equals(userIdToCheck, userId);
    }
}
