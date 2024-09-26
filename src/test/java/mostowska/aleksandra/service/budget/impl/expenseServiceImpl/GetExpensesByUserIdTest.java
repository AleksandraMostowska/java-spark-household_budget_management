package mostowska.aleksandra.service.budget.impl.expenseServiceImpl;

import mostowska.aleksandra.model.Expense;
import mostowska.aleksandra.model.User;
import mostowska.aleksandra.model.utils.ExpenseType;
import mostowska.aleksandra.model.utils.Frequency;
import mostowska.aleksandra.repository.budget.ExpenseRepository;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.user.UserService;
import mostowska.aleksandra.service.budget.impl.ExpenseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GetExpensesByUserIdTest {

    @InjectMocks
    private ExpenseServiceImpl expenseService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private UserService userService;

    private final Long userId = 1L;
    private List<Expense> expenses;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        expenses = new ArrayList<>();
        expenses.add(Expense.builder()
                .id(1L)
                .expenseType(ExpenseType.CUSTOM)
                .customExpenseType("Custom Expense 1")
                .description("Expense 1")
                .amount(BigDecimal.valueOf(50.00))
                .frequency(Frequency.MONTHLY)
                .customFrequency(null)
                .userId(userId)
                .build());
        expenses.add(Expense.builder()
                .id(2L)
                .expenseType(ExpenseType.CUSTOM)
                .customExpenseType("Custom Expense 2")
                .description("Expense 2")
                .amount(BigDecimal.valueOf(25.00))
                .frequency(Frequency.MONTHLY)
                .customFrequency(null)
                .userId(userId)
                .build());
        expenses.add(Expense.builder()
                .id(3L)
                .expenseType(ExpenseType.CUSTOM)
                .customExpenseType("Custom Expense 3")
                .description("Expense 3")
                .amount(BigDecimal.valueOf(30.00))
                .frequency(Frequency.MONTHLY)
                .customFrequency(null)
                .userId(userId)
                .build());
    }

    @Test
    void whenUserExists() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(expenseRepository.findAllForUserId(userId)).thenReturn(expenses);
        var result = expenseService.getExpensesByUserId(userId);
        assertEquals(3, result.size());
        assertEquals("Expense 1", result.get(0).description());
        assertEquals("Expense 2", result.get(1).description());
        assertEquals("Expense 3", result.get(2).description());
    }

    @Test
    void whenUserDoesNotExist() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> expenseService.getExpensesByUserId(userId));
    }
}

