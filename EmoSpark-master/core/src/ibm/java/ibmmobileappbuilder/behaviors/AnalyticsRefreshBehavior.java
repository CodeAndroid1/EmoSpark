package ibmmobileappbuilder.behaviors;

import android.view.MenuItem;

import ibmmobileappbuilder.analytics.AnalyticsReporter;
import ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector;
import ibmmobileappbuilder.ui.Refreshable;

import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Add refresh pattern to lists
 */
public class AnalyticsRefreshBehavior extends RefreshBehavior {

    private final AnalyticsReporter analyticsReporter;
    private final String datasource;

    public AnalyticsRefreshBehavior(Refreshable fragment, String datasource) {
        super(fragment);
        this.datasource = datasource;
        analyticsReporter = AnalyticsReporterInjector.analyticsReporter(getApplicationContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        if (handled) {
            analyticsReporter.sendEvent(analyticsInfo()
                            .withAction("refresh")
                            .withDataSource(datasource)
                            .build().toMap()
            );
        }
        return handled;
    }
}
