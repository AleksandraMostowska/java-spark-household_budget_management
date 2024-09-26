package mostowska.aleksandra.repository.budget;

import mostowska.aleksandra.model.AvailableInvestment;
import mostowska.aleksandra.model.Investment;
import mostowska.aleksandra.repository.generic.CrudRepository;

import java.util.List;

/**
 * InvestmentRepository is an interface for managing Investment entities.
 * It extends the CrudRepository interface to provide basic CRUD operations,
 * along with additional methods specific to Investment entities.
 */
public interface InvestmentRepository extends CrudRepository<Investment, Long> {
    /**
     * Retrieves all investments for a specific user identified by their user ID.
     *
     * @param userId The ID of the user whose investments are to be retrieved.
     * @return A list of investments associated with the specified user ID.
     */
    List<Investment> findAllForUserId(Long userId);

    /**
     * Retrieves a list of all available investments.
     *
     * @return A list of available investments.
     */
    List<AvailableInvestment> getAvailable();
}
