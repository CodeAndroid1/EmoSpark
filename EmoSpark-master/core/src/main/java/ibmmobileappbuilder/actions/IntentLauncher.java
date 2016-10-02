package ibmmobileappbuilder.actions;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

public interface IntentLauncher {

    void start(@NonNull Context context, @NonNull Intent intent);
}
