package mostowska.aleksandra.repository.budget.impl;

import mostowska.aleksandra.model.AvailableInvestment;
import mostowska.aleksandra.model.Investment;
import mostowska.aleksandra.repository.budget.InvestmentRepository;
import mostowska.aleksandra.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * InvestmentRepositoryImpl provides the implementation for managing Investment entities.
 * It interacts with the database to perform CRUD operations as well as user-specific queries.
 */
@Repository
public class InvestmentRepositoryImpl extends AbstractCrudRepository<Investment, Long> implements InvestmentRepository {

    public InvestmentRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    /**
     * Retrieves all investments for a specific user identified by their user ID.
     *
     * @param userId The ID of the user whose investments are to be retrieved.
     * @return A list of investments associated with the specified user ID.
     */
    @Override
    public List<Investment> findAllForUserId(Long userId) {
        var sql = "select * from investments where user_id = :user_id";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("user_id", userId)
                .mapToBean(Investment.class)
                .collect(Collectors.toList()));
    }

    /**
     * Retrieves a list of all available investments.
     *
     * @return A list of available investments.
     */
    @Override
    public List<AvailableInvestment> getAvailable() {
        var sql = "select * from available_investments";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(AvailableInvestment.class)
                .collect(Collectors.toList()));
    }
}
