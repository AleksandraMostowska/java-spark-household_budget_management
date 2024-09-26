package mostowska.aleksandra.repository.budget.impl;

import mostowska.aleksandra.model.SavingsGoal;
import mostowska.aleksandra.repository.budget.SavingsGoalRepository;
import mostowska.aleksandra.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SavingsGoalRepositoryImpl provides the implementation for managing SavingsGoal entities.
 * It interacts with the database to perform CRUD operations as well as user-specific queries.
 */
@Repository
public class SavingsGoalRepositoryImpl extends AbstractCrudRepository<SavingsGoal, Long> implements SavingsGoalRepository {

    public SavingsGoalRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    /**
     * Retrieves all savings goals for a specific user identified by their user ID.
     *
     * @param userId The ID of the user whose savings goals are to be retrieved.
     * @return A list of savings goals associated with the specified user ID.
     */
    @Override
    public List<SavingsGoal> findAllForUserId(Long userId) {
        var sql = "select * from savings_goals where user_id = :user_id";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("user_id", userId)
                .mapToBean(SavingsGoal.class)
                .collect(Collectors.toList()));
    }

    /**
     * Retrieves a specific savings goal for a user by user ID and goal ID.
     *
     * @param userId The ID of the user whose goal is to be retrieved.
     * @param goalId The ID of the savings goal to be retrieved.
     * @return An Optional containing the found savings goal, or empty if not found.
     */
    @Override
    public Optional<SavingsGoal> findByUserIdAndGoalId(Long userId, Long goalId) {
        var sql = "select * from savings_goals where user_id = :user_id and id = :id";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("user_id", userId)
                .bind("id", goalId)
                .mapToBean(SavingsGoal.class)
                .findFirst());
    }
}
