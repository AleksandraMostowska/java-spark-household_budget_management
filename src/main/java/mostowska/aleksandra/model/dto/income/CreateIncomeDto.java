package mostowska.aleksandra.model.dto.income;

import mostowska.aleksandra.model.Income;
import mostowska.aleksandra.model.utils.Frequency;
import mostowska.aleksandra.model.utils.IncomeType;

import java.math.BigDecimal;

/**
 * A Data Transfer Object (DTO) used for creating new income records.
 * This class carries the necessary data for creating an income entry.
 *
 * @param incomeType The type of income being created.
 * @param customIncomeType Optional custom income type provided by the user.
 * @param description A description of the income source.
 * @param amount The amount of income to be recorded.
 * @param frequency The frequency at which the income is received.
 * @param customFrequency An optional custom frequency value.
 */
public record CreateIncomeDto(
        IncomeType incomeType,
        String customIncomeType,
        String description,
        BigDecimal amount,
        Frequency frequency,
        Long customFrequency
) {
    /**
     * Converts the CreateIncomeDto into an Income entity.
     *
     * @param userId The ID of the user associated with this income.
     * @return A new Income object created from this DTO.
     */
    public Income toIncome(Long userId) {
        checkCustomFields();
        return Income
                .builder()
                .incomeType(incomeType)
                .customIncomeType(customIncomeType)
                .description(description)
                .amount(amount)
                .frequency(frequency)
                .customFrequency(customFrequency)
                .userId(userId)
                .build();
    }

    /**
     * Validates custom fields based on the income type and frequency.
     * Throws an exception if invalid configurations are detected.
     */
    private void checkCustomFields() {
        if (incomeType == IncomeType.CUSTOM && customIncomeType == null) {
            throw new IllegalStateException("Custom income type must be provided");
        }
        if (incomeType != IncomeType.CUSTOM && customIncomeType != null) {
            throw new IllegalStateException("'CUSTOM' must be chosen for handling custom income type");
        }

        if (frequency == Frequency.CUSTOM && customFrequency == null) {
            throw new IllegalStateException("Custom frequency must be provided");
        }
        if (frequency != Frequency.CUSTOM && customFrequency != null) {
            throw new IllegalStateException("'CUSTOM' must be chosen for handling custom frequency");
        }
    }
}
