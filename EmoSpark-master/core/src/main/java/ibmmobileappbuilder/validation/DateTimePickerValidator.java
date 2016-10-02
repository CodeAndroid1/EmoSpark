package ibmmobileappbuilder.validation;

import android.view.View;

import ibmmobileappbuilder.views.DateTimePicker;

/**
 * Validator that uses a {@link DateTimePicker} for errors
 */
public abstract class DateTimePickerValidator<T> implements Validator<T> {

    private final View mView;
    private final int mPickerId;
    private final int mErrorMsgId;

    public DateTimePickerValidator(View rootView, int pickerId, int errorMsgId) {
        mView = rootView;
        mPickerId = pickerId;
        mErrorMsgId = errorMsgId;
    }

    @Override
    public void setError(boolean show) {
        DateTimePicker picker = (DateTimePicker) mView.findViewById(mPickerId);
        if (show) {
            picker.setError(mErrorMsgId);
        } else {
            picker.setError(null);
        }
    }
}
