package mostowska.aleksandra.service.user.impl.userServiceImpl;

import mostowska.aleksandra.model.User;
import mostowska.aleksandra.model.dto.user.GetUserDto;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.user.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class GetUserByIdTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private GetUserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .budget(BigDecimal.valueOf(100.00))
                .budgetAfterGoals(BigDecimal.valueOf(90.00))
                .enabled(true)
                .build();
        userDto = user.toGetUserDto();
    }

    @Test
    void whenUserExists() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        var result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(userDto.id(), result.id());
        assertEquals(userDto.username(), result.username());
        assertEquals(userDto.email(), result.email());
        assertEquals(userDto.budget(), result.budget());
        assertEquals(userDto.budgetAfterGoals(), result.budgetAfterGoals());
    }

    @Test
    void whenUserDoesNotExist() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> userService.getUserById(1L));
    }
}

