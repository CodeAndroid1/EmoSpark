package ibmmobileappbuilder.actions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import ibmmobileappbuilder.analytics.model.AnalyticsInfo;
import ibmmobileappbuilder.core.R;

import static android.content.Intent.createChooser;
import static ibmmobileappbuilder.analytics.model.AnalyticsInfo.Builder.analyticsInfo;

/**
 * Mail sender action
 */
public class PhoneAction implements Action {

    private final IntentLauncher intentLauncher;
    private final String phoneNumber;

    public PhoneAction(IntentLauncher intentLauncher, String phoneNumber) {
        this.intentLauncher = intentLauncher;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public void execute(@NonNull Context context) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));

        intentLauncher.start(context, createChooser(intent, context.getString(R.string.call)));
    }

    @NonNull
    @Override
    public AnalyticsInfo getAnalyticsInfo() {
        return analyticsInfo()
                .withAction("Call to phone")
                .withTarget(phoneNumber)
                .build();
    }
}
