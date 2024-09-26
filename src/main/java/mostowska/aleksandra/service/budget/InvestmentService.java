package mostowska.aleksandra.service.budget;

import mostowska.aleksandra.model.dto.investment.CreateInvestmentDto;
import mostowska.aleksandra.model.dto.investment.GetAvailableInvestmentDto;
import mostowska.aleksandra.model.dto.investment.GetInvestmentDto;

import java.math.BigDecimal;
import java.util.List;

public interface InvestmentService {
    /**
     * Adds a new investment for a specified user.
     *
     * @param createInvestmentDto The DTO containing details of the investment to be created.
     * @param userId             The ID of the user for whom the investment is being added.
     * @return GetInvestmentDto   The created investment data.
     */
    GetInvestmentDto addInvestment(CreateInvestmentDto createInvestmentDto, Long userId);

    /**
     * Removes an investment for a specified user.
     *
     * @param investmentId The ID of the investment to be removed.
     * @param userId      The ID of the user who owns the investment.
     * @return GetInvestmentDto The removed investment data.
     */
    GetInvestmentDto removeInvestment(Long investmentId, Long userId);

    /**
     * Retrieves all investments for a specified user.
     *
     * @param userId The ID of the user whose investments are to be retrieved.
     * @return A list of investments for the user.
     */
    List<GetInvestmentDto> getInvestmentsByUserId(Long userId);

    /**
     * Sums the total amount of all investments for a specified user.
     *
     * @param userId The ID of the user whose investments are to be summed.
     * @return The total amount of investments for the user.
     */
    BigDecimal sumUsersInvestments(Long userId);

    /**
     * Retrieves a list of available investments.
     *
     * @return A list of available investments.
     */
    List<GetAvailableInvestmentDto> showAvailableInvestments();
}
