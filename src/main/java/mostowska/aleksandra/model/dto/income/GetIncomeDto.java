package mostowska.aleksandra.model.dto.income;

import mostowska.aleksandra.model.utils.IncomeType;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) representing income details.
 * This class is used to encapsulate information about a specific income source.
 *
 * @param id The unique identifier for the income record.
 * @param incomeType The type of income (e.g., SALARY, BONUS, RENTAL).
 * @param amount The amount of income received.
 */
public record GetIncomeDto(Long id, IncomeType incomeType, BigDecimal amount) {
}
