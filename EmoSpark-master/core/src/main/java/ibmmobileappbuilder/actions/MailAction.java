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
public class MailAction implements Action {

    private final String mail;
    private final IntentLauncher intentLauncher;

    public MailAction(IntentLauncher intentLauncher, String mail) {
        this.intentLauncher = intentLauncher;
        if (mail != null && !mail.startsWith("mailto:")) {
            this.mail = "mailto:" + mail;
        } else {
            this.mail = mail;
        }
    }

    @Override
    public void execute(@NonNull Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse(mail));

        intentLauncher.start(context,
                createChooser(intent, context.getString(R.string.send_email))
        );
    }

    @NonNull
    @Override
    public AnalyticsInfo getAnalyticsInfo() {
        return analyticsInfo()
                .withAction("Send email")
                .withTarget(mail)
                .build();
    }
}
