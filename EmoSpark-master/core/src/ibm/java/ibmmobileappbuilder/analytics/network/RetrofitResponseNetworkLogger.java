package ibmmobileappbuilder.analytics.network;

import android.support.annotation.Nullable;

import retrofit.client.Response;
import retrofit.mime.TypedByteArray;

import static ibmmobileappbuilder.analytics.NetworkResponse.Builder.networkResponse;

public class RetrofitResponseNetworkLogger {

    private final NetworkLogger networkLogger;

    public RetrofitResponseNetworkLogger(NetworkLogger networkLogger) {
        this.networkLogger = networkLogger;
    }

    public void logResponse(Response response) {
        int statusCode = response.getStatus();
        networkLogger.logResponse(networkResponse()
                        .withUrl(response.getUrl())
                        .withStatusCode(String.valueOf(statusCode))
                        .withBody(getResponseBodyIfRequired(response, statusCode))
                        .build()
        );
    }

    @Nullable
    private String getResponseBodyIfRequired(Response response, int statusCode) {
        if (statusCode < 300 || !(response.getBody() instanceof TypedByteArray)) {
            return null;
        } else {
            return new String(((TypedByteArray) response.getBody()).getBytes());
        }
    }
}
