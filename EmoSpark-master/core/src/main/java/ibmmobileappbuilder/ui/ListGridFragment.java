/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import ibmmobileappbuilder.adapters.DatasourceAdapter;
import ibmmobileappbuilder.adapters.PaginationAwareAdapter;
import ibmmobileappbuilder.behaviors.AnalyticsSwipeRefreshBehavior;
import ibmmobileappbuilder.behaviors.Behavior;
import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.ds.Pagination;
import ibmmobileappbuilder.ds.SearchOptions;
import ibmmobileappbuilder.ds.filter.DateRangeFilter;
import ibmmobileappbuilder.ds.filter.Filter;
import ibmmobileappbuilder.ds.filter.StringListFilter;
import ibmmobileappbuilder.mvp.presenter.ListCrudPresenter;
import ibmmobileappbuilder.util.Constants;
import ibmmobileappbuilder.util.EndlessScrollListener;

/**
 * A fragment representing a list of Items that come from a datasource. Subclasses must
 * implement the {@link ListGridFragment#getDatasource()}, and
 * {@link #bindView(Object, View, int)} methods.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView, dinamically.
 * <p/>
 */
public abstract class ListGridFragment<T>
        extends BaseFragment
        implements Refreshable, Filterable, ibmmobileappbuilder.mvp.view.ListView<T>,
        AbsListView.OnItemClickListener, AbsListView.OnItemLongClickListener {

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView listView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private DatasourceAdapter<T> adapter;
    private Datasource<T> datasource;

    // set to true when the fragment is created the first time (this fragment is sticky)
    private boolean justCreated;

    /**
     * Loading more... item
     */
    private View footer;

    /**
     * List header
     */
    private int headerRes;
    private View headerView;
    private EndlessScrollListener scrollListener;
    private ProgressBar progressView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // lists are refreshable
        addBehavior(new AnalyticsSwipeRefreshBehavior(this));

        // we set this fragments sticky to cache list contents on configuration changes
        setRetainInstance(true);
        justCreated = true;

        // create datasource and adapter
        datasource = getDatasource();
        // adapter instantation
        adapter = createAdapter();
        adapter.setCallback(
                new DatasourceAdapter.Callback() {
                    @Override
                    public void onDataAvailable() {
                        setListShown(true);
                        showFooter(false);

                        // inform the scroll listener so that it continues processing scrolls
                        if (scrollListener != null) {
                            scrollListener.finishLoading();
                        }
                    }

                    @Override
                    public void onPageRequested() {
                        showFooter(true);
                    }

                    @Override
                    public void onDatasourceError(Exception e) {
                        setListShown(true);
                        showFooter(false);
                    }
                }
        );
    }

    private void showFooter(boolean show) {
        if (listView.getVisibility() == View.VISIBLE) {
            if (footer != null) {
                if (show) {
                    ((ListView) listView).addFooterView(footer);
                } else {
                    ((ListView) listView).removeFooterView(footer);
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getLayout(), container, false);

        // Set up the listview
        listView = (AbsListView) view.findViewById(android.R.id.list);
        progressView = (ProgressBar) view.findViewById(R.id.progressContainer);

        // Set OnItemClickListener so we can be notified on item clicks
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        // set up pagination
        if (datasource instanceof Pagination) {
            scrollListener = new EndlessScrollListener() {
                @Override
                public void onLoadMore(int page, int totalItemsCount) {
                    ((PaginationAwareAdapter) adapter).loadNextPage();
                }
            };

            listView.setOnScrollListener(scrollListener);

            // set the footer loading more... item
            // this must be done BEFORE assigning the adapter (in KitKat this is fixed)
            if (listView instanceof ListView) {
                footer = inflater.inflate(R.layout.list_footer, null, false);
                ((ListView) listView).addFooterView(footer);
            }
        }

        // register header
        if (listView instanceof ListView && headerRes != 0) {
            headerView = inflater.inflate(headerRes, null, false);
            ((ListView) listView).addHeaderView(headerView, null, false);
        }

        // register adapter (after any header or footer)
        listView.setAdapter(adapter);

        // again, remove the footer from the list
        if (datasource instanceof Pagination && listView instanceof ListView) {
            ((ListView) listView).removeFooterView(footer);
        }

        View emptyView = view.findViewById(android.R.id.empty);
        if (emptyView != null) {
            listView.setEmptyView(emptyView);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        // load first batch of data
        if (justCreated) {
            refresh(false);
        }
    }

    @Override
    public void onPause() {  
        super.onPause();

        // set the created flag off (this fragment is retained)
        justCreated = false;
    }

    @Override
    public void onDestroy() {
        // this actually is not called, since this fragment is retained
        datasource = null;
        adapter = null;

        super.onDestroy();
    }

    protected DatasourceAdapter<T> createAdapter() {
        if (datasource instanceof Pagination) {
            return new PaginationAwareAdapter<T>(getActivity(), getItemLayout(), datasource) {
                @Override
                public void bindView(T item, int position, View view) {
                    ListGridFragment.this.bindView(item, view, position);
                }
            };
        } else {
            return new DatasourceAdapter<T>(getActivity(), getItemLayout(), datasource) {

                @Override
                public void bindView(T item, int position, View view) {
                    ListGridFragment.this.bindView(item, view, position);
                }
            };
        }
    }

    protected abstract SearchOptions getSearchOptions();

    private void setListShown(boolean shown) {
        if (progressView != null && listView != null) {
            View emptyView = listView.getEmptyView();
            if (emptyView != null && adapter != null) {
                //replicating empty view functionality here
                emptyView.setVisibility(shown && adapter.isEmpty() ? View.VISIBLE : View.GONE);
            }
            progressView.setVisibility(shown ? View.GONE : View.VISIBLE);
            listView.setVisibility(shown ? View.VISIBLE : View.GONE);
        }
    }

    public AbsListView getListView() {
        return listView;
    }

    public void setHeaderResource(int resource) {
        this.headerRes = resource;
    }

    public View getHeaderView() {
        return headerView;
    }

    // Click listeners
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        itemClicked((T) parent.getAdapter().getItem(position), position);
    }

    // Default click implementation (without presenter)
    protected void itemClicked(T item, int position) {
        showDetail(item, position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if we did launch an activity for editing, this will refresh on return
        if (resultCode == Constants.CONTENT_UPDATED) {
            refresh();
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        boolean res = false;
        for (Behavior b : behaviors) {
            res = b.onItemLongClick(parent, view, position, id) || res;
        }
        return res;
    }

    // Refreshable interface
    @Override
    public void refresh() {
        refresh(true);
    }

    private void refresh(boolean forceRefresh) {
        setListShown(false);
        listView.clearChoices();
        adapter.refresh(forceRefresh);
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = listView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    @Override
    public void showDetail(T item, int position) {

    }

    @Override
    public void showMessage(int messageRes) {
        Snackbar.make(getView(), getString(messageRes), Snackbar.LENGTH_LONG).show();
    }

    /**
     * get the adapter this fragment is attached to
     *
     * @return the adapter
     */
    public DatasourceAdapter<T> getAdapter() {
        return adapter;
    }

    /**
     * set the filtering criteria for the adapter. This criteria belongs to the page
     */
    @Override
    public void onSearchTextChanged(String searchText) {
        //we are always using the same searchOptions, so this can be applied to the searchOptions objects
        datasource.onSearchTextChanged(searchText);
        refresh();
    }

    @Override
    public void addFilter(Filter filter) {
        datasource.addFilter(filter);
    }

    @Override
    public void clearFilters() {
        datasource.clearFilters();
    }

    public void addStringFilter(String field, List<String> values) {
        datasource.addFilter(new StringListFilter(field, values));
    }

    public void addDateRangeFilter(String field, long value1, long value2) {
        Date min = (value1 != -1) ? new Date(value1) : null;
        Date max = (value2 != -1) ? new Date(value2) : null;
        datasource.addFilter(new DateRangeFilter(field, min, max));
    }


    @Override
    public ListCrudPresenter<T> getPresenter() {
        return (ListCrudPresenter<T>) super.getPresenter();
    }

    // Delegates

    /**
     * Get the datasource for this list
     *
     * @return a @link Datasource object
     */
    protected abstract Datasource<T> getDatasource();

    /**
     * Get the layout for this fragment.
     *
     * @return a valid layout for lists (fragment_list, fragment_grid3cols and fragment_grid4cols)
     */
    protected abstract @LayoutRes int getLayout();

    /**
     * Get the layout for this list's items
     *
     * @return the item layout
     */
    protected abstract @LayoutRes int getItemLayout();

    /**
     * Binds the item layout to each list item.
     *
     * @param item     The item to bind to the view
     * @param view     The view inflated from #getItemLayout()
     * @param position the current position
     */
    protected abstract void bindView(T item, View view, int position);

}
