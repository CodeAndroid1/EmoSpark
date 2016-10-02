package ibmmobileappbuilder.util;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import java.lang.reflect.Method;

import ibmmobileappbuilder.injectors.ApplicationInjector;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Utils for fragment instantiation
 */
public class FragmentUtils {
    private static final String TAG = FragmentUtils.class.getSimpleName();

    // fragment instantiation
    public static Fragment instantiate(Class<? extends Fragment> clazz, Bundle defaults) {
        try {
            Method method = clazz.getMethod("newInstance", Bundle.class);
            return (Fragment) method.invoke(null, defaults);
        } catch (Exception e) {
            analyticsReporter(getApplicationContext()).sendHandledException(
                    "FragmentUtils",
                    "Exception instantiating the fragment [" + clazz.getName() + "]",
                    e
            );
            Log.d(TAG, "Exception instantiating the fragment [" + clazz.getName() + "]");
            throw new IllegalArgumentException("Couldn't instantie fragment: + clazz.getName()", e);
        }
    }


}
