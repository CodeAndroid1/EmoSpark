package ibmmobileappbuilder.injectors;

import android.app.Application;
import android.content.Context;

public class ApplicationInjector {

    private static Application context;

    public static void setApplicationContext(Application application) {
        context = application;
    }

    public static Context getApplicationContext() {
        if (context != null) {
            return context;
        }
        throw new IllegalStateException("You must set the application context first");
    }
}
