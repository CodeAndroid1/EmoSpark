package ibmmobileappbuilder.mvp.presenter;

import android.util.SparseArray;

import ibmmobileappbuilder.mvp.view.FormView;
import ibmmobileappbuilder.validation.Validator;

public abstract class BaseFormPresenter<T> extends BasePresenter implements FormPresenter<T> {

    private SparseArray<Validator<T>> validators;
    protected final FormView<T> view;

    public BaseFormPresenter(FormView<T> view) {
        this.view = view;
    }

    @Override
    public void cancel() {
        view.close(false);
    }

    @Override
    public void addValidator(int viewId, Validator<T> validator) {
        if (this.validators == null) {
            this.validators = new SparseArray<>();
        }

        this.validators.put(viewId, validator);
    }

    @Override
    public boolean validate(T item) {
        boolean res = true;
        if (validators != null) {
            for (int i = 0; i < validators.size(); i++) {
                Validator<T> val = validators.get(validators.keyAt(i));
                if (!val.validate(item)) {
                    val.setError(true);
                    res = false;
                } else {
                    val.setError(false);
                }
            }
        }

        return res;
    }

}
