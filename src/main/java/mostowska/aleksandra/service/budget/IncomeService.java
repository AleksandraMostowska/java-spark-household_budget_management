package mostowska.aleksandra.service.budget;

import mostowska.aleksandra.model.dto.income.CreateIncomeDto;
import mostowska.aleksandra.model.dto.income.GetIncomeDto;

import java.math.BigDecimal;
import java.util.List;

public interface IncomeService {
    /**
     * Adds a new income record for a specified user.
     *
     * @param createIncomeDto The DTO containing details of the income to be added.
     * @param userId          The ID of the user for whom the income is being recorded.
     * @return GetIncomeDto   The created income data.
     */
    GetIncomeDto addIncome(CreateIncomeDto createIncomeDto, Long userId);

    /**
     * Removes an income record for a specified user.
     *
     * @param incomeId The ID of the income record to be removed.
     * @param userId   The ID of the user who owns the income record.
     * @return GetIncomeDto The removed income data.
     */
    GetIncomeDto removeIncome(Long incomeId, Long userId);

    /**
     * Retrieves all income records for a specified user.
     *
     * @param userId The ID of the user whose income records are to be retrieved.
     * @return A list of income records for the user.
     */
    List<GetIncomeDto> getIncomesByUserId(Long userId);

    /**
     * Sums the total income for a specified user.
     *
     * @param userId The ID of the user whose income is to be summed.
     * @return The total income amount for the user.
     */
    BigDecimal sumUsersIncomes(Long userId);
}
