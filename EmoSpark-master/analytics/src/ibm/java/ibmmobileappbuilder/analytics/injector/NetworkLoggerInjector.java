package ibmmobileappbuilder.analytics.injector;

import ibmmobileappbuilder.analytics.MfpNetworkLogger;
import ibmmobileappbuilder.analytics.network.NetworkLogger;

public class NetworkLoggerInjector {

    public static NetworkLogger networkLogger() {
        return new MfpNetworkLogger();
    }

}
