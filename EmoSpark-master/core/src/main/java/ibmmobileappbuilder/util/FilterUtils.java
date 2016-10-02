package ibmmobileappbuilder.util;

import android.content.Context;
import android.util.Log;

import java.util.Date;
import java.util.List;

import ibmmobileappbuilder.ds.filter.Filter;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Utility class to ease the sorting and searching actions in lists.
 */
public class FilterUtils {

    public static int compareString(Object o1, Object o2) {
        int res;
        if (o1 == null || o2 == null) {
            res = 0;
        } else {
            res = ((String) o1).compareToIgnoreCase((String) o2);
        }
        return res;
    }

    public static int compareDouble(Double d1, Double d2) {
        int res;
        if (d1 == null || d2 == null) {
            res = 0;
        } else {
            res = d1.compareTo(d2);
        }
        return res;
    }

    public static int compareDate(Context context, Object o1, Object o2) {
        // params will come as Strings
        int res;
        if (o1 == null || o2 == null) {
            res = 0;
        } else {
            try {
                Date d1 = android.text.format.DateFormat.getMediumDateFormat(context)
                        .parse((String) o1);
                Date d2 = android.text.format.DateFormat.getMediumDateFormat(context)
                        .parse((String) o2);
                res = d1.compareTo(d2);
            } catch (Exception e) {
                analyticsReporter(getApplicationContext()).sendHandledException(
                        "FilterUtils",
                        "Parse Error",
                        e
                );
                Log.e("ParseError", e.getMessage());
                res = 0;
            }
        }
        return res;
    }


    public static boolean searchInString(String columnText, String filterText) {
        boolean res = false;
        if (columnText != null && filterText != null) {
            res = columnText.toLowerCase().contains(filterText.toLowerCase());
        }
        return res;
    }

    public static boolean searchInDouble(Double columnText, String filterText) {
        boolean res = false;
        if (columnText != null && filterText != null) {
            res = columnText.toString().toLowerCase().contains(filterText.toLowerCase());
        }
        return res;
    }

    public static boolean searchInDate(Date columnText, String filterText) {
        boolean res = false;
        if (columnText != null && filterText != null) {
            res = columnText.toString().toLowerCase().contains(filterText.toLowerCase());
        }
        return res;
    }

    public static boolean applyFilters(String name, Object value, List<Filter> filters) {
        if (filters != null) {
            for (Filter filter : filters) {
                if (filter.getField().equals(name)) {
                    if (!filter.applyFilter(value)) {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}