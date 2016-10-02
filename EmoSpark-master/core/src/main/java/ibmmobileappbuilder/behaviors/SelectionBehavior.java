package ibmmobileappbuilder.behaviors;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;

import java.util.ArrayList;
import java.util.List;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ui.ListGridFragment;
import ibmmobileappbuilder.util.ColorUtils;

/**
 * A behavior that enables the Contextual Action Bar (CAB) in its activity. At the moment,
 * only ListViews are supported
 */
public class SelectionBehavior<T> extends NoOpBehavior {

    private final ListGridFragment fragment;
    private final int mTitleRes;

    private Callback<T> mCallback;

    private AbsListView mView;
    private int mIconRes;

    public SelectionBehavior(ListGridFragment<T> fr, int titleRes) {
        this.fragment = fr;
        this.mTitleRes = titleRes;
    }

    public SelectionBehavior(ListGridFragment<T> fr, int titleRes, int iconRes) {
        this.fragment = fr;
        this.mTitleRes = titleRes;
        mIconRes = iconRes;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        View lv = view.findViewById(android.R.id.list);

        if (lv instanceof AbsListView) {
            mView = (AbsListView) lv;
            mView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
            mView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                    // nothing to do
                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                    MenuItem item = menu.add(mTitleRes);
                    if(mIconRes != 0) {
                        item.setIcon(mIconRes);
                        ColorUtils.tintIcon(item, R.color.textBarColor, fragment.getActivity());
                    }
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                    // call custom callback
                    if (mCallback != null) {
                        ArrayList<T> res = getCheckedItems();
                        mCallback.onSelected(res);
                        mode.finish();
                        return true;
                    }

                    return false;
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {
                }

                @SuppressWarnings("unchecked")
                private ArrayList<T> getCheckedItems() {
                    SparseBooleanArray checked = mView.getCheckedItemPositions();
                    ArrayList<T> res = new ArrayList<T>(checked.size());

                    for (int i = 0; i < checked.size(); i++) {
                        boolean value = checked.get(checked.keyAt(i));
                        if (value)
                            res.add((T) fragment.getAdapter().getItem(checked.keyAt(i)));
                    }
                    return res;
                }
            });
        }
    }

    public void setCallback(Callback<T> callback) {
        this.mCallback = callback;
    }

    public interface Callback<T> {

        void onSelected(List<T> selectedItems);
    }
}
