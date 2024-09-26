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
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GetAllUsersTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private List<User> userList;
    private List<GetUserDto> userDtoList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userList = List.of(
                new User(1L, "user1", "user1@example.com", "password1", null,
                        BigDecimal.ZERO, BigDecimal.ZERO, true),
                new User(2L, "user2", "user2@example.com", "password2", null,
                        BigDecimal.ZERO, BigDecimal.ZERO, true),
                new User(3L, "user3", "user3@example.com", "password3", null,
                        BigDecimal.ZERO, BigDecimal.ZERO, true)
        );

        userDtoList = userList.stream()
                .map(User::toGetUserDto)
                .collect(Collectors.toList());
    }

    @Test
    void whenUsersExist() {
        when(userRepository.findAll()).thenReturn(userList);
        var result = userService.getAllUsers();

        assertNotNull(result);
        assertEquals(userDtoList.size(), result.size());

        for (int i = 0; i < userDtoList.size(); i++) {
            assertEquals(userDtoList.get(i).id(), result.get(i).id());
            assertEquals(userDtoList.get(i).username(), result.get(i).username());
            assertEquals(userDtoList.get(i).email(), result.get(i).email());
            assertEquals(userDtoList.get(i).budget(), result.get(i).budget());
            assertEquals(userDtoList.get(i).budgetAfterGoals(), result.get(i).budgetAfterGoals());
        }
    }

    @Test
    void whenNoUsersExist() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<GetUserDto> result = userService.getAllUsers();

        assertNotNull(result);
        assertTrue(result.isEmpty(), "The result list should be empty");
    }
}


