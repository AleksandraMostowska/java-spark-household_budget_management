package mostowska.aleksandra.model.dto.expense;

import mostowska.aleksandra.model.Expense;
import mostowska.aleksandra.model.utils.ExpenseType;
import mostowska.aleksandra.model.utils.Frequency;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) used for creating new expense records.
 * This class carries the necessary data for creating an expense entry.
 *
 * @param expenseType The type of expense being created.
 * @param customExpenseType Optional custom expense type provided by the user.
 * @param description A description of the expense.
 * @param amount The amount of the expense to be recorded.
 * @param frequency The frequency at which the expense is incurred.
 * @param customFrequency An optional custom frequency value.
 */
public record CreateExpenseDto(
        ExpenseType expenseType,
        String customExpenseType,
        String description,
        BigDecimal amount,
        Frequency frequency,
        Long customFrequency
) {
    /**
     * Converts the CreateExpenseDto into an Expense entity.
     *
     * @param userId The ID of the user associated with this expense.
     * @return A new Expense object created from this DTO.
     */
    public Expense toExpense(Long userId) {
        checkCustomFields();

        return Expense
                .builder()
                .expenseType(expenseType)
                .customExpenseType(customExpenseType)
                .description(description)
                .amount(amount)
                .frequency(frequency)
                .customFrequency(customFrequency)
                .userId(userId)
                .build();
    }

    /**
     * Validates custom fields based on the expense type and frequency.
     * Throws an exception if invalid configurations are detected.
     */
    private void checkCustomFields() {
        if (expenseType == ExpenseType.CUSTOM && customExpenseType == null) {
            throw new IllegalStateException("Custom expense type must be provided");
        }
        if (expenseType != ExpenseType.CUSTOM && customExpenseType != null) {
            throw new IllegalStateException("'CUSTOM' must be chosen for handling custom expense type");
        }

        if (frequency == Frequency.CUSTOM && customFrequency == null) {
            throw new IllegalStateException("Custom frequency must be provided");
        }
        if (frequency != Frequency.CUSTOM && customFrequency != null) {
            throw new IllegalStateException("'CUSTOM' must be chosen for handling custom frequency");
        }
    }
}
