package mostowska.aleksandra.service.user;

import mostowska.aleksandra.model.dto.user.CreateUserDto;
import mostowska.aleksandra.model.dto.user.GetUserDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * UserService defines the contract for user-related operations.
 * It includes methods for user registration, activation, retrieval, and budget management.
 */
public interface UserService {
    GetUserDto register(CreateUserDto createUserDto);
    GetUserDto activate(Long userId, Long expirationTime);
    GetUserDto getUserById(Long userId);
    List<GetUserDto> getAllUsers();
    void addToBudget(Long userId, BigDecimal amount);
    void cutFromBudget(Long userId, BigDecimal amount);
    void addToBudgetAfterGoals(Long userId, BigDecimal amount);
    void cutFromBudgetAfterGoals(Long userId, BigDecimal amount);
    BigDecimal getBudget(Long userId);
    BigDecimal getBudgetAfterGoals(Long userId);
}
