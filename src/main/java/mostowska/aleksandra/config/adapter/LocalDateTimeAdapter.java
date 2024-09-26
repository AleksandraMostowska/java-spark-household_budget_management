package mostowska.aleksandra.config.adapter;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Custom adapter for serializing and deserializing LocalDateTime objects
 * using Gson.
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Serializes a LocalDateTime object to its JSON representation.
     *
     * @param src The LocalDateTime object to serialize.
     * @param typeOfSrc The type of the source object (used by Gson, not used here).
     * @param context The context of the serialization process.
     * @return A JsonElement representing the serialized LocalDateTime.
     */
    @Override
    public JsonElement serialize(LocalDateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.format(formatter));
    }

    /**
     * Deserializes a JSON element into a LocalDateTime object.
     *
     * @param json The JSON element to deserialize.
     * @param typeOfT The type of the target object (used by Gson, not used here).
     * @param context The context of the deserialization process.
     * @return The deserialized LocalDateTime object.
     */
    @Override
    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        return LocalDateTime.parse(json.getAsString(), formatter);
    }
}
