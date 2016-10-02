package ibmmobileappbuilder.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import ibmmobileappbuilder.analytics.model.AnalyticsInfo;
import ibmmobileappbuilder.core.R;

import static android.content.Intent.createChooser;
import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;

public class MapsAction implements Action {

    private final IntentLauncher intentLauncher;
    private final String uri;

    public MapsAction(IntentLauncher intentLauncher, String uri) {
        this.intentLauncher = intentLauncher;
        this.uri = uri;
    }

    @Override
    public void execute(@NonNull Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));

        intentLauncher.start(context,
                createChooser(intent, context.getString(R.string.find_on_map))
        );
    }

    @NonNull
    @Override
    public AnalyticsInfo getAnalyticsInfo() {
        return analyticsInfo()
                .withAction("Find on map")
                .withTarget(uri)
                .build();
    }
}
