package mostowska.aleksandra.model.dto.expense;

import mostowska.aleksandra.model.utils.ExpenseType;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) representing an expense record.
 * This class is used to encapsulate information about a specific expense.
 *
 * @param expenseType The type of expense (e.g., GROCERIES, RENT).
 * @param description A brief description of the expense.
 * @param amount The monetary amount of the expense.
 */
public record GetExpenseDto(ExpenseType expenseType, String description, BigDecimal amount) {
}
