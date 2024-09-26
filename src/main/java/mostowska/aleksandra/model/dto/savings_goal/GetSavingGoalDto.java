package mostowska.aleksandra.model.dto.savings_goal;

import mostowska.aleksandra.model.utils.SavingsGoalType;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) used for returning information about a savings goal.
 * This DTO carries basic information regarding the type of savings goal, any custom goal type, and the associated percentage.
 *
 * @param goalType The type of the savings goal (e.g., VACATION, CAR, RETIREMENT, etc.).
 * @param customGoalType The custom name of the savings goal if the type is set to CUSTOM.
 * @param percentage The percentage of the user’s budget allocated to this savings goal.
 */
public record GetSavingGoalDto(SavingsGoalType goalType, String customGoalType, BigDecimal percentage) {
}
