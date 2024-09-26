package mostowska.aleksandra.service.budget.impl;

import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.model.AvailableInvestment;
import mostowska.aleksandra.model.Investment;
import mostowska.aleksandra.model.dto.investment.CreateInvestmentDto;
import mostowska.aleksandra.model.dto.investment.GetAvailableInvestmentDto;
import mostowska.aleksandra.model.dto.investment.GetInvestmentDto;
import mostowska.aleksandra.repository.budget.InvestmentRepository;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.budget.InvestmentService;
import mostowska.aleksandra.service.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {
    private final UserRepository userRepository;
    private final InvestmentRepository investmentRepository;
    private final UserService userService;

    /**
     * Adds a new investment for a specified user.
     *
     * @param createInvestmentDto The DTO containing details of the investment to be added.
     * @param userId              The ID of the user for whom the investment is being created.
     * @return GetInvestmentDto   The created investment data.
     */
    @Override
    public GetInvestmentDto addInvestment(CreateInvestmentDto createInvestmentDto, Long userId) {
        if (createInvestmentDto == null) {
            throw new IllegalStateException("Investment cannot be null");
        }
        if (!createInvestmentDto.isAssetType()) {
            throw new IllegalStateException("Wrong investment type - please choose from available.");
        }

        var user = userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (!user.hasEnoughBudgetForAsset(createInvestmentDto.amountInvested())) {
            throw new IllegalStateException("Not enough budget for chosen asset");
        }

        var investmentToAdd = createInvestmentDto.toInvestment(userId);
        var insertedInvestment = investmentRepository.save(investmentToAdd).toGetInvestmentDto();
        userService.cutFromBudget(userId, insertedInvestment.amountInvested());
        return insertedInvestment;
    }

    /**
     * Removes an investment for a specified user.
     *
     * @param investmentId The ID of the investment to be removed.
     * @param userId       The ID of the user who owns the investment.
     * @return GetInvestmentDto The removed investment data.
     */
    @Override
    public GetInvestmentDto removeInvestment(Long investmentId, Long userId) {
        if (investmentId == null || userId == null) {
            throw new IllegalStateException("Removal failed");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        if (!investmentRepository
                .findById(investmentId)
                .orElseThrow(() -> new IllegalStateException("Investment not found"))
                .hasUserId(userId)) {
            throw new IllegalStateException("No such investment found");
        }

        var investmentToRemove = investmentRepository.delete(investmentId).toGetInvestmentDto();
        userService.addToBudget(userId, investmentToRemove.currentValue());
        return investmentToRemove;
    }

    /**
     * Retrieves all investments for a specified user.
     *
     * @param userId The ID of the user whose investments are to be retrieved.
     * @return A list of investments for the user.
     */
    @Override
    public List<GetInvestmentDto> getInvestmentsByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        var investmentsFound = investmentRepository.findAllForUserId(userId);
        return investmentsFound
                .stream()
                .map(Investment::toGetInvestmentDto)
                .collect(Collectors.toList());
    }

    /**
     * Sums the total amount of investments for a specified user.
     *
     * @param userId The ID of the user whose investments amounts are to be summed.
     * @return The total investment amount for the user.
     */
    @Override
    public BigDecimal sumUsersInvestments(Long userId) {
        return getInvestmentsByUserId(userId)
                .stream()
                .map(GetInvestmentDto::amountInvested)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Retrieves a list of available investments.
     *
     * @return A list of available investment options.
     */
    @Override
    public List<GetAvailableInvestmentDto> showAvailableInvestments() {
        return investmentRepository
                .getAvailable()
                .stream()
                .map(AvailableInvestment::toGetAvailableInvestmentDto)
                .collect(Collectors.toList());
    }
}
