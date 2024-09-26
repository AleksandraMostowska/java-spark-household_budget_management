package mostowska.aleksandra.service.user.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mostowska.aleksandra.model.User;
import mostowska.aleksandra.model.dto.user.CreateUserDto;
import mostowska.aleksandra.model.dto.user.GetUserDto;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.service.email.EmailService;
import mostowska.aleksandra.service.user.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UserServiceImpl provides the implementation of user-related operations defined in the UserService interface.
 * It includes functionalities for user registration, activation, retrieval, and budget management.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Value("${password.registration.timestamp}")
    private long registrationTimestamp;

    /**
     * Registers a new user with the provided user details.
     * Validates the uniqueness of username and email before creating the user.
     *
     * @param createUserDto Data Transfer Object containing user registration information.
     * @return Data Transfer Object representing the registered user.
     */
    @Override
    public GetUserDto register(CreateUserDto createUserDto) {
        if (createUserDto == null) {
            throw new IllegalStateException("Required data not provided");
        }

        var username = createUserDto.username();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already exists");
        }

        var email = createUserDto.email();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalStateException("Email already exists");
        }

        var password = createUserDto.password();
        var userToRegister = createUserDto
                .toUser()
                .withPassword(passwordEncoder.encode(password));

        var insertedUser = userRepository.save(userToRegister);

        // Generate activation link with expiration timestamp
        var timestamp = LocalDateTime
                .now()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli() + registrationTimestamp;

        // Send activation email
        emailService.send(
                insertedUser.toGetUserDto().email(),
                "Activate your account",
                "Activate your account: http://localhost:8080/users/activate?id="
                        + insertedUser.toGetUserDto().id()
                        + "&timestamp="
                        + timestamp
        );

        return insertedUser.toGetUserDto();
    }

    /**
     * Activates a user account based on user ID and checks the expiration time of the activation link.
     *
     * @param userId        The ID of the user to be activated.
     * @param expirationTime The timestamp until which the activation link is valid.
     * @return Data Transfer Object representing the activated user.
     */
    @Override
    public GetUserDto activate(Long userId, Long expirationTime) {
        if (userId == null) {
            throw new IllegalArgumentException("User id is null");
        }
        if (expirationTime == null) {
            throw new IllegalArgumentException("Expiration time is null");
        }

        var currentTime = LocalDateTime
                .now()
                .toInstant(ZoneOffset.UTC)
                .toEpochMilli();
        if (expirationTime < currentTime) {
            throw new IllegalStateException("Activation link expired");
        }

        return userRepository
                .findById(userId)
                .map(user -> userRepository
                        .update(userId, user.withEnabled(true))
                        .toGetUserDto()
                )
                .orElseThrow(() -> new IllegalStateException("No user found with given id"));
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId The ID of the user to be retrieved.
     * @return Data Transfer Object representing the retrieved user.
     */
    @Override
    public GetUserDto getUserById(Long userId) {
        return findUserFromDB(userId)
                .toGetUserDto();
    }

    /**
     * Retrieves all users in the system.
     *
     * @return A list of Data Transfer Objects representing all users.
     */
    @Override
    public List<GetUserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(User::toGetUserDto)
                .collect(Collectors.toList());
    }

    /**
     * Adds a specified amount to the user's budget.
     *
     * @param userId The ID of the user whose budget is to be updated.
     * @param amount The amount to be added.
     */
    @Override
    public void addToBudget(Long userId, BigDecimal amount) {
        var userToBeInserted = findUserFromDB(userId)
                .withBudgetAdd(amount);
        userRepository.update(userId, userToBeInserted);
    }

    /**
     * Cuts a specified amount from the user's budget.
     *
     * @param userId The ID of the user whose budget is to be updated.
     * @param amount The amount to be cut.
     */
    @Override
    public void cutFromBudget(Long userId, BigDecimal amount) {
        var userToBeInserted = findUserFromDB(userId)
                .withBudgetCut(amount);
        userRepository.update(userId, userToBeInserted);
    }

    /**
     * Adds a specified amount to the user's budget after goals.
     *
     * @param userId The ID of the user whose budget is to be updated.
     * @param amount The amount to be added.
     */
    @Override
    public void addToBudgetAfterGoals(Long userId, BigDecimal amount) {
        var userToBeInserted = findUserFromDB(userId)
                .withBudgetAfterGoalsAdd(amount);
        userRepository.update(userId, userToBeInserted);
    }

    /**
     * Cuts a specified amount from the user's budget after goals.
     *
     * @param userId The ID of the user whose budget is to be updated.
     * @param amount The amount to be cut.
     */
    @Override
    public void cutFromBudgetAfterGoals(Long userId, BigDecimal amount) {
        var userToBeInserted = findUserFromDB(userId)
                .withBudgetAfterGoalsCut(amount);
        userRepository.update(userId, userToBeInserted);
    }

    /**
     * Retrieves the current budget of a user.
     *
     * @param userId The ID of the user whose budget is to be retrieved.
     * @return The current budget of the user.
     */
    @Override
    public BigDecimal getBudget(Long userId) {
        return findUserFromDB(userId).toGetUserDto().budget();
    }

    /**
     * Retrieves the budget of a user after goals have been considered.
     *
     * @param userId The ID of the user whose budget after goals is to be retrieved.
     * @return The budget after goals for the user.
     */
    @Override
    public BigDecimal getBudgetAfterGoals(Long userId) {
        return findUserFromDB(userId).toGetUserDto().budgetAfterGoals();
    }

    /**
     * Helper method to find a user by their ID from the database.
     * Throws an exception if the user is not found.
     *
     * @param userId The ID of the user to be retrieved.
     * @return The User entity if found.
     */
    private User findUserFromDB(Long userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new IllegalStateException("No user of given id found"));
    }
}
