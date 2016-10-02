package ibmmobileappbuilder.util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.actions.StartActivityAction;
import ibmmobileappbuilder.ui.DetailFragment;

public class NavigationUtils {
    @SuppressWarnings("unchecked")
    public static void navigateToDetail(Context context, Class activityClass, Class fragmentClass, Bundle args) {
        if (context.getResources().getBoolean(R.bool.tabletLayout) && context instanceof FragmentActivity) {
            FragmentManager fragmentManager = ((FragmentActivity) context).getSupportFragmentManager();
            DetailFragment fr = (DetailFragment) fragmentManager.findFragmentById(R.id.detail_frame);
            if (fr == null || !fr.getClass().equals(fragmentClass)) {
                FragmentTransaction replaceTransaction = fragmentManager.beginTransaction();
                replaceTransaction.replace(R.id.detail_frame,
                        FragmentUtils.instantiate(fragmentClass, args)
                );
                replaceTransaction.commit();
            } else {
                fr.setItem(args.getParcelable(Constants.CONTENT));
            }
        } else {
            // show detail activity.
            Intent intent = new Intent(context, activityClass);
            intent.putExtras(args);
            context.startActivity(intent);
        }
    }

    public static <T> void showAddOrUpdateItem(T item,
                                               int position,
                                               FragmentActivity activity,
                                               Class activityClass) {
        Bundle args = new Bundle();
        args.putInt(Constants.ITEMPOS, position);
        args.putParcelable(Constants.CONTENT, (Parcelable) item);
        int requestCode = item == null ? Constants.MODE_CREATE : Constants.MODE_EDIT;
        args.putInt(Constants.MODE, requestCode);

        new StartActivityAction(activityClass, args, requestCode).execute(activity);
    }

    public static <T> Intent generateIntentToAddOrUpdateItem(T item,
                                                             int position,
                                                             FragmentActivity activity,
                                                             Class activityClass) {
        Bundle args = new Bundle();
        args.putInt(Constants.ITEMPOS, position);
        args.putParcelable(Constants.CONTENT, (Parcelable) item);
        Intent intent = new Intent(activity, activityClass);
        intent.putExtras(args);
        return intent;
    }
}
