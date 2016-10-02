/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ds.Pagination;
import ibmmobileappbuilder.ds.SearchOptions;
import ibmmobileappbuilder.ds.filter.Filter;
import ibmmobileappbuilder.mvp.view.DetailView;
import ibmmobileappbuilder.util.Constants;

/**
 * Fragments to show a detail view inside a {@link android.support.v4.app.Fragment}
 * <p/>
 * DetailFragments expect the {@link Constants#CONTENT} to be passed in
 * That argument is optional, given that you implement the {@link #getDatasource()}
 * method.
 */
public abstract class DetailFragment<T>
        extends BaseFragment
        implements Filterable, DetailView, Refreshable {

    private int itemPos;
    private Datasource<T> datasource;
    protected T item;

    /**
     * Containers for views
     */
    private View contentContainer;
    private View progressContainer;

    // Search options for filtering
    private SearchOptions searchOptions;

    public DetailFragment() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onCreate(Bundle state) {
        super.onCreate(state);
        Bundle args = getArguments();

        // restore state either from savedState or defaults
        if (state != null) {
            item = state.getParcelable(Constants.CONTENT);
        }
        if (item == null) {
            item = args.getParcelable(Constants.CONTENT);
        }

        itemPos = args.getInt(Constants.ITEMPOS, 0);
    }

    /**
     * Trick to know when the fragment is being shown (onResume doesn't work for this when the
     * fragment is in the context of a viewpager)
     * This method is highly dependant on
     * {@link android.support.v4.app.FragmentStatePagerAdapter#setPrimaryItem(ViewGroup,
     * int, Object)}
     * but works in all cases.
     * {@inheritDoc}
     */
    @Override
    public void setMenuVisibility(boolean visible) {
        super.setMenuVisibility(visible);
        if (visible && item != null) {
            onShow(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        progressContainer = view.findViewById(R.id.progressContainer);
        contentContainer = view.findViewById(R.id.contentContainer);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedState) {
        super.onViewCreated(view, savedState);
        initView(view, savedState);
    }

    /**
     * Init this view for details.
     *
     * @param view
     * @param savedState
     */
    protected void initView(View view, Bundle savedState) {
        // we put this here to ensure all onCreate chain has been called,
        // and so getDatasource will return a valid (non null) value
        datasource = getDatasource();

        if (item != null) {
            bindView(item, view);
            setContentShown(true);
        } else {
            refresh();
        }
    }

    /**
     * Updates the item being shown, and perform the appropiate binding
     *
     * @param newItem the new item to show
     */
    public void setItem(T newItem) {
        item = newItem;

        View v = getView();
        if (v != null) {   // only bind to view if it already exists
            bindView(item, v);
            setContentShown(true);
            onShow(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save the current item into outState
        outState.putParcelable(Constants.CONTENT, (Parcelable) item);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onResume() {
        super.onResume();

        if (item != null) {
            onShow(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        datasource = null;
        item = null;
    }

    // Filterable interface

    protected Datasource.Listener<T> dataListener = new Datasource.Listener<T>() {
        @Override
        public void onSuccess(final T result) {
            item = result;
            final View v = getView();
            // force UI thread
            Activity act = getActivity();
            if (act != null) {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (v != null) {   // only bind to view if it already exists
                            bindView(result, v);
                        }
                        setContentShown(true);
                    }
                });
            }
        }

        @Override
        public void onFailure(Exception e) {
            // force UI thread
            Activity act = getActivity();
            if (act != null) {
                act.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // inform to users
//                        BusProvider.getInstance().post(new DatasourceFailureEvent());
                        setContentShown(false);
                    }
                });
            }
        }
    };

    protected Datasource.Listener<List<T>> dataListListener = new Datasource.Listener<List<T>>() {

        @Override
        public void onSuccess(List<T> ts) {
            if (ts.size() > 0) {
                dataListener.onSuccess(ts.get(0));
            } else {
                dataListener.onSuccess(null);
            }
        }

        @Override
        public void onFailure(Exception e) {
            dataListener.onFailure(e);
        }
    };

    public void refresh() {
        if (datasource != null) {
            setContentShown(false);
            //TODO is this a good idea???
            if (searchOptions != null && datasource instanceof Pagination) {
                // don't use itemPos and take the first item
                ((Pagination<T>) datasource).getItems(0, dataListListener);
            } else {
                datasource.getItem(String.valueOf(itemPos), dataListener);
            }
        } else {
            throw new IllegalStateException("Either Item or Datasource should be implemented");
        }
    }

    @Override
    public void onSearchTextChanged(String s) {
        ensureSearchOptions();
        searchOptions.setSearchText(s);
        refresh();
    }

    @Override
    public void addFilter(Filter filter) {
        ensureSearchOptions();
        searchOptions.addFilter(filter);
    }

    @Override
    public void clearFilters() {
        searchOptions.setFilters(null);
    }

    private void ensureSearchOptions() {
        if (searchOptions == null)
            searchOptions = new SearchOptions();
    }

    @Override
    public void showMessage(int message, boolean toast) {
        if (toast) {
            Toast.makeText(getActivity(), getString(message), Toast.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getView(), getString(message), Snackbar.LENGTH_LONG).show();
        }
    }

    protected void setContentShown(boolean shown) {
        if (progressContainer != null && contentContainer != null) {
            progressContainer.setVisibility(shown ? View.GONE : View.VISIBLE);
            contentContainer.setVisibility(shown ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void navigateToEditForm() {
        // do nothing. implemented in subclasses
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if the item is updated, we refresh the view
        if (requestCode == Constants.MODE_EDIT && resultCode == Constants.CONTENT_UPDATED) {

            T item = (T) data.getParcelableExtra(Constants.CONTENT);
            if (item != null)
                setItem(item);

            // set our result code (in phone mode, we have our own activity)
            getActivity().setResult(resultCode, data);
        }
    }

    @Override
    public void close(boolean shouldRefresh) {
        // close detail activity if we are in phone mode
        // or a navigation activity is not the fragment owner
        Activity act = getActivity();

        if (!getResources().getBoolean(R.bool.tabletLayout) ||
                act instanceof BaseDetailActivity) {
            // TODO: Support tablet layouts
            Intent data = new Intent();
            data.putExtra(Constants.CONTENT, (Parcelable) (shouldRefresh ? null : getItem()));
            act.setResult(Constants.CONTENT_UPDATED, data);
            act.finish();
        }
    }

    /**
     * Gets the current item
     *
     * @return the current item, or null if it's not been retrieved yet
     */
    public T getItem() {
        return item;
    }

    /**
     * called when a view is ready to be binded to data, but maybe it's not
     * showing yet (i.e. in a {@link android.support.v4.view.ViewPager})
     *
     * @param item an instance of T data
     * @param view the view to bind data to
     */
    public abstract void bindView(T item, View view);

    /**
     * This method will return a datasource for data retrieval
     *
     * @return a datasource
     */
    public Datasource<T> getDatasource() {
        return null;
    }

    /**
     * Called whenever a fragment is shown to the user
     *
     * @param item the item being shown
     */
    protected void onShow(T item) {
    }

    ;

    /**
     * The layout for this fragment
     *
     * @return the layout id for this screen
     */
    protected abstract int getLayout();

}
