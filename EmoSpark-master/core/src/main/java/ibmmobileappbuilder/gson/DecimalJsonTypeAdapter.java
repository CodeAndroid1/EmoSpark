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

import ibmmobileappbuilder.injectors.ApplicationInjector;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;

/**
 * Adapter for Radarc-type Backend Decimal type.
 */
public class DecimalJsonTypeAdapter implements JsonDeserializer<Double>, JsonSerializer<Double> {

    public Double deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String jsonDouble = json.getAsString();
        Double res = 0.0;
        try {
            res = Double.parseDouble(jsonDouble);
        } catch (Exception e) {
            analyticsReporter(ApplicationInjector.getApplicationContext()).sendHandledException(
                    "DecimalJsonTypeAdapter",
                    "ParseError",
                    e
            );
            Log.d("ParseError", e.getMessage());
        }
        return res;
    }

    @Override
    public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src);
    }
}
