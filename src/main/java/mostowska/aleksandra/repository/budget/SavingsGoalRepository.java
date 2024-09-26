package mostowska.aleksandra.repository.budget;

import mostowska.aleksandra.model.SavingsGoal;
import mostowska.aleksandra.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * SavingsGoalRepository is an interface for managing SavingsGoal entities.
 * It extends the CrudRepository interface to provide basic CRUD operations,
 * along with additional methods specific to SavingsGoal entities.
 */
public interface SavingsGoalRepository extends CrudRepository<SavingsGoal, Long> {
    /**
     * Retrieves all savings goals for a specific user identified by their user ID.
     *
     * @param userId The ID of the user whose savings goals are to be retrieved.
     * @return A list of savings goals associated with the specified user ID.
     */
    List<SavingsGoal> findAllForUserId(Long userId);

    /**
     * Retrieves a savings goal for a specific user identified by their user ID
     * and goal ID.
     *
     * @param userId The ID of the user whose goal is to be retrieved.
     * @param goalId The ID of the savings goal to be retrieved.
     * @return An Optional containing the found savings goal, or empty if not found.
     */
    Optional<SavingsGoal> findByUserIdAndGoalId(Long userId, Long goalId);
}
