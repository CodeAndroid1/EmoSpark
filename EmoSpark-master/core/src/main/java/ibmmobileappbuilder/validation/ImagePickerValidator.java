package ibmmobileappbuilder.validation;

import android.view.View;

import ibmmobileappbuilder.views.DatePicker;
import ibmmobileappbuilder.views.ImagePicker;

/**
 * Validator that uses a {@link DatePicker} for errors
 */
public abstract class ImagePickerValidator<T> implements Validator<T> {

    private final View mView;
    private final int mPickerId;
    private final int mErrorMsgId;

    public ImagePickerValidator(View rootView, int pickerId, int errorMsgId) {
        mView = rootView;
        mPickerId = pickerId;
        mErrorMsgId = errorMsgId;
    }

    @Override
    public void setError(boolean show) {
        ImagePicker picker = (ImagePicker) mView.findViewById(mPickerId);
        if (show) {
            picker.setError(mErrorMsgId);
        } else {
            picker.setError(null);
        }
    }
}
