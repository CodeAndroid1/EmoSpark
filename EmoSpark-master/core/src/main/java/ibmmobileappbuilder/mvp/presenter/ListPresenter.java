package ibmmobileappbuilder.mvp.presenter;

import java.util.List;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.view.CrudListView;

public class ListPresenter<T> extends BasePresenter implements ListCrudPresenter<T>, Datasource.Listener<T> {

    private final CrudDatasource<T> crudDatasource;
    private final CrudListView<T> view;

    public ListPresenter(CrudDatasource<T> crudDatasource, CrudListView<T> view) {
        this.crudDatasource = crudDatasource;
        this.view = view;
    }

    @Override
    public void deleteItem(T item) {
        crudDatasource.deleteItem(item, this);
    }

    @Override
    public void deleteItems(List<T> items) {
        crudDatasource.deleteItems(items, this);
    }

    @Override
    public void addForm() {
        view.showAdd();
    }

    @Override
    public void editForm(T item, int position) {
        view.showEdit(item, position);
    }

    @Override
    public void detail(T item, int position) {
        view.showDetail(item, position);
    }

    @Override
    public void onSuccess(T t) {
        view.showMessage(R.string.items_deleted);
        view.refresh();
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic);
    }
}


