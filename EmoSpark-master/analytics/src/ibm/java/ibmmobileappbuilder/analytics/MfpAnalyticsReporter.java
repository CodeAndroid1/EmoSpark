package ibmmobileappbuilder.analytics;

import android.app.Application;
import android.util.Log;

import com.worklight.common.Logger;
import com.worklight.common.WLAnalytics;
import com.worklight.wlclient.api.WLClient;
import com.worklight.wlclient.api.WLFailResponse;
import com.worklight.wlclient.api.WLResponse;
import com.worklight.wlclient.api.WLResponseListener;

import org.json.JSONObject;

import java.util.Collections;
import java.util.Map;

public class MfpAnalyticsReporter implements AnalyticsReporter, WLResponseListener {

    private static final String TAG = MfpAnalyticsReporter.class.getSimpleName();
    private static final String PAGE_VIEW = "page_view";
    private static final String EVENT = "event";

    @Override
    public void init(Application application) {
        WLAnalytics.startRecordingActivityLifecycleEvents(application);
        WLClient.createInstance(application).connect(this);
        WLAnalytics.setContext(application);
    }

    @Override
    public void sendView(String pageName) {
        WLAnalytics.log(PAGE_VIEW, new JSONObject(Collections.singletonMap("page", pageName)));
    }

    @Override
    public void sendEvent(Map<String, String> paramsMap) {
        WLAnalytics.log(EVENT, new JSONObject(paramsMap));
    }

    @Override
    public void sendHandledException(String tag, String message, Throwable exception) {
        Logger.getInstance(tag).error(message, exception);
        Logger.send();
    }

    @Override
    public void onSuccess(WLResponse wlResponse) {
        Log.v(TAG, "success connecting");
    }

    @Override
    public void onFailure(WLFailResponse wlFailResponse) {
        Log.w(TAG, "Failure connecting to mfp services" + wlFailResponse.getResponseText());
    }
}
