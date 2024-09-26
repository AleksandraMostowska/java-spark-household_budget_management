package mostowska.aleksandra.service.budget.impl;

import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.model.Income;
import mostowska.aleksandra.model.dto.income.CreateIncomeDto;
import mostowska.aleksandra.model.dto.income.GetIncomeDto;
import mostowska.aleksandra.repository.budget.IncomeRepository;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.budget.IncomeService;
import mostowska.aleksandra.service.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {
    private final UserRepository userRepository;
    private final IncomeRepository incomeRepository;
    private final UserService userService;

    /**
     * Adds a new income record for a specified user.
     *
     * @param createIncomeDto The DTO containing details of the income to be added.
     * @param userId          The ID of the user for whom the income is being created.
     * @return GetIncomeDto   The created income data.
     */
    @Override
    public GetIncomeDto addIncome(CreateIncomeDto createIncomeDto, Long userId) {
        if (createIncomeDto == null) {
            throw new IllegalStateException("Income cannot be null");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        var incomeToAdd = createIncomeDto.toIncome(userId);
        var insertedIncome = incomeRepository.save(incomeToAdd).toGetIncomeDto();
        userService.addToBudget(userId, insertedIncome.amount());
        return insertedIncome;
    }

    /**
     * Removes an income record for a specified user.
     *
     * @param incomeId The ID of the income record to be removed.
     * @param userId   The ID of the user who owns the income record.
     * @return GetIncomeDto The removed income data.
     */
    @Override
    public GetIncomeDto removeIncome(Long incomeId, Long userId) {
        if (incomeId == null || userId == null) {
            throw new IllegalStateException("Removal failed");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        if (!incomeRepository
                .findById(incomeId)
                .orElseThrow(() -> new IllegalStateException("Income not found"))
                .hasUserId(userId)) {
            throw new IllegalStateException("No such income found");
        }

        var incomeToRemove = incomeRepository.delete(incomeId).toGetIncomeDto();
        userService.cutFromBudget(userId, incomeToRemove.amount());
        return incomeToRemove;
    }

    /**
     * Retrieves all income records for a specified user.
     *
     * @param userId The ID of the user whose income records are to be retrieved.
     * @return A list of income records for the user.
     */
    @Override
    public List<GetIncomeDto> getIncomesByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        var incomesFound = incomeRepository.findAllForUserId(userId);
        return incomesFound
                .stream()
                .map(Income::toGetIncomeDto)
                .collect(Collectors.toList());
    }

    /**
     * Sums the total amount of incomes for a specified user.
     *
     * @param userId The ID of the user whose income amounts are to be summed.
     * @return The total income amount for the user.
     */
    @Override
    public BigDecimal sumUsersIncomes(Long userId) {
        return getIncomesByUserId(userId)
                .stream()
                .map(GetIncomeDto::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
