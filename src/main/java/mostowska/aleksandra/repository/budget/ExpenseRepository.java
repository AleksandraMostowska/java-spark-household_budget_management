package mostowska.aleksandra.repository.budget;

import mostowska.aleksandra.model.Expense;
import mostowska.aleksandra.repository.generic.CrudRepository;

import java.util.List;

/**
 * ExpenseRepository is an interface for managing Expense entities.
 * It extends the CrudRepository interface to provide basic CRUD operations,
 * along with additional methods specific to Expense entities.
 */
public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    /**
     * Retrieves all expenses for a specific user identified by their user ID.
     *
     * @param userId The ID of the user whose expenses are to be retrieved.
     * @return A list of expenses associated with the specified user ID.
     */
    List<Expense> findAllForUserId(Long userId);
}
