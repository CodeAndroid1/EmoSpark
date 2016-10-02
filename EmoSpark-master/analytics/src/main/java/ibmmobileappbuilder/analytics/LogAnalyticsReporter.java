package ibmmobileappbuilder.analytics;

import android.app.Application;
import android.util.Log;

import java.util.Map;

public class LogAnalyticsReporter implements AnalyticsReporter {

    private static final String TAG = LogAnalyticsReporter.class.getSimpleName();

    @Override
    public void init(Application app) {
        //Do nothing
    }

    @Override
    public void sendView(String screenName) {
        Log.i(TAG, String.format("page_view:%s", screenName));
    }

    @Override
    public void sendEvent(Map<String, String> paramMaps) {
        Log.i(TAG, String.format("Event with params: %s", paramMaps));
    }

    @Override
    public void sendHandledException(String tag, String message, Throwable exception) {
        Log.e(tag, message, exception);
    }
}
