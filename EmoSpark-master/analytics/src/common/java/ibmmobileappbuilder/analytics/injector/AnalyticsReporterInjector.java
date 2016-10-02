package ibmmobileappbuilder.analytics.injector;

import android.content.Context;
import android.util.Log;

import java.util.Properties;

import ibmmobileappbuilder.analytics.AnalyticsReporter;
import ibmmobileappbuilder.analytics.LogAnalyticsReporter;

public class AnalyticsReporterInjector {

    private final static AnalyticsReporter LOG_ANALYTICS_REPORTER = new LogAnalyticsReporter();

    public static AnalyticsReporter analyticsReporter() {
        return LOG_ANALYTICS_REPORTER;
    }

    public static AnalyticsReporter analyticsReporter(Context context) {
        return LOG_ANALYTICS_REPORTER;
    }
}
