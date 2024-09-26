package mostowska.aleksandra.service.user.impl.userServiceImpl;

import mostowska.aleksandra.model.User;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.user.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class GetBudgetAfterGoalsTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    private User existingUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        existingUser = User.builder()
                .id(1L)
                .username("user1")
                .email("user1@example.com")
                .password("password1")
                .role(null)
                .budget(BigDecimal.valueOf(100.00))
                .budgetAfterGoals(BigDecimal.valueOf(50.00))
                .enabled(true)
                .build();
    }

    @Test
    void whenUserExists() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        BigDecimal budgetAfterGoals = userService.getBudgetAfterGoals(1L);
        assertEquals(BigDecimal.valueOf(50.00), budgetAfterGoals);
    }

    @Test
    void whenUserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> userService.getBudgetAfterGoals(1L));
    }
}

