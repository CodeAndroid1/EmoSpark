
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo3DSItem;

import java.util.List;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.ListCrudPresenter;
import ibmmobileappbuilder.mvp.view.CrudListView;

public class DepressedPresenter extends BasePresenter implements ListCrudPresenter<Emo3DSItem>,
      Datasource.Listener<Emo3DSItem>{

    private final CrudDatasource<Emo3DSItem> crudDatasource;
    private final CrudListView<Emo3DSItem> view;

    public DepressedPresenter(CrudDatasource<Emo3DSItem> crudDatasource,
                                         CrudListView<Emo3DSItem> view) {
       this.crudDatasource = crudDatasource;
       this.view = view;
    }

    @Override
    public void deleteItem(Emo3DSItem item) {
        crudDatasource.deleteItem(item, this);
    }

    @Override
    public void deleteItems(List<Emo3DSItem> items) {
        crudDatasource.deleteItems(items, this);
    }

    @Override
    public void addForm() {
        view.showAdd();
    }

    @Override
    public void editForm(Emo3DSItem item, int position) {
        view.showEdit(item, position);
    }

    @Override
    public void detail(Emo3DSItem item, int position) {
        view.showDetail(item, position);
    }

    @Override
    public void onSuccess(Emo3DSItem item) {
                view.showMessage(R.string.items_deleted);
        view.refresh();
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic);
    }

}

