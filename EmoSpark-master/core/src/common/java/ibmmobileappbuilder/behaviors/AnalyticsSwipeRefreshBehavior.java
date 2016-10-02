package ibmmobileappbuilder.behaviors;

import ibmmobileappbuilder.analytics.AnalyticsReporter;
import ibmmobileappbuilder.ui.ListGridFragment;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Pull to refresh pattern for listing fragments
 */
public class AnalyticsSwipeRefreshBehavior extends SwipeRefreshBehavior {

    private AnalyticsReporter analyticsReporter;

    public AnalyticsSwipeRefreshBehavior(ListGridFragment<?> fragment) {
        super(fragment);
        try {
            analyticsReporter = analyticsReporter(getApplicationContext());
        } catch (Exception e) {
            analyticsReporter = null;
        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        if (analyticsReporter == null) {
            return;
        }
        analyticsReporter.sendEvent(analyticsInfo()
                        .withAction("pullToRefresh")
                        .build().toMap()
        );
    }
}
