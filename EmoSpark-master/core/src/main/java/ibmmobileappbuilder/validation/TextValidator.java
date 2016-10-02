package ibmmobileappbuilder.validation;

import android.support.design.widget.TextInputLayout;
import android.view.View;

/**
 * Validator that uses a {@link TextInputLayout} for errors
 */
public abstract class TextValidator<T> implements Validator<T> {

    private final View mView;
    private final int mLayoutResId;
    private final int mErrorMsgId;

    public TextValidator(View rootView, int layoutResId, int errorMsgId) {
        mView = rootView;
        mLayoutResId = layoutResId;
        mErrorMsgId = errorMsgId;
    }

    @Override
    public void setError(boolean show) {
        TextInputLayout layout = (TextInputLayout) mView.findViewById(mLayoutResId);
        if (show) {
            layout.setError(mView.getContext().getString(mErrorMsgId));
        } else {
            layout.setError(null);
        }
    }
}
