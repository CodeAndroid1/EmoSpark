package ibmmobileappbuilder.util;

import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Implementation of the ViewHolder pattern for reusing views in listings/grids
 */
@SuppressWarnings("unchecked")
public class ViewHolder {

    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        resetView(childView);

        return (T) childView;
    }

    /**
     * Reset view content for recycling
     *
     * @param view the view to reset
     */
    private static void resetView(View view) {
        if (view instanceof TextView) {
            ((TextView) view).setText(null);
        } else if(view instanceof ImageButton){
            return;
        }
        else if (view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(null);
        }
        if(view instanceof Checkable){
            ((Checkable) view).setChecked(false);
        }
    }
}
