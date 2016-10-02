package ibmmobileappbuilder.actions;


import android.content.Context;
import android.support.annotation.NonNull;

import ibmmobileappbuilder.analytics.model.AnalyticsInfo;

public interface Action {

    void execute(@NonNull Context context);

    @NonNull
    AnalyticsInfo getAnalyticsInfo();

}
