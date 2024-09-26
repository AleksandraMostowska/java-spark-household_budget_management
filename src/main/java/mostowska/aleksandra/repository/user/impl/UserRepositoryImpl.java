package mostowska.aleksandra.repository.user.impl;

import mostowska.aleksandra.model.User;
import mostowska.aleksandra.repository.user.UserRepository;
import mostowska.aleksandra.repository.generic.AbstractCrudRepository;
import org.jdbi.v3.core.Jdbi;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * UserRepositoryImpl is the implementation of the UserRepository interface.
 * This class provides methods to interact with the User data in the database.
 */
@Repository
public class UserRepositoryImpl extends AbstractCrudRepository<User, Long> implements UserRepository {

    /**
     * Constructs a UserRepositoryImpl with the specified Jdbi instance.
     *
     * @param jdbi The Jdbi instance used for database operations.
     */
    public UserRepositoryImpl(Jdbi jdbi) {
        super(jdbi);
    }

    /**
     * Finds a User by their username.
     *
     * @param username The username of the user to be found.
     * @return An Optional containing the User if found, or empty if not.
     */
    @Override
    public Optional<User> findByUsername(String username) {
        var sql = "select * from users where username = :username";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)  // Creates a query using Jdbi
                .bind("username", username)  // Binds the username parameter to the query
                .mapToBean(User.class)  // Maps the result to the User class
                .findFirst()  // Finds the first result if present
        );
    }

    /**
     * Finds a User by their email address.
     *
     * @param email The email address of the user to be found.
     * @return An Optional containing the User if found, or empty if not.
     */
    @Override
    public Optional<User> findByEmail(String email) {
        var sql = "select * from users where email = :email";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)  // Creates a query using Jdbi
                .bind("email", email)  // Binds the email parameter to the query
                .mapToBean(User.class)  // Maps the result to the User class
                .findFirst()  // Finds the first result if present
        );
    }
}
