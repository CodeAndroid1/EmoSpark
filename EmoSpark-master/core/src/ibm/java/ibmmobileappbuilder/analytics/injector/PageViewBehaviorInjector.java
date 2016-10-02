package ibmmobileappbuilder.analytics.injector;

import ibmmobileappbuilder.behaviors.AnalyticsBehavior;
import ibmmobileappbuilder.behaviors.Behavior;
import ibmmobileappbuilder.injectors.ApplicationInjector;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

public class PageViewBehaviorInjector {

    public static Behavior pageViewBehavior(String pageName) {
        return new AnalyticsBehavior(analyticsReporter(getApplicationContext()), pageName);
    }
}
