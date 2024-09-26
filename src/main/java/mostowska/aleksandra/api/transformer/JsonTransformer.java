package mostowska.aleksandra.api.transformer;

import com.google.gson.Gson; // Import the Gson library for JSON serialization
import lombok.RequiredArgsConstructor; // Import Lombok for constructor generation
import org.springframework.stereotype.Component; // Import Spring's Component annotation for component scanning
import spark.ResponseTransformer; // Import Spark's ResponseTransformer interface for response transformation

@Component // Mark this class as a Spring component for dependency injection
@RequiredArgsConstructor // Generate a constructor for all final fields, including Gson
public class JsonTransformer implements ResponseTransformer {
    private final Gson gson; // Instance of Gson for converting objects to JSON

    /**
     * Transforms the given object into a JSON string.
     *
     * @param o The object to be transformed into JSON.
     * @return A JSON representation of the object.
     * @throws Exception If an error occurs during transformation.
     */
    @Override
    public String render(Object o) throws Exception {
        return gson.toJson(o); // Convert the object to JSON and return it
    }
}
