package ibmmobileappbuilder.analytics;

import android.app.Application;

import java.util.Map;

public interface AnalyticsReporter {
    AnalyticsReporter NO_OP = new AnalyticsReporter() {
        @Override
        public void init(Application app) { }

        @Override
        public void sendView(String screenName) { }

        @Override
        public void sendEvent(Map<String, String> paramsMap) { }

        @Override
        public void sendHandledException(String tag, String message, Throwable exception) {}
    };

    void init(Application app);
    void sendView(String screenName);
    void sendEvent(Map<String, String> paramsMap);
    void sendHandledException(String tag, String message, Throwable exception);
}