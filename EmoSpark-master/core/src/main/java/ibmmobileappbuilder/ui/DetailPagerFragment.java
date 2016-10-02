/*
 * Copyright 2016.
 * This code is part of IBM Mobile App Builder
 */

package ibmmobileappbuilder.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ds.Count;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.util.Constants;
import ibmmobileappbuilder.util.DepthPageTransformer;

/**
 * A fragment that shows sibling detail views with nested fragments in
 * a {@link ViewPager},
 * representing an item that come from a datasource. Subclasses must implement the method
 * {@link #getDatasource()}.
 * This fragment expects an argument {@link Constants#ITEMPOS}
 * to be passed in
 */
public abstract class DetailPagerFragment<T> extends BaseFragment {

    int mPos = 0;

    ViewPager mPager;

    PagerAdapter mAdapter;

    Datasource<T> mDatasource;

    /**
     * By default, the pager calls to datasource's getCount() method, but some datasources
     * don't implement that interface. In that case, a SIZE
     * argument ({@link Constants}) can be passed to this fragment,
     * forcing a size for the ViewPager.
     */
    private int mSize;

    // empty constructor
    public DetailPagerFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mPos = arguments.getInt(Constants.ITEMPOS, 0);
            mSize = arguments.getInt(Constants.SIZE, 1);
        }

        // restore state
        if (state != null) {
            mPos = state.getInt(Constants.ITEMPOS, mPos);
        }

        mDatasource = getDatasource();
        mAdapter = createPagerAdapter();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.ITEMPOS, mPager.getCurrentItem());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.detail_pager, container, false);

        // inject views
        mPager = (ViewPager) view.findViewById(R.id.pager);

        // set current page
        mPager.setAdapter(mAdapter);
        mPager.setCurrentItem(mPos);

        // set animation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mPager.setPageTransformer(true, new DepthPageTransformer());
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        mPager = null;
        mAdapter = null;
        mDatasource = null;
        super.onDestroy();
    }

    protected PagerAdapter createPagerAdapter() {

        return new FragmentStatePagerAdapter(
                getChildFragmentManager()) {

            @Override
            public Fragment getItem(int i) {
                // to avoid memory lakes, implement as static inner class, and use weakreferences
                final Fragment fr = DetailPagerFragment.this.getCardFragment(i);

                if (fr != null) {
                    Bundle args = new Bundle();
                    args.putInt(Constants.ITEMPOS, i);
                    fr.setArguments(args);

                    mDatasource.getItem(String.valueOf(i), new Datasource.Listener<T>() {
                        @Override
                        @SuppressWarnings("unchecked")
                        public void onSuccess(T t) {
                            ((DetailFragment<T>) fr).setItem(t);    // update item
                        }

                        @Override
                        public void onFailure(Exception e) {

                        }
                    });
                }

                return fr;
            }

            @Override
            public int getCount() {
                // if datasource supports counting, ask for it
                if (mDatasource != null && mDatasource instanceof Count) {
                    return ((Count) mDatasource).getCount();
                } else {
                    return mSize;   // why do we want a pager for just one page?
                }
            }
        };
    }

    /**
     * Get the Fragment to show in a page in this ViewPager.
     *
     * @param i the position
     * @return the fragment. See {@link DetailFragment}
     */
    protected abstract Fragment getCardFragment(int i);

    /**
     * This is called to get the {@link Datasource} this detail item belongs to
     *
     * @return a datasource
     */
    protected abstract Datasource<T> getDatasource();
}
