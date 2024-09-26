package mostowska.aleksandra.repository.user;

import mostowska.aleksandra.model.User;
import mostowska.aleksandra.repository.generic.CrudRepository;

import java.util.List;
import java.util.Optional;

/**
 * UserRepository interface that extends the generic CrudRepository.
 * This interface provides methods for accessing User entities in the database.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    /**
     * Finds a User by their username.
     *
     * @param username The username of the user to be found.
     * @return An Optional containing the User if found, or empty if not.
     */
    Optional<User> findByUsername(String username);

    /**
     * Finds a User by their email address.
     *
     * @param email The email address of the user to be found.
     * @return An Optional containing the User if found, or empty if not.
     */
    Optional<User> findByEmail(String email);
}
