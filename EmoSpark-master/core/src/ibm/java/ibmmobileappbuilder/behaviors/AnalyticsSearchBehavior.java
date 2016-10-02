package ibmmobileappbuilder.behaviors;

import ibmmobileappbuilder.analytics.AnalyticsReporter;
import ibmmobileappbuilder.ui.Filterable;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Adds an action bar search interface
 */
public class AnalyticsSearchBehavior extends SearchBehavior {

    private final AnalyticsReporter analyticsReporter;
    private final String datasource;

    public AnalyticsSearchBehavior(Filterable f, String datasource) {
        super(f);
        this.datasource = datasource;
        analyticsReporter = analyticsReporter(getApplicationContext());

    }

    public void refreshSearch(String newFilter) {
        super.refreshSearch(newFilter);

        analyticsReporter.sendEvent(analyticsInfo()
                        .withAction("search")
                        .withTarget(newFilter)
                        .withDataSource(datasource)
                        .build().toMap()
        );
    }
}
