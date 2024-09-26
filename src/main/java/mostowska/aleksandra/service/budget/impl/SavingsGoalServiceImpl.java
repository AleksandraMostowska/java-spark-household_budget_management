package mostowska.aleksandra.service.budget.impl;

import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.model.SavingsGoal;
import mostowska.aleksandra.model.dto.savings_goal.CreateSavingGoalDto;
import mostowska.aleksandra.model.dto.savings_goal.GetSavingGoalDto;
import mostowska.aleksandra.repository.budget.SavingsGoalRepository;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.budget.SavingsGoalService;
import mostowska.aleksandra.service.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SavingsGoalServiceImpl implements SavingsGoalService {
    private final UserRepository userRepository;
    private final SavingsGoalRepository savingsGoalRepository;
    private final UserService userService;

    /**
     * Adds a new savings goal for a specified user.
     *
     * @param createSavingGoalDto The DTO containing details of the savings goal to be added.
     * @param userId              The ID of the user for whom the savings goal is being created.
     * @return GetSavingGoalDto   The created savings goal data.
     */
    @Override
    public GetSavingGoalDto addSavingGoal(CreateSavingGoalDto createSavingGoalDto, Long userId) {
        if (createSavingGoalDto == null) {
            throw new IllegalStateException("Savings goal cannot be null");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        var goalToAdd = createSavingGoalDto.toSavingsGoal(userId);

        if (!goalToAdd.hasCorrectPercentage()) {
            throw new IllegalStateException("Goal must be in range [0; 1]");
        }

        var insertedGoal = savingsGoalRepository.save(goalToAdd);
        userService.cutFromBudgetAfterGoals(userId, insertedGoal.calculateSavingsFromUsersBudget(userService, userId));
        return insertedGoal.toGetSavingGoalDto();
    }

    /**
     * Removes a savings goal for a specified user.
     *
     * @param goalId The ID of the savings goal to be removed.
     * @param userId The ID of the user who owns the savings goal.
     * @return GetSavingGoalDto The removed savings goal data.
     */
    @Override
    public GetSavingGoalDto removeSavingGoal(Long goalId, Long userId) {
        if (goalId == null || userId == null) {
            throw new IllegalStateException("Removal failed");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        if (!savingsGoalRepository
                .findById(goalId)
                .orElseThrow(() -> new IllegalStateException("Goal not found"))
                .hasUserId(userId)) {
            throw new IllegalStateException("No such goal found");
        }

        var goalToRemove = savingsGoalRepository.delete(goalId);
        userService.addToBudgetAfterGoals(userId, goalToRemove.calculateSavingsFromUsersBudget(userService, userId));
        return goalToRemove.toGetSavingGoalDto();
    }

    /**
     * Retrieves all savings goals for a specified user.
     *
     * @param userId The ID of the user whose savings goals are to be retrieved.
     * @return A list of savings goals for the user.
     */
    @Override
    public List<GetSavingGoalDto> getSavingGoalsByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        var goalsFound = savingsGoalRepository.findAllForUserId(userId);
        return goalsFound
                .stream()
                .map(SavingsGoal::toGetSavingGoalDto)
                .collect(Collectors.toList());
    }

    /**
     * Sums the total amount of savings goals for a specified user.
     *
     * @param userId The ID of the user whose savings goals amounts are to be summed.
     * @return The total savings goal amount for the user.
     */
    @Override
    public BigDecimal sumTotalSavingsGoalsAmount(Long userId) {
        return getSavingGoalsByUserId(userId)
                .stream()
                .map(goal -> goal.percentage().multiply(userService.getBudget(userId)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates the estimated date to achieve a specified savings goal.
     *
     * @param userId      The ID of the user for whom the goal is set.
     * @param goalId      The ID of the savings goal.
     * @param amountToReach The amount the user wants to reach.
     * @return LocalDateTime The estimated date to achieve the goal.
     */
    @Override
    public LocalDateTime getDateToPursueChosenGoal(Long userId, Long goalId, BigDecimal amountToReach) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        if (savingsGoalRepository.findById(goalId).isEmpty()) {
            throw new IllegalStateException("Goal not found");
        }

        var goal = savingsGoalRepository
                .findByUserIdAndGoalId(userId, goalId)
                .orElseThrow(() -> new IllegalStateException("No such goal found"));

        var goalPerMonth = goal.calculateSavingsFromUsersBudget(userService, userId);
        var monthsToReachGoal = amountToReach.divide(goalPerMonth, 0, RoundingMode.UP);

        return LocalDateTime.now().plusMonths(monthsToReachGoal.longValue());
    }
}
