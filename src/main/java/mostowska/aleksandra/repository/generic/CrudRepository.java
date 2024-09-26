package mostowska.aleksandra.repository.generic;

import java.util.List;
import java.util.Optional;

/**
 * CrudRepository is a generic interface that defines the basic CRUD operations.
 * This interface can be implemented for various entities.
 *
 * @param <T> The type of the entity.
 * @param <ID> The type of the entity's identifier.
 */
public interface CrudRepository<T, ID> {
    /**
     * Saves a new entity in the repository.
     *
     * @param item The entity to be saved.
     * @return The saved entity.
     */
    T save(T item);

    /**
     * Updates an existing entity identified by its ID.
     *
     * @param id The ID of the entity to update.
     * @param item The updated entity data.
     * @return The updated entity.
     */
    T update(ID id, T item);

    /**
     * Saves multiple entities in the repository.
     *
     * @param items The list of entities to be saved.
     * @return The list of saved entities.
     */
    List<T> saveAll(List<T> items);

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity to find.
     * @return An Optional containing the found entity, or empty if not found.
     */
    Optional<T> findById(ID id);

    /**
     * Finds the last 'n' entities in the repository.
     *
     * @param n The number of entities to retrieve.
     * @return A list of the last 'n' entities.
     */
    List<T> findLast(int n);

    /**
     * Retrieves all entities in the repository.
     *
     * @return A list of all entities.
     */
    List<T> findAll();

    /**
     * Finds all entities by a list of their IDs.
     *
     * @param ids The list of IDs of the entities to find.
     * @return A list of found entities.
     */
    List<T> findAllById(List<ID> ids);

    /**
     * Deletes an entity identified by its ID.
     *
     * @param id The ID of the entity to delete.
     * @return The deleted entity.
     */
    T delete(ID id);

    /**
     * Deletes multiple entities identified by their IDs.
     *
     * @param ids The list of IDs of the entities to delete.
     * @return A list of the deleted entities.
     */
    List<T> deleteAllById(List<ID> ids);

    /**
     * Deletes all entities in the repository.
     *
     * @return A list of the deleted entities.
     */
    List<T> deleteAll();
}
