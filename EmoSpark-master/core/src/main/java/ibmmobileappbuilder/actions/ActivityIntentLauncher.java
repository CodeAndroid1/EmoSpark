package ibmmobileappbuilder.actions;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public class ActivityIntentLauncher implements IntentLauncher {

    @Override
    public void start(@NonNull Context context, @NonNull Intent intent) {
        context.startActivity(intent);
    }
}
