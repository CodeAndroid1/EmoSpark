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
import java.net.MalformedURLException;
import java.net.URL;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Adapter for Radarc-type Backend URL type.
 */
public class URLJsonTypeAdapter implements JsonDeserializer<URL>, JsonSerializer<URL> {

    public URL deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String jsonUrl = json.getAsString();
        URL result = null;
        try {
            result = new URL(jsonUrl);
        } catch (MalformedURLException e) {
            analyticsReporter(getApplicationContext()).sendHandledException(
                    "URLJsonTypeAdapter",
                    "Parse Error",
                    e
            );
            Log.d("ParseError", e.getMessage());
        }
        return result;
    }

    @Override
    public JsonElement serialize(URL src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toExternalForm());
    }
}
