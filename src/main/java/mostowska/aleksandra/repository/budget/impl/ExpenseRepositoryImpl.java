package mostowska.aleksandra.repository.budget.impl;

import mostowska.aleksandra.model.Expense;
import mostowska.aleksandra.repository.budget.ExpenseRepository;
import mostowska.aleksandra.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * ExpenseRepositoryImpl provides the implementation for managing Expense entities.
 * It interacts with the database to perform CRUD operations as well as user-specific queries.
 */
@Repository
public class ExpenseRepositoryImpl extends AbstractCrudRepository<Expense, Long> implements ExpenseRepository {

    public ExpenseRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    /**
     * Retrieves all expenses for a specific user identified by their user ID.
     *
     * @param userId The ID of the user whose expenses are to be retrieved.
     * @return A list of expenses associated with the specified user ID.
     */
    @Override
    public List<Expense> findAllForUserId(Long userId) {
        var sql = "select * from expenses where user_id = :user_id";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("user_id", userId)
                .mapToBean(Expense.class)
                .collect(Collectors.toList()));
    }
}
