package ibmmobileappbuilder.mvp.presenter;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.view.FormView;

public class DefaultFormPresenter<T> extends BaseFormPresenter<T> {

    private final CrudDatasource<T> datasource;

    public DefaultFormPresenter(CrudDatasource<T> datasource, FormView<T> view) {
        super(view);
        this.datasource = datasource;
    }

    @Override
    public void deleteItem(T item) {
        datasource.deleteItem(item, new OnItemDeletedListener());
    }

    @Override
    public void save(T item) {
        // validate
        if (validate(item)) {
            datasource.updateItem(item, new OnItemUpdatedListener());
        } else {
            this.view.showMessage(R.string.correct_errors, false);
        }
    }

    @Override
    public void create(T item) {
        if (validate(item)) {
            datasource.create(item, new OnItemCreatedListener());
        } else {
            this.view.showMessage(R.string.correct_errors, false);
        }
    }

    private class OnItemDeletedListener extends ShowingErrorOnFailureListener {
        @Override
        public void onSuccess(T item) {
            view.showMessage(R.string.item_deleted, true);
            view.close(true);
        }
    }

    private class OnItemUpdatedListener extends ShowingErrorOnFailureListener {
        @Override
        public void onSuccess(T item) {
            view.setItem(item);
            view.showMessage(R.string.item_updated, true);
            view.close(true);
        }

    }

    private class OnItemCreatedListener extends ShowingErrorOnFailureListener {
        @Override
        public void onSuccess(T item) {
            view.setItem(item);
            view.showMessage(R.string.item_created, true);
            view.close(true);
        }
    }

    private abstract class ShowingErrorOnFailureListener implements Datasource.Listener<T> {
        @Override
        public void onFailure(Exception e) {
            view.showMessage(R.string.error_data_generic, true);
        }
    }

}
