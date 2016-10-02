package ibmmobileappbuilder.util;

import android.support.annotation.Nullable;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import ibmmobileappbuilder.analytics.AnalyticsReporter;
import ibmmobileappbuilder.ds.restds.GeoPoint;
import ibmmobileappbuilder.injectors.GsonInjector;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * String utility methods
 */
public class StringUtils {

    private static final String TAG = StringUtils.class.getSimpleName();
    private static final AnalyticsReporter ANALYTICS_REPORTER = analyticsReporter(getApplicationContext());

    /**
     * returns the first chars from a String, or the entire string if its lenght is lower than
     * nChars
     *
     * @param theString the input string
     * @param nChars    the number of chars to be returned (if lenght < nchars)
     * @param ellipsize if a "..." should be appended to the output
     * @return the truncated char
     */
    public static String firstNChars(String theString, int nChars, boolean ellipsize) {
        if (theString == null) {
            return null;
        }

        int endIndex = theString.length() > nChars ? nChars : theString.length() - 1;

        return theString.substring(0, endIndex) + (ellipsize ? " ..." : "");
    }

    /**
     * Remove all &lt;img&gt; tags from the input string
     *
     * @param input the input string
     * @return the resulting string
     */
    public static String removeImgTag(String input) {
        return input.replaceAll("<img.+?>", "");
    }

    /**
     * returns a data object as a Number despite of its type. Used in charts.
     *
     * @param num the object to be converted to Number. Could be String or Double
     * @return the object converted to Number
     */
    public static Number StringToNumber(Object num) {
        if (num == null) {
            return null;
        }

        if (num instanceof Number) {
            return (Number) num;
        }

        Number res = null;

        try {
            if (num instanceof String) {
                res = Float.parseFloat((String) num);
            }
        } catch (NumberFormatException e) {
            ANALYTICS_REPORTER.sendHandledException(TAG, "Error parsing string to number", e);
            Log.d("Parsing Error", "Error parsing string to number " + e.getMessage());
        }
        return res;
    }

    /**
     * Utility method to parse a date using ISO-8601
     *
     * @return the parsed date or null if an exception occurs
     */
    public static Date parseDateTime(String date) {
        Date res = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        // all dates must come in UTC timezone
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            res = df.parse(date);
        } catch (ParseException e) {
            ANALYTICS_REPORTER.sendHandledException(TAG, "Parse Error while parsing Date", e);
            Log.e("ParseError", e.getMessage());
        }

        return res;
    }

    /**
     * Utility method to parse a date using ISO-8601
     *
     * @return the parsed date or null if an exception occurs
     */
    public static Date parseDate(String date) {
        Date res = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // all dates must come in UTC timezone
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            res = df.parse(date);
        } catch (ParseException e) {
            ANALYTICS_REPORTER.sendHandledException(TAG, "Parse Error while parsing Date", e);
            Log.e("ParseError", e.getMessage());
        }

        return res;
    }

    /**
     * Returns the short month name, given its month number
     *
     * @param month
     * @return the month name (JAN, FEB, etc). Months are 0-based
     */
    public static String monthName(int month) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.set(Calendar.MONTH, month);
            SimpleDateFormat format = new SimpleDateFormat("MMM");
            return format.format(cal.getTime());
        } catch (Exception e) {
            ANALYTICS_REPORTER.sendHandledException(TAG, "monthName", e);
            return null;
        }
    }

    /*
     * Converts a double into a String for TextViews, removing optionally any
     * 0 decimals (ie: 34.00 -> 34)
     */
    public static String doubleToString(Double val, boolean removeTrailingZeroes) {
        if (val == null) {
            return null;
        }

        String res = val.toString();
        return removeTrailingZeroes ?
                res.replaceAll("\\.0*$", "") :
                res;
    }

    public static Long parseLong(String value) {
        Long res;
        try {
            res = (value != null && value.length() != 0) ? Long.parseLong(value.toString()) : null;
        } catch (NumberFormatException e) {
            ANALYTICS_REPORTER.sendHandledException(TAG, "parseLong", e);
            res = null;
        }
        return res;
    }

    public static Double parseDouble(String value) {
        Double res;
        try {
            res = (value != null && value.length() != 0) ? Double.parseDouble(value.toString()) : null;
        } catch (NumberFormatException e) {
            ANALYTICS_REPORTER.sendHandledException(TAG, "parseDouble", e);
            res = null;
        }
        return res;
    }

    /**
     * Utility for transforming a boolean into a string, supporting nulls (tristate)
     *
     * @param value the Boolean object, can be null
     * @return a string representation of the value
     */
    public static String booleanToString(Boolean value) {
        return (value == null) ? "" : (value ? "Yes" : "No");
    }

    public static GeoPoint parseGeopoint(String geopointJson) {
        return GsonInjector.cloudantGson().fromJson(geopointJson, GeoPoint.class);
    }

    /**
     * parses a string and try to take an url from it
     *
     * @param value the input string
     * @return a valid url, or null
     */
    public static
    @Nullable
    URL parseUrl(String value) {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            ANALYTICS_REPORTER.sendHandledException(TAG, "parseUrl", e);
            return null;
        }
    }

    /**
     * check if a url is relative, and prepends a baseUrl in that case
     *
     * @param baseUrl the base url to prepend if the url is relative
     * @param url     the url to parse (if relative, it must start with '/')
     * @param qs      query string
     * @return a valid url, or null
     */
    public static
    @Nullable
    URL parseUrl(String baseUrl, String url, String qs) {
        if (url == null) {
            return null;
        }

        if (url.startsWith("http://") || url.startsWith("https://")) {
            return parseUrl(url);
        } else if (baseUrl != null) {
            String postfix = qs != null ? (url.indexOf('?') >= 0 ? "&" : "?") + qs : "";

            // url is relative
            if (baseUrl.endsWith("/")) {
                baseUrl = baseUrl.substring(0, baseUrl.length());
            }

            return parseUrl(baseUrl + url + postfix);
        }

        return null;
    }

    /**
     * silly method overload to better and cleaner code generation
     *
     * @param url an url
     * @return the same input url
     */
    public static URL parseUrl(URL url) {
        return url;
    }

    public static boolean isEmptyOrNull(String text) {
        return text == null || text.isEmpty();
    }

    public static boolean isNotEmptyOrNull(String text) {
        return !isEmptyOrNull(text);
    }
}
