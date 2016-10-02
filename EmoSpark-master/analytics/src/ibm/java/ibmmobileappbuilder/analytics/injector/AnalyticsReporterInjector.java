package ibmmobileappbuilder.analytics.injector;

import android.content.Context;
import android.util.Log;

import java.util.Properties;

import ibmmobileappbuilder.analytics.AnalyticsReporter;
import ibmmobileappbuilder.analytics.LogAnalyticsReporter;
import ibmmobileappbuilder.analytics.MfpAnalyticsReporter;

public class AnalyticsReporterInjector {

    private static AnalyticsReporter instance;

    public static AnalyticsReporter analyticsReporter(Context context) {
        if (instance != null) {
            return instance;
        }

        try {
            Properties p = new Properties();
            p.load(context.getAssets().open("wlclient.properties"));
            String wlServerPort = p.getProperty("wlServerPort");
            if (wlServerPort != null && !wlServerPort.isEmpty()) {
                instance = new MfpAnalyticsReporter();
            } else {
                instance = new LogAnalyticsReporter();
            }
        } catch (Exception e) {
            Log.w("AnalyticsReporter", "Could not initialize MFP", e);
            instance = new LogAnalyticsReporter();
        }
        return instance;
    }

    public static AnalyticsReporter analyticsReporter() {
        if (instance == null) {
            throw new IllegalStateException("You must call analyticsReporter(Context context) first");
        }
        return instance;
    }
}
