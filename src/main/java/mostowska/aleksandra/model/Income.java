package mostowska.aleksandra.model;

import lombok.*;
import mostowska.aleksandra.model.dto.income.GetIncomeDto;
import mostowska.aleksandra.model.utils.Frequency;
import mostowska.aleksandra.model.utils.IncomeType;

import java.math.BigDecimal;
import java.util.Objects;

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Income {
    private Long id;
    private IncomeType incomeType;
    private String customIncomeType;
    private String description;
    private BigDecimal amount;
    private Frequency frequency;
    private Long customFrequency;
    private Long userId;

    /**
     * Creates a new `Income` object with a custom income type. The income type will be set to `IncomeType.CUSTOM`
     * and the custom income type will be updated.
     *
     * @param newCustomIncomeType the new custom income type to be set
     * @return a new `Income` instance with the updated custom income type
     */
    public Income withCustomIncomeType(String newCustomIncomeType) {
        return Income.builder()
                .id(id)
                .incomeType(IncomeType.CUSTOM)
                .description(description)
                .amount(amount)
                .frequency(frequency)
                .customIncomeType(newCustomIncomeType)
                .customFrequency(customFrequency)
                .userId(userId)
                .build();
    }

    /**
     * Creates a new `Income` object with an updated custom frequency.
     *
     * @param newCustomFrequency the new custom frequency value to set
     * @return a new `Income` instance with the updated custom frequency
     */
    public Income withCustomFrequency(Long newCustomFrequency) {
        return Income.builder()
                .id(id)
                .incomeType(incomeType)
                .description(description)
                .amount(amount)
                .frequency(frequency)
                .customIncomeType(customIncomeType)
                .customFrequency(newCustomFrequency)
                .userId(userId)
                .build();
    }

    /**
     * Converts the `Income` object to a `GetIncomeDto`, which contains essential information about the income.
     *
     * @return a new `GetIncomeDto` instance representing this income
     */
    public GetIncomeDto toGetIncomeDto() {
        return new GetIncomeDto(id, incomeType, amount);
    }

    /**
     * Checks if this income record belongs to a specific user by comparing user IDs.
     *
     * @param userIdToCheck the user ID to check against the income's user ID
     * @return true if the user IDs match, false otherwise
     */
    public boolean hasUserId(Long userIdToCheck) {
        return Objects.equals(userIdToCheck, userId);
    }
}
