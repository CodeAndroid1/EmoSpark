package ibmmobileappbuilder.ds;

import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.OkHttpClient;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import ibmmobileappbuilder.ds.filter.Filter;
import ibmmobileappbuilder.gson.DateJsonTypeAdapter;
import ibmmobileappbuilder.gson.DecimalJsonTypeAdapter;
import ibmmobileappbuilder.gson.IntegerJsonTypeAdapter;
import ibmmobileappbuilder.gson.URLJsonTypeAdapter;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.Converter;
import retrofit.converter.GsonConverter;

/**
 * Service representing a remote API
 * @param <R> the Java interface (retrofit) representing this api
 */
public abstract class RestService<R> {
    private final Class<R> mClass;
    Converter mConverter;
    private R mServiceProxy;

    public RestService(Class<R> clazz){
        mClass = clazz;
    }

    /**
     * Get the service proxy
     *
     * @return the R retrofit proxy
     */
    public R getServiceProxy() {
        if (mServiceProxy == null) {
            RestAdapter restAdapter = createRestAdapterBuilder();
            mServiceProxy = restAdapter.create(mClass);
        }
        return mServiceProxy;
    }

    protected RestAdapter createRestAdapterBuilder() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setClient(getClient())
                .setEndpoint(getServerUrl())
                .setConverter(getConverter())
                .setLogLevel(getLogLevel());

        if (!(tryApiKey(builder) || tryBasicAuth(builder))) {
            throw new IllegalArgumentException("AppNow datasource needs an api key or user-pwd pair !");
        }

        return builder.build();
    }

    protected boolean tryApiKey(RestAdapter.Builder builder) {
        final String apiKey = getApiKey();
        if (apiKey == null){
            return false;
        }

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                request.addHeader("apikey", apiKey);
            }
        });

        return true;
    }

    protected boolean tryBasicAuth(RestAdapter.Builder builder) {
        final String user = getApiUser();
        final String pwd = getApiPassword();

        if(user == null || pwd == null){
            return false;
        }

        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade request) {
                String credentials = user + ":" + pwd;
                String base64EncodedCredentials = Base64
                        .encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                request.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            }
        });

        return true;
    }

    protected String getConditions(SearchOptions options, String[] searchCols){
        if(options == null)
            return null;

        ArrayList<String> exps = new ArrayList<>();
        if(options.getFilters() != null) {
            for (Filter filter : options.getFilters()) {
                String qs = filter.getQueryString();
                if (qs != null)
                    exps.add(qs);
            }
        }

        // TODO: Add full text search $text
        String st = options.getSearchText();
        if (st != null && searchCols != null && searchCols.length > 0){
            ArrayList<String> searches = new ArrayList<>();
            for(String col: searchCols){
                searches.add("{\"" + col + "\":{\"$regex\":\"" + st + "\",\"$options\":\"i\"}}");
            }
            String searchExp = "\"$or\":[" + TextUtils.join(",", searches) + "]";
            exps.add(searchExp);
        }

        if (exps.size() > 0)
            return "{" + TextUtils.join(",", exps) + "}";

        return null;
    }

    protected String getSort(SearchOptions options){
        String col = options.getSortColumn();
        boolean asc = options.isSortAscending();

        if(col == null)
            return null;

        if (!asc)
            col = "-" + col;

        return col;
    }

    protected Converter createConverter() {
        // Initialize the rest backend
        Gson gson = new GsonBuilder()
                .serializeNulls()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)    // field policy
                .registerTypeAdapter(Integer.class, new IntegerJsonTypeAdapter())
                .registerTypeAdapter(Double.class, new DecimalJsonTypeAdapter())
                .registerTypeAdapter(Date.class, new DateJsonTypeAdapter())  // Date conversions for allowed formats
                .registerTypeAdapter(URL.class, new URLJsonTypeAdapter())
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        // disable rest _id (most times are mongo attributes) serialization
                        // this could slow down serialization,
                        // but luckily gson is caching the result
                        SerializedName annotation = f.getAnnotation(SerializedName.class);
                        return annotation != null && annotation.value().equals("_id");
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        return new GsonConverter(gson);
    }

    protected Converter getConverter() {
        if (mConverter == null) {
            mConverter = createConverter();
        }

        return mConverter;
    }

    /**
     * Override this to customise the log level
     *
     * @return the LogLevel
     */
    protected RestAdapter.LogLevel getLogLevel() {
        return RestAdapter.LogLevel.NONE;
    }

    /**
     * Override this to customize client
     */
    protected Client getClient() {
        OkHttpClient c = new OkHttpClient();
        c.setConnectTimeout(getHttpClientTimeout(), TimeUnit.SECONDS);
        return new OkClient(c);
    }

    /**
     * Set the connection timeout, override to customize
     *
     * @return the timeout in seconds
     */
    protected long getHttpClientTimeout() {
        return 5;
    }

    /**
     * Get the api key for this datasource
     *
     * @return null if no api key is present, or the api key to enable basic auth
     */
    protected String getApiKey() {
        return null;
    }

    protected String getApiUser(){
        return null;
    }

    protected String getApiPassword(){
        return null;
    }

    /**
     * Get the base url for this retrofit endpoint
     *
     * @return the base url
     */
    public abstract String getServerUrl();

    /**
     * Get the url for a image resource in this datasource
     * @param path the image path (can be relative or absolute)
     * @return the URL object you can pass to an ImageLoader class
     */
    public abstract URL getImageUrl(String path);
}
