package ibmmobileappbuilder.validation;

import android.view.View;

import ibmmobileappbuilder.views.GeoPicker;

/**
 * Validator that uses a {@link GeoPicker} for errors
 */

public abstract class GeoPointValidator<T> implements Validator<T> {

    private final View mView;
    private final int mMsgId;
    private final int mPickerId;

    public GeoPointValidator(View rootView, int viewId, int errorMsgId) {
        mView = rootView;
        mPickerId = viewId;
        mMsgId = errorMsgId;
    }

    @Override
    public void setError(boolean show) {
        GeoPicker picker = (GeoPicker) mView.findViewById(mPickerId);
        if (show) {
            picker.setError(mMsgId);
        } else {
            picker.setError(null);
        }
    }
}
