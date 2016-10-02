package ibmmobileappbuilder.analytics;

import com.worklight.common.WLAnalytics;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import ibmmobileappbuilder.analytics.network.NetworkLogger;

public class MfpNetworkLogger implements NetworkLogger {
    private static final String NETWORK_RESPONSE = "network_response";
    private static final String NETWORK_REQUEST = "network_request";

    @Override
    public void logRequest(String url, String httpMethod) {
        Map<String, String> paramsMap = new HashMap<>(2);
        paramsMap.put("url", url);
        paramsMap.put("http_method", httpMethod);
        WLAnalytics.log(NETWORK_REQUEST, new JSONObject(paramsMap));
    }

    @Override
    public void logResponse(NetworkResponse networkResponse) {
        //TODO object to Json
        Map<String, String> paramsMap = new HashMap<>(3);
        paramsMap.put("url", networkResponse.getUrl());
        paramsMap.put("response_code", networkResponse.getStatusCode());
        paramsMap.put("response_body", networkResponse.getBody());
        WLAnalytics.log(NETWORK_RESPONSE, new JSONObject(paramsMap));
    }
}
