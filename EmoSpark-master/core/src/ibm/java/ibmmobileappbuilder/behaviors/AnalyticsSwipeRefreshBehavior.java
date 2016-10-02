package ibmmobileappbuilder.behaviors;

import ibmmobileappbuilder.analytics.AnalyticsReporter;
import ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector;
import ibmmobileappbuilder.injectors.ApplicationInjector;
import ibmmobileappbuilder.ui.ListGridFragment;

import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;

/**
 * Pull to refresh pattern for listing fragments
 */
public class AnalyticsSwipeRefreshBehavior extends SwipeRefreshBehavior {

    private AnalyticsReporter analyticsReporter;

    public AnalyticsSwipeRefreshBehavior(ListGridFragment<?> fragment) {
        super(fragment);
        try {
            Class.forName("AnalyticsReporterInjector",
                    false,
                    ClassLoader.getSystemClassLoader()
            );
            analyticsReporter = AnalyticsReporterInjector.analyticsReporter(ApplicationInjector.getApplicationContext());
        } catch (ClassNotFoundException e) {
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
