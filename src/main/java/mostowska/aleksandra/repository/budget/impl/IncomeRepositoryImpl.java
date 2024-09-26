package mostowska.aleksandra.repository.budget.impl;

import mostowska.aleksandra.model.Income;
import mostowska.aleksandra.repository.budget.IncomeRepository;
import mostowska.aleksandra.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * IncomeRepositoryImpl provides the implementation for managing Income entities.
 * It interacts with the database to perform CRUD operations as well as user-specific queries.
 */
@Repository
public class IncomeRepositoryImpl extends AbstractCrudRepository<Income, Long> implements IncomeRepository {

    public IncomeRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    /**
     * Retrieves all incomes for a specific user identified by their user ID.
     *
     * @param userId The ID of the user whose incomes are to be retrieved.
     * @return A list of incomes associated with the specified user ID.
     */
    @Override
    public List<Income> findAllForUserId(Long userId) {
        var sql = "select * from incomes where user_id = :user_id";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("user_id", userId)
                .mapToBean(Income.class)
                .collect(Collectors.toList()));
    }
}
