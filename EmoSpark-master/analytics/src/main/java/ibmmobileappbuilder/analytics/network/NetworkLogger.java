package ibmmobileappbuilder.analytics.network;

import ibmmobileappbuilder.analytics.NetworkResponse;

public interface NetworkLogger {

    void logRequest(String url, String httpMethod);
    void logResponse(NetworkResponse networkResponse);

}
