package mostowska.aleksandra.repository.generic;

import com.google.common.base.CaseFormat;
import lombok.RequiredArgsConstructor;
import mostowska.aleksandra.model.utils.*;
import org.atteo.evo.inflector.English;
import org.jdbi.v3.core.Jdbi;

import java.lang.reflect.ParameterizedType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.joining;

/**
 * AbstractCrudRepository is an abstract implementation of the CrudRepository interface.
 * It provides the basic functionality for CRUD operations using Jdbi.
 *
 * @param <T> The type of the entity.
 * @param <ID> The type of the entity's identifier.
 */
@RequiredArgsConstructor
public abstract class AbstractCrudRepository<T, ID> implements CrudRepository<T, ID> {
    protected final Jdbi jdbi;

    @SuppressWarnings("unchecked")
    private final Class<T> entityType
            = (Class<T>) ((ParameterizedType) super.getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    /**
     * Saves a new entity in the database.
     *
     * @param item The entity to save.
     * @return The saved entity.
     */
    @Override
    public T save(T item) {
        var sql = "insert into %s %s values %s;".formatted(
                tableName(),
                columnNamesForInsert(),
                columnValuesForInsert(item)
        );
        var insertedRows = jdbi.withHandle(handle -> handle.execute(sql));

        if (insertedRows == 0) {
            throw new IllegalStateException("Row not inserted");
        }

        return findLast(1).get(0);
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param id The ID of the entity to update.
     * @param item The updated entity data.
     * @return The updated entity.
     */
    @Override
    public T update(ID id, T item) {
        var sql = "update %s set %s where id = :id".formatted(
                tableName(),
                columnNamesAndValuesForUpdate(item)
        );
        var updatedRows = jdbi.withHandle(handle -> handle
                .createUpdate(sql)
                .bind("id", id)
                .execute());

        if (updatedRows == 0) {
            throw new IllegalStateException("Update not completed");
        }

        return findById(id).orElseThrow();
    }

    /**
     * Saves multiple entities in the database.
     *
     * @param items The list of entities to save.
     * @return The list of saved entities.
     */
    @Override
    public List<T> saveAll(List<T> items) {
        var sql = "insert into %s %s values %s".formatted(
                tableName(),
                columnNamesForInsert(),
                items
                        .stream()
                        .map(this::columnValuesForInsert)
                        .collect(joining(", "))
        );
        var insertedRows = jdbi.withHandle(handle -> handle.execute(sql));
        if (insertedRows == 0) {
            throw new IllegalStateException("Rows not inserted");
        }
        return findLast(insertedRows);
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id The ID of the entity to find.
     * @return An Optional containing the found entity, or empty if not found.
     */
    @Override
    public Optional<T> findById(ID id) {
        var sql = "select * from " + tableName() + " where id = :id";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("id", id)
                .mapToBean(entityType)
                .findFirst()
        );
    }

    /**
     * Finds the last 'n' entities from the database.
     *
     * @param n The number of entities to retrieve.
     * @return A list of the last 'n' entities.
     */
    @Override
    public List<T> findLast(int n) {
        var sql = "select * from " + tableName() + " order by id desc limit :n";
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bind("n", n)
                .mapToBean(entityType)
                .list()
        );
    }

    /**
     * Retrieves all entities from the database.
     *
     * @return A list of all entities.
     */
    @Override
    public List<T> findAll() {
        var sql = "select * from " + tableName();
        return jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .mapToBean(entityType)
                .list()
        );
    }

    /**
     * Finds all entities by a list of their IDs.
     *
     * @param ids The list of IDs of the entities to find.
     * @return A list of found entities.
     */
    @Override
    public List<T> findAllById(List<ID> ids) {
        var sql = "select * from " + tableName() + " where id in (<ids>)";
        var items = jdbi.withHandle(handle -> handle
                .createQuery(sql)
                .bindList("ids", ids)
                .mapToBean(entityType)
                .list());

        if (items.size() != ids.size()) {
            throw new IllegalStateException("Not all ids are present in table");
        }

        return items;
    }

    /**
     * Deletes an entity identified by its ID.
     *
     * @param id The ID of the entity to delete.
     * @return The deleted entity.
     */
    @Override
    public T delete(ID id) {
        var itemToDelete = findById(id)
                .orElseThrow(() -> new IllegalStateException("No item to delete"));

        var sql = "delete from " + tableName() + " where id = :id";
        jdbi.useHandle(handle -> handle
                .createUpdate(sql).bind("id", id)
                .execute());
        return itemToDelete;
    }

    /**
     * Deletes multiple entities identified by their IDs.
     *
     * @param ids The list of IDs of the entities to delete.
     * @return A list of the deleted entities.
     */
    @Override
    public List<T> deleteAllById(List<ID> ids) {
        var items = findAllById(ids);
        var sql = "delete from " + tableName() + " where id in (<ids>)";
        jdbi.useHandle(handle -> handle
                .createUpdate(sql)
                .bindList("ids", ids)
                .execute());
        return items;
    }

    /**
     * Deletes all entities in the database.
     *
     * @return A list of the deleted entities.
     */
    @Override
    public List<T> deleteAll() {
        var items = findAll();
        var sql = "delete from " + tableName() + " where id > 0";
        jdbi.useHandle(handle -> handle.execute(sql));
        return items;  // Returns the list of deleted entities
    }

    /**
     * Converts a string from UpperCamelCase to lower_underscore format.
     *
     * @param upperCamel The string to convert.
     * @return The converted string in lower_underscore format.
     */
    private String toLowerUnderscore(String upperCamel) {
        return CaseFormat.UPPER_CAMEL.to(
                CaseFormat.LOWER_UNDERSCORE,
                upperCamel);
    }

    /**
     * Retrieves the table name for the entity based on its type.
     *
     * @return The table name as a string.
     */
    private String tableName() {
        return English.plural(toLowerUnderscore(entityType.getSimpleName()));
    }

    /**
     * Retrieves the column names for a select statement.
     *
     * @return An array of column names.
     */
    private String[] columnNamesForSelect() {
        return Arrays
                .stream(entityType.getDeclaredFields())
                .map(field -> toLowerUnderscore(field.getName()))
                .toArray(String[]::new);
    }

    /**
     * Retrieves the column names for an insert statement.
     *
     * @return A formatted string of column names for insert.
     */
    private String columnNamesForInsert() {
        var cols = Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(field -> toLowerUnderscore(field.getName()))
                .collect(joining(", "));
        return "( %s )".formatted(cols);
    }

    /**
     * Retrieves the column values for an insert statement.
     *
     * @param item The entity to extract column values from.
     * @return A formatted string of column values for insert.
     */
    private String columnValuesForInsert(T item) {
        var values = Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> !field.getName().equalsIgnoreCase("id"))
                .map(field -> {
                    try {
                        field.setAccessible(true);

                        if (field.get(item) == null) {
                            return "NULL";
                        }

                        if (List.of(
                                String.class,
                                Enum.class,
                                Role.class,
                                AssetType.class,
                                ExpenseType.class,
                                Frequency.class,
                                IncomeType.class,
                                SavingsGoalType.class,
                                LocalDateTime.class,
                                LocalDate.class).contains(field.getType())) {
                            return "'%s'".formatted(field.get(item));
                        }
                        return field.get(item).toString();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }).collect(joining(", "));
        return "( %s )".formatted(values);
    }

    /**
     * Retrieves the column names and values for an update statement.
     *
     * @param item The entity to extract column names and values from.
     * @return A formatted string of column names and values for update.
     */
    private String columnNamesAndValuesForUpdate(T item) {
        return Arrays
                .stream(entityType.getDeclaredFields())
                .filter(field -> {
                    try {
                        field.setAccessible(true);
                        return !field.getName().equalsIgnoreCase("id") && field.get(item) != null;
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .map(field -> {
                    try {
                        field.setAccessible(true);

                        if (List.of(
                                String.class,
                                Enum.class,
                                Role.class,
                                AssetType.class,
                                ExpenseType.class,
                                Frequency.class,
                                IncomeType.class,
                                SavingsGoalType.class,
                                LocalDateTime.class,
                                LocalDate.class).contains(field.getType())) {
                            return "%s='%s'".formatted(
                                    toLowerUnderscore(field.getName()),
                                    field.get(item));
                        }
                        return toLowerUnderscore(field.getName()) + "=" + field.get(item).toString();  // Returns field name and value
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                }).collect(joining(", "));
    }
}
