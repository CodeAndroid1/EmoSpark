package ibmmobileappbuilder.analytics;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import junit.framework.Assert;
import java.util.Map;

public class GoogleAnalyticsReporter implements AnalyticsReporter {

    private Tracker tracker;
    private final int configurationId;

    public GoogleAnalyticsReporter(int configurationId) {
        this.configurationId = configurationId;
    }

    @Override
    public void init(Application app) {
        tracker = GoogleAnalytics.getInstance(app).newTracker(configurationId);
    }

    @Override
    public void sendView(String screenName) {
        assertTrackerNotNull();
        tracker.setScreenName(screenName);
        tracker.send(new HitBuilders.ScreenViewBuilder().build());

    }

    @Override
    public void sendEvent(Map<String, String> paramsMap) {
        assertTrackerNotNull();
        tracker.send(new HitBuilders.EventBuilder()
                        .setCategory(paramsMap.get("category"))
                        .setAction(paramsMap.get("action"))
                        .build()
        );
    }

    @Override
    public void sendHandledException(String tag, String message, Throwable exception) {
        tracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(message)
                .setFatal(false)
                .build());
    }

    private void assertTrackerNotNull() {
        Assert.assertNotNull("You must call first init with a valid context", tracker);
    }
}
