package mostowska.aleksandra.service.budget.impl;

import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.model.Expense;
import mostowska.aleksandra.model.dto.expense.CreateExpenseDto;
import mostowska.aleksandra.model.dto.expense.GetExpenseDto;
import mostowska.aleksandra.repository.budget.ExpenseRepository;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.budget.ExpenseService;
import mostowska.aleksandra.service.user.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {
    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final UserService userService;

    /**
     * Adds a new expense record for a specified user.
     *
     * @param createExpenseDto The DTO containing details of the expense to be added.
     * @param userId          The ID of the user for whom the expense is being recorded.
     * @return GetExpenseDto   The created expense data.
     */
    @Override
    public GetExpenseDto addExpense(CreateExpenseDto createExpenseDto, Long userId) {
        if (createExpenseDto == null) {
            throw new IllegalStateException("Expense cannot be null");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        var expenseToAdd = createExpenseDto.toExpense(userId);
        var insertedExpense = expenseRepository.save(expenseToAdd).toGetExpenseDto();
        userService.cutFromBudget(userId, insertedExpense.amount());
        return insertedExpense;
    }

    /**
     * Removes an expense record for a specified user.
     *
     * @param expenseId The ID of the expense record to be removed.
     * @param userId    The ID of the user who owns the expense record.
     * @return GetExpenseDto The removed expense data.
     */
    @Override
    public GetExpenseDto removeExpense(Long expenseId, Long userId) {
        if (expenseId == null || userId == null) {
            throw new IllegalStateException("Removal failed");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }

        if (!expenseRepository
                .findById(expenseId)
                .orElseThrow(() -> new IllegalStateException("Expense not found"))
                .hasUserId(userId)) {
            throw new IllegalStateException("No such expense found");
        }

        var expenseToRemove = expenseRepository.delete(expenseId).toGetExpenseDto();
        userService.addToBudget(userId, expenseToRemove.amount());
        return expenseToRemove;
    }

    /**
     * Retrieves all expense records for a specified user.
     *
     * @param userId The ID of the user whose expense records are to be retrieved.
     * @return A list of expense records for the user.
     */
    @Override
    public List<GetExpenseDto> getExpensesByUserId(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new IllegalStateException("User not found");
        }
        var expensesFound = expenseRepository.findAllForUserId(userId);
        return expensesFound
                .stream()
                .map(Expense::toGetExpenseDto)
                .collect(Collectors.toList());
    }

    /**
     * Sums the total expenses for a specified user.
     *
     * @param userId The ID of the user whose expenses are to be summed.
     * @return The total expense amount for the user.
     */
    @Override
    public BigDecimal sumUsersExpenses(Long userId) {
        return getExpensesByUserId(userId)
                .stream()
                .map(GetExpenseDto::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
