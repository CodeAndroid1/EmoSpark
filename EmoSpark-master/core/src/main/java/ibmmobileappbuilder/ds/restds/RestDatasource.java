package ibmmobileappbuilder.ds.restds;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.squareup.okhttp.OkHttpClient;

import android.text.TextUtils;
import android.util.Base64;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ds.SearchOptions;
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
 * Datasource backed with Retrofit framework
 *
 * @param <T> the entity type this datasource returns
 * @param <R> the interface class that defines this retrofit service
 */
public abstract class RestDatasource<T, R> implements Datasource<T> {

    protected SearchOptions searchOptions;

    private Class<R> serviceInterface;
    private Converter converter;
    private R serviceProxy;

    public RestDatasource(Class<R> clazz, SearchOptions searchOptions) {
        this.serviceInterface = clazz;
        this.searchOptions = searchOptions;
    }


    @Override
    public void onSearchTextChanged(String s){
        searchOptions.setSearchText(s);
    }

    @Override
    public void addFilter(Filter filter){
        searchOptions.addFilter(filter);
    }

    @Override
    public void clearFilters() {
        searchOptions.setFilters(null);
    }

    /**
     * Get the service proxy
     *
     * @return the R retrofit proxy
     */
    protected R getServiceProxy() {
        if (serviceProxy == null) {
            RestAdapter restAdapter = createRestAdapterBuilder().build();
            serviceProxy = restAdapter.create(serviceInterface);
        }
        return serviceProxy;
    }

    /**
     * Sets the service interface for this Retrofit datasource
     *
     * @param clazz the service interface
     */
    public void setServiceInterface(Class clazz) {
        this.serviceInterface = clazz;
    }

    /**
     * Creates the Retrofit Converter used for this datasource. Override this
     * to set your own configuration
     *
     * @return the Converter object
     */
    protected Converter createConverter() {
        // Initialize the rest backend
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)    // field policy
                .registerTypeAdapter(Integer.class, new IntegerJsonTypeAdapter())
                .registerTypeAdapter(Double.class, new DecimalJsonTypeAdapter())
                .registerTypeAdapter(Date.class, new DateJsonTypeAdapter())  // Date conversions for allowed formats
                .registerTypeAdapter(URL.class, new URLJsonTypeAdapter())
                .create();

        return new GsonConverter(gson);
    }

    protected Converter getConverter() {
        if (converter == null) {
            converter = createConverter();
        }

        return converter;
    }

    /**
     * Create a builder for this datasource
     *
     * @return the Retrofit builder object
     */
    protected RestAdapter.Builder createRestAdapterBuilder() {
        RestAdapter.Builder builder = new RestAdapter.Builder()
                .setClient(getClient())
                .setEndpoint(getServerUrl())
                .setConverter(getConverter())
                .setLogLevel(getLogLevel());

        // we use basic auth based on app-id:api-key
        if (getApiKey() != null) {
            builder.setRequestInterceptor(new RequestInterceptor() {
                @Override
                public void intercept(RequestFacade request) {
                    String credentials = getAppId() + ":" + getApiKey();
                    String base64EncodedCredentials = Base64
                            .encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    request.addHeader("Authorization", "Basic " + base64EncodedCredentials);

                    String token = getToken();
                    if (token != null) {
                        request.addHeader("UserToken", token);
                    }
                }
            });
        }

        return builder;
    }

    protected String getFilterQuery(SearchOptions options){

        ArrayList<String> conditions = new ArrayList<>();

        addFilters(conditions, options.getFixedFilters());
        addFilters(conditions, options.getFilters());

        if (conditions.size() > 0){
            return "[" + TextUtils.join(",", conditions) + "]";
        }

        return null;
    }

    private void addFilters(ArrayList<String> exps, List<Filter> filters) {
        if(filters != null) {
            for (Filter filter : filters) {
                String qs = filter.getQueryString();
                if (qs != null)
                    exps.add("{" + qs + "}");
            }
        }
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

    // API

    /**
     * Get the base url for this retrofit endpoint
     *
     * @return the base url
     */
    public abstract String getServerUrl();

    /**
     * Get the api key for this datasource
     *
     * @return null if no api key is present, or the api key to enable basic auth
     */
    protected String getApiKey() {
        return null;
    }

    /**
     * Get the App Id
     *
     * @return the app id
     */
    protected String getAppId() {
        return null;
    }

    /**
     * Get the token required for retrieve secured data
     *
     * @return the token
     */
    protected String getToken() {
        return null;
    }

    /**
     * Get the username required for retrieve secured data
     *
     * @return the user
     */
    protected String getUserId() {
        return null;
    }

    /**
     * Set the connection timeout, override to customize
     *
     * @return the timeout in seconds
     */
    protected long getHttpClientTimeout() {
        return 5;
    }
}
