package ibmmobileappbuilder.actions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import ibmmobileappbuilder.analytics.model.AnalyticsInfo;

import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;

public class StartActivityAction implements Action {

    private final Class clazz;
    private final Bundle bundle;
    private final int requestCode;

    public StartActivityAction(Class clazz) {
        this(clazz, 0);
    }

    public StartActivityAction(Class clazz, int requestCode) {
        this(clazz, null, requestCode);
    }

    public StartActivityAction(Class clazz, Bundle bundle) {
        this(clazz, bundle, 0);
    }

    public StartActivityAction(Class clazz, Bundle bundle, int requestCode) {
        this.clazz = clazz;
        this.bundle = bundle;
        this.requestCode = requestCode;
    }

    @Override
    public void execute(@NonNull Context context) {
        Intent intent = new Intent(context, this.clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }

        if (requestCode != 0) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public AnalyticsInfo getAnalyticsInfo() {
        return analyticsInfo()
                .withAction("Start activity")
                .withTarget(clazz.getSimpleName())
                .build();
    }
}
