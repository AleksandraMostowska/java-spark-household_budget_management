package mostowska.aleksandra.model;

import lombok.*;
import mostowska.aleksandra.model.dto.expense.GetExpenseDto;
import mostowska.aleksandra.model.utils.ExpenseType;
import mostowska.aleksandra.model.utils.Frequency;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Expense {
    private Long id;
    private ExpenseType expenseType;
    private String customExpenseType;
    private String description;
    private BigDecimal amount;
    private Frequency frequency;
    private Long customFrequency;
    private Long userId;

    /**
     * Creates a new `Expense` object with a custom expense type.
     * This can only be done if the expense type is `CUSTOM`,
     * otherwise an exception will be thrown.
     *
     * @param newCustomExpenseType the new custom expense type to set
     * @return a new `Expense` instance with the updated custom expense type
     * @throws IllegalArgumentException if the expense type is not `CUSTOM`
     */
    public Expense withCustomExpenseType(String newCustomExpenseType) {
        if (expenseType != ExpenseType.CUSTOM) {
            throw new IllegalArgumentException("Custom expense type can only be set if the expense type is CUSTOM.");
        }
        return Expense.builder()
                .id(id)
                .expenseType(expenseType)
                .customExpenseType(newCustomExpenseType)
                .description(description)
                .amount(amount)
                .frequency(frequency)
                .customFrequency(customFrequency)
                .userId(userId)
                .build();
    }

    /**
     * Creates a new `Expense` object with a custom frequency.
     * This can only be done if the expense type is `CUSTOM`,
     * otherwise an exception will be thrown.
     *
     * @param newCustomFrequency the new custom frequency to set
     * @return a new `Expense` instance with the updated custom frequency
     * @throws IllegalArgumentException if the expense type is not `CUSTOM`
     */
    public Expense withCustomFrequency(Long newCustomFrequency) {
        if (expenseType != ExpenseType.CUSTOM) {
            throw new IllegalArgumentException("Custom frequency can only be set if the expense type is CUSTOM.");
        }
        return Expense.builder()
                .id(id)
                .expenseType(expenseType)
                .customExpenseType(customExpenseType)
                .description(description)
                .amount(amount)
                .frequency(frequency)
                .customFrequency(newCustomFrequency)
                .userId(userId)
                .build();
    }

    /**
     * Converts the `Expense` object to a `GetExpenseDto`, which contains
     * essential details such as expense type, description, and amount.
     *
     * @return a new `GetExpenseDto` instance representing this expense
     */
    public GetExpenseDto toGetExpenseDto() {
        return new GetExpenseDto(expenseType, description, amount);
    }

    /**
     * Checks if this expense record belongs to a specific user by comparing user IDs.
     *
     * @param userIdToCheck the user ID to check against the expense's user ID
     * @return true if the user IDs match, false otherwise
     */
    public boolean hasUserId(Long userIdToCheck) {
        return Objects.equals(userIdToCheck, userId);
    }
}
