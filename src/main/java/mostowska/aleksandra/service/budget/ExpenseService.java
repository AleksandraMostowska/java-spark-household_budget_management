package mostowska.aleksandra.service.budget;

import mostowska.aleksandra.model.dto.expense.CreateExpenseDto;
import mostowska.aleksandra.model.dto.expense.GetExpenseDto;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseService {
    /**
     * Adds a new expense record for a specified user.
     *
     * @param createExpenseDto The DTO containing details of the expense to be added.
     * @param userId          The ID of the user for whom the expense is being recorded.
     * @return GetExpenseDto   The created expense data.
     */
    GetExpenseDto addExpense(CreateExpenseDto createExpenseDto, Long userId);

    /**
     * Removes an expense record for a specified user.
     *
     * @param expenseId The ID of the expense record to be removed.
     * @param userId    The ID of the user who owns the expense record.
     * @return GetExpenseDto The removed expense data.
     */
    GetExpenseDto removeExpense(Long expenseId, Long userId);

    /**
     * Retrieves all expense records for a specified user.
     *
     * @param userId The ID of the user whose expense records are to be retrieved.
     * @return A list of expense records for the user.
     */
    List<GetExpenseDto> getExpensesByUserId(Long userId);

    /**
     * Sums the total expenses for a specified user.
     *
     * @param userId The ID of the user whose expenses are to be summed.
     * @return The total expense amount for the user.
     */
    BigDecimal sumUsersExpenses(Long userId);
}
