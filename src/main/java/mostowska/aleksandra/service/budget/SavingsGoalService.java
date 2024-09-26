package mostowska.aleksandra.service.budget;

import mostowska.aleksandra.model.dto.savings_goal.CreateSavingGoalDto;
import mostowska.aleksandra.model.dto.savings_goal.GetSavingGoalDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface SavingsGoalService {
    /**
     * Adds a new savings goal for a specified user.
     *
     * @param createSavingGoalDto The DTO containing details of the savings goal to be created.
     * @param userId              The ID of the user for whom the savings goal is being added.
     * @return GetSavingGoalDto   The created savings goal data.
     */
    GetSavingGoalDto addSavingGoal(CreateSavingGoalDto createSavingGoalDto, Long userId);

    /**
     * Removes a savings goal for a specified user.
     *
     * @param goalId The ID of the savings goal to be removed.
     * @param userId The ID of the user who owns the savings goal.
     * @return GetSavingGoalDto The removed savings goal data.
     */
    GetSavingGoalDto removeSavingGoal(Long goalId, Long userId);

    /**
     * Retrieves all savings goals for a specified user.
     *
     * @param userId The ID of the user whose savings goals are to be retrieved.
     * @return A list of savings goals for the user.
     */
    List<GetSavingGoalDto> getSavingGoalsByUserId(Long userId);

    /**
     * Sums the total amount of all savings goals for a specified user.
     *
     * @param userId The ID of the user whose savings goals are to be summed.
     * @return The total amount of savings goals for the user.
     */
    BigDecimal sumTotalSavingsGoalsAmount(Long userId);

    /**
     * Calculates the date by which a user should pursue a chosen savings goal.
     *
     * @param userId         The ID of the user.
     * @param goalId         The ID of the savings goal.
     * @param amountToReach  The target amount to reach for the savings goal.
     * @return The date by which the user should achieve the savings goal.
     */
    LocalDateTime getDateToPursueChosenGoal(Long userId, Long goalId, BigDecimal amountToReach);
}
