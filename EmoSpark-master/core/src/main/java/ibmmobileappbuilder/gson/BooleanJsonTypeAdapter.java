package ibmmobileappbuilder.gson;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

public class BooleanJsonTypeAdapter implements JsonDeserializer<Boolean>, JsonSerializer<Boolean> {

    @Override
    public Boolean deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Boolean result = null;
        try {
            result = json.getAsBoolean();
        } catch (Exception e) {
            analyticsReporter(getApplicationContext()).sendHandledException(
                    "BooleanJsonTypeAdapter",
                    "ParseError",
                    e
            );
            Log.d("ParseError", e.getMessage());
        }
        return result;
    }

    @Override
    public JsonElement serialize(Boolean src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
