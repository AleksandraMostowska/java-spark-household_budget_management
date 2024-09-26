package mostowska.aleksandra.repository.budget;

import mostowska.aleksandra.model.Income;
import mostowska.aleksandra.repository.generic.CrudRepository;

import java.util.List;

/**
 * IncomeRepository is an interface for managing Income entities.
 * It extends the CrudRepository interface to provide basic CRUD operations,
 * along with additional methods specific to Income entities.
 */
public interface IncomeRepository extends CrudRepository<Income, Long> {
    /**
     * Retrieves all incomes for a specific user identified by their user ID.
     *
     * @param userId The ID of the user whose incomes are to be retrieved.
     * @return A list of incomes associated with the specified user ID.
     */
    List<Income> findAllForUserId(Long userId);
}
