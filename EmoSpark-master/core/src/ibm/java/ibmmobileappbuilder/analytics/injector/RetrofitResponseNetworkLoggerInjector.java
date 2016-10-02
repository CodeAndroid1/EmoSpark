package ibmmobileappbuilder.analytics.injector;

import ibmmobileappbuilder.analytics.network.RetrofitResponseNetworkLogger;

import static ibmmobileappbuilder.analytics.injector.NetworkLoggerInjector.networkLogger;

public class RetrofitResponseNetworkLoggerInjector {

    public static RetrofitResponseNetworkLogger retrofitNetworkLogger() {
        return new RetrofitResponseNetworkLogger(networkLogger());
    }
}
