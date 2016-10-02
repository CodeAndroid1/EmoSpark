package ibmmobileappbuilder.validation;

import android.view.View;

import ibmmobileappbuilder.views.DatePicker;

/**
 * Validator that uses a {@link DatePicker} for errors
 */
public abstract class DatePickerValidator<T> implements Validator<T> {

    private final View mView;
    private final int mPickerId;
    private final int mErrorMsgId;

    public DatePickerValidator(View rootView, int pickerId, int errorMsgId) {
        mView = rootView;
        mPickerId = pickerId;
        mErrorMsgId = errorMsgId;
    }

    @Override
    public void setError(boolean show) {
        DatePicker picker = (DatePicker) mView.findViewById(mPickerId);
        if (show) {
            picker.setError(mErrorMsgId);
        } else {
            picker.setError(null);
        }
    }
}
