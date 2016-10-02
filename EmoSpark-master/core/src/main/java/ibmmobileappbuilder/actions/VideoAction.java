package ibmmobileappbuilder.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import ibmmobileappbuilder.analytics.model.AnalyticsInfo;
import ibmmobileappbuilder.core.R;

import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;

/**
 * Play video action
 */
public class VideoAction implements Action {

    private final IntentLauncher intentLauncher;
    private final String link;

    public VideoAction(IntentLauncher intentLauncher, String link) {
        this.intentLauncher = intentLauncher;
        this.link = link;
    }

    @Override
    public void execute(@NonNull Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));

        intentLauncher.start(context, Intent.createChooser(intent, context.getString(R.string.play_video)));
    }

    @NonNull
    @Override
    public AnalyticsInfo getAnalyticsInfo() {
        return analyticsInfo()
                .withAction("Play video")
                .withTarget(link)
                .build();
    }
}
