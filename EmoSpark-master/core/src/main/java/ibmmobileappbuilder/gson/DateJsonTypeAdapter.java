package ibmmobileappbuilder.gson;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import android.util.Log;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import ibmmobileappbuilder.injectors.ApplicationInjector;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Adapter for Radarc-type Backend Date type.
 */
public class DateJsonTypeAdapter implements JsonDeserializer<Date>, JsonSerializer<Date> {

    private static final String DEFAULT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final String[] ISO_FORMATS = {
            DEFAULT_FORMAT,
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm'Z'",
            "yyyy-MM-dd",
            "yyyyMMdd",
            "yy-MM-dd",
            "yyMMdd"
    };


    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {

        String jsonDate = json.getAsString();

        for (String format : ISO_FORMATS) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);
                // all dates must come in UTC timezone
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                return sdf.parse(jsonDate);
            } catch (Exception e) {
                analyticsReporter(getApplicationContext()).sendHandledException(
                        "DateJsonTypeAdapter",
                        "ParseError",
                        e
                );
                Log.d("ParseError", e.getMessage());
            }
        }

        return null;
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

        return new JsonPrimitive(sdf.format(src));
    }
}
