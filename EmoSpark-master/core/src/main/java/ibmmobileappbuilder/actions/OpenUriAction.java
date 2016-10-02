package ibmmobileappbuilder.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import ibmmobileappbuilder.analytics.model.AnalyticsInfo;
import ibmmobileappbuilder.core.R;

import static android.content.Intent.createChooser;
import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;

public class OpenUriAction implements Action {

    private final Uri uri;
    private final IntentLauncher intentLauncher;


    public OpenUriAction(IntentLauncher intentLauncher, @NonNull String uri) {
        this.intentLauncher = intentLauncher;
        Uri parsedUri = Uri.parse(uri);
        if (parsedUri.getScheme() == null) {
            parsedUri = Uri.parse("http://" + uri);
        }
        this.uri = parsedUri;
    }

    @Override
    public void execute(@NonNull Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intentLauncher.start(context, createChooser(intent, context.getString(R.string.open_url)));
    }

    @NonNull
    @Override
    public AnalyticsInfo getAnalyticsInfo() {
        return analyticsInfo()
                .withAction("Open link")
                .withTarget(uri.toString())
                .build();
    }

}
