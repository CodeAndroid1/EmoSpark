package ibmmobileappbuilder.adapters;

import android.content.Context;
import android.util.Log;
import android.widget.BaseAdapter;

import java.util.List;

import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ds.ForceRefreshDatasource;
import ibmmobileappbuilder.ds.Pagination;

/**
 * Adapter backed by a {@link Datasource}.
 * <p/>
 * Based on an implementation of {@link BaseAdapter} which uses the new/bind pattern for its views,
 * <a href="https://gist.github.com/JakeWharton/5423616">
 * https://gist.github.com/JakeWharton/5423616
 * </a>. Apache 2.0 licensed.
 */
public abstract class PaginationAwareAdapter<T> extends DatasourceAdapter<T> {

    private static final String TAG = PaginationAwareAdapter.class.getSimpleName();

    private int currentPage = -1;
    private boolean reachedEnd;

    public PaginationAwareAdapter(Context context, int viewId, Datasource<T> datasource) {
        super(context, viewId, datasource);
    }

    /**
     * Perform a full query to the datasource
     */
    public void refresh(boolean forceRefresh) {
        // reset vars
        currentPage = -1;
        reachedEnd = false;

        // load first page
        loadNextPage(true, forceRefresh); // clear and load first page
    }

    public void loadNextPage() {
        loadNextPage(false);
    }

    public void loadNextPage(boolean clear) {
        loadNextPage(clear, false);
    }

    @SuppressWarnings("unchecked")
    public void loadNextPage(final boolean clear, boolean forceRefresh) {
        if (!reachedEnd) {
            Log.d(TAG, "loading page: " + (currentPage + 1));
            Pagination<T> pagedDS = (Pagination<T>) datasource;
            final int pageSize = pagedDS.getPageSize();

            // notify next page is being requested
            if (callback != null) {
                callback.onPageRequested();
            }

            //if clear we are refreshing so we have to force a sync
            Datasource.Listener<List<T>> paginationListener = new Datasource.Listener<List<T>>() {
                @Override
                public void onSuccess(final List<T> result) {
                    if (result.size() < pageSize) {
                        reachedEnd = true;
                    }

                    // clear if it is first page
                    if (clear) {
                        clear();
                    }

                    addAll(result);

                    notifyDataSetChanged();
                    if (callback != null) {
                        callback.onDataAvailable();
                    }
                }

                @Override
                public void onFailure(final Exception e) {
                    // inform the user
                    notifyDatasourceError(e);
                    notifyDataSetChanged();
                    if (callback != null) {
                        callback.onDatasourceError(e);
                    }
                }
            };

            //TODO this should be able to apply some text searchs
            if (pagedDS instanceof ForceRefreshDatasource) {
                ForceRefreshDatasource forceRefreshDs = (ForceRefreshDatasource) pagedDS;
                forceRefreshDs.getItems(++currentPage, forceRefresh, paginationListener);
            } else {
                pagedDS.getItems(++currentPage, paginationListener);
            }
        }
    }
}