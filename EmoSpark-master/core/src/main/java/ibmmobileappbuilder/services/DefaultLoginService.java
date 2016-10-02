/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.services;

import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import ibmmobileappbuilder.util.LoginUtils;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class DefaultLoginService extends AsyncTask<Void, Void, Map<String, String>>
        implements LoginService {

    private String mEmail;

    private String mPassword;

    private String mServerUrl;

    private HttpClient httpclient;

    public DefaultLoginService(String serverUrl) {
        mServerUrl = serverUrl;
        httpclient = new DefaultHttpClient();
    }

    @Override
    protected Map<String, String> doInBackground(Void... params) {

        HttpUriRequest request = new HttpPost(mServerUrl);
        String credentials = mEmail + ":" + mPassword;
        String base64EncodedCredentials = Base64
                .encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
        HttpResponse response = null;
        Map<String, String> responseParams = new HashMap<String, String>();

        try {
            response = httpclient.execute(request);
        } catch (IOException e) {
            analyticsReporter(getApplicationContext()).sendHandledException(
                    "DefaultLoginService",
                    "Client execute",
                    e
            );
            Log.e("clientExecute", e.getMessage());
        }

        if (response != null && response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

            String line = LoginUtils.getResponseString(response);
            JSONObject json = LoginUtils.parseJSON(line);

            try {
                responseParams.put(LoginUtils.EXPIRATION_TIME, json.getString("expirationTime"));
                responseParams.put(LoginUtils.TOKEN, json.getString("token"));
            } catch (JSONException e) {
                analyticsReporter(getApplicationContext()).sendHandledException(
                        "DefaultLoginService",
                        "jsonGetLong",
                        e
                );
                Log.e("jsonGetLong", e.getMessage());
            }
        }
        return responseParams;
    }

    @Override
    public void attemptLogin(String email, String password) {
        this.mEmail = email;
        this.mPassword = password;
        this.execute();
    }
}
