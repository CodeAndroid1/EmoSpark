package ibmmobileappbuilder.behaviors;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import java.lang.ref.WeakReference;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ui.ListGridFragment;

/**
 * Pull to refresh pattern for listing fragments
 */
public class SwipeRefreshBehavior extends NoOpBehavior implements SwipeRefreshLayout.OnRefreshListener {

    private final WeakReference<ListGridFragment> mWeakFragment;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    public SwipeRefreshBehavior(ListGridFragment<?> fragment) {
        this.mWeakFragment = new WeakReference<ListGridFragment>(fragment);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListGridFragment fr = mWeakFragment.get();

        if (fr != null) {
            //setup
            mSwipeRefreshLayout = new SwipeRefreshLayout(fr.getActivity());
            mSwipeRefreshLayout.setOnRefreshListener(this);

            //layout modification
            AbsListView listView = (AbsListView) view.findViewById(android.R.id.list);
            ViewGroup parent = ((ViewGroup) listView.getParent());
            parent.removeView(listView);
            parent.addView(mSwipeRefreshLayout, 0);
            mSwipeRefreshLayout.addView(listView);

            //color customization
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.material_deep_teal_500,
                    R.color.material_deep_teal_200,
                    R.color.material_blue_grey_800);
        }
    }

    @Override
    public void onRefresh() {
        ListGridFragment fr = mWeakFragment.get();

        if (fr != null) {
            fr.getAdapter().registerDataSetObserver(new DataSetObserver() {
                // wait async for completion
                @Override
                public void onChanged() {
                    super.onChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
            fr.refresh();
        }

    }
}
