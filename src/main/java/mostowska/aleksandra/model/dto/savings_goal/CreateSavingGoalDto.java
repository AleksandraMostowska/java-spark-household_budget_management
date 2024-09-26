package mostowska.aleksandra.model.dto.savings_goal;

import mostowska.aleksandra.model.SavingsGoal;
import mostowska.aleksandra.model.utils.SavingsGoalType;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) used for creating a new savings goal.
 * It contains the necessary fields for creating a new goal, such as goal type, custom goal type (if applicable), and the savings percentage.
 *
 * @param goalType The type of the savings goal.
 * @param customGoalType The custom name of the savings goal if the type is CUSTOM.
 * @param percentage The percentage of the user’s budget allocated to this savings goal.
 */
public record CreateSavingGoalDto(SavingsGoalType goalType, String customGoalType, BigDecimal percentage) {

    /**
     * Converts the CreateSavingGoalDto into a SavingsGoal entity.
     * This method builds a SavingsGoal using the provided user ID.
     *
     * @param userId The ID of the user who owns this savings goal.
     * @return A SavingsGoal entity populated with the data from this DTO.
     */
    public SavingsGoal toSavingsGoal(Long userId) {
        checkCustomFields();

        return SavingsGoal
                .builder()
                .goalType(goalType)
                .customGoalType(customGoalType)
                .percentage(percentage)
                .userId(userId)
                .build();
    }

    /**
     * Validates the consistency between the goalType and customGoalType fields.
     * If the goalType is CUSTOM, a custom goal name must be provided.
     * If the goalType is not CUSTOM, no custom goal name should be present.
     *
     * @throws IllegalStateException if validation fails.
     */
    private void checkCustomFields() {
        if (goalType == SavingsGoalType.CUSTOM && customGoalType == null) {
            throw new IllegalStateException("Custom goal type must be provided");
        }
        if (goalType != SavingsGoalType.CUSTOM && customGoalType != null) {
            throw new IllegalStateException("'CUSTOM' must be chosen for handling custom goal type");
        }
    }
}
