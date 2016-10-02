package ibmmobileappbuilder.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

import ibmmobileappbuilder.ds.Cache;
import ibmmobileappbuilder.ds.Datasource;

/**
 * Adapter backed by a {@link Datasource}.
 * <p/>
 * Based on an implementation of {@link BaseAdapter} which uses the new/bind pattern for its views,
 * <a href="https://gist.github.com/JakeWharton/5423616">
 * https://gist.github.com/JakeWharton/5423616
 * </a>. Apache 2.0 licensed.
 */
public abstract class DatasourceAdapter<T> extends ArrayAdapter<T> {


    private final LayoutInflater inflater;
    private final int viewId;

    protected final Datasource<T> datasource;
    protected Callback callback;
    protected WeakReference<Context> contextWeakReference;

    public DatasourceAdapter(Context context, int viewId, Datasource<T> datasource) {
        super(context, viewId);
        this.inflater = LayoutInflater.from(context);
        this.datasource = datasource;
        this.viewId = viewId;
        this.contextWeakReference = new WeakReference<>(context);

        setNotifyOnChange(false); // we want to control when observers will be notified
    }

    /**
     * Sets a new callback for operations
     *
     * @param c the new callback to register
     */
    public void setCallback(Callback c) {
        this.callback = c;
    }

    public Callback getCallback() {
        return callback;
    }


    public void refresh() {
        refresh(false);
    }

    /**
     * Perform a full query to the datasource
     */
    public void refresh(boolean forceRefresh) {
        // invalidate current data
        if (datasource instanceof Cache) {
            ((Cache) datasource).invalidate();
        }

        datasource.getItems(
                new Datasource.Listener<List<T>>() {
                    @Override
                    public void onSuccess(final List<T> result) {
                        // ensure that datasource callbacks (which may be async) are run in ui thread
                        runOnActivity(
                                new Runnable() {
                                    @Override
                                    public void run() {

                                        clear();
                                        addAll(result);

                                        notifyDataSetChanged();
                                        // signal that the data is available
                                        if (callback != null) {
                                            callback.onDataAvailable();
                                        }
                                    }
                                }
                        );
                    }

                    @Override
                    public void onFailure(final Exception e) {
                        runOnActivity(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        // inform the user
                                        if (callback != null) {
                                            callback.onDatasourceError(e);
                                        }
                                        notifyDatasourceError(e);
                                        notifyDataSetChanged();
                                    }
                                }
                        );
                    }
                }
        );
    }

    private void runOnActivity(Runnable runnable) {
        Activity act = (Activity) contextWeakReference.get();
        if (act != null) {
            act.runOnUiThread(runnable);
        }
    }

    @Override
    public final View getView(int position, View view, ViewGroup container) {
        if (view == null) {
            view = newView(inflater, position, container);
            if (view == null) {
                throw new IllegalStateException("newView result must not be null.");
            }
        }

        bindView(getItem(position), position, view);
        return view;
    }

    public void notifyDatasourceError(Exception e) {

    }

    /**
     * Create a new instance of a view for the specified position.
     */
    private View newView(LayoutInflater inflater, int position, ViewGroup container) {
        return inflater.inflate(viewId, container, false);
    }

    /**
     * Bind the data for the specified {@code position} to the view.
     */
    public abstract void bindView(T item, int position, View view);

    /**
     * Callback interface for datasource operations. All callbacks will be called on UI Thread
     */
    public interface Callback {
        void onPageRequested();

        void onDataAvailable();

        void onDatasourceError(Exception e);
    }
}