package ibmmobileappbuilder.behaviors;

import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.util.ColorUtils;

/**
 * Creates a Share action provider
 */
public class ShareBehavior extends NoOpBehavior {

    private final ShareListener mListener;
    Context mContext;

    public ShareBehavior(Context context, ShareListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.add(R.string.share);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item.setIcon(R.drawable.abc_ic_menu_share_mtrl_alpha);
        ColorUtils.tintIcon(item, R.color.textBarColor, mContext);

        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mListener.onShare();
                return true;
            }
        });
    }

    public interface ShareListener{
        /**
         * Updates this sharing action
         */
        void onShare();
    }
}
