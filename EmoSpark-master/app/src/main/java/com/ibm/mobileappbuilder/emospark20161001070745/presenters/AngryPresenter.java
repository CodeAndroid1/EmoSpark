
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo4DSItem;

import java.util.List;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.ListCrudPresenter;
import ibmmobileappbuilder.mvp.view.CrudListView;

public class AngryPresenter extends BasePresenter implements ListCrudPresenter<Emo4DSItem>,
      Datasource.Listener<Emo4DSItem>{

    private final CrudDatasource<Emo4DSItem> crudDatasource;
    private final CrudListView<Emo4DSItem> view;

    public AngryPresenter(CrudDatasource<Emo4DSItem> crudDatasource,
                                         CrudListView<Emo4DSItem> view) {
       this.crudDatasource = crudDatasource;
       this.view = view;
    }

    @Override
    public void deleteItem(Emo4DSItem item) {
        crudDatasource.deleteItem(item, this);
    }

    @Override
    public void deleteItems(List<Emo4DSItem> items) {
        crudDatasource.deleteItems(items, this);
    }

    @Override
    public void addForm() {
        view.showAdd();
    }

    @Override
    public void editForm(Emo4DSItem item, int position) {
        view.showEdit(item, position);
    }

    @Override
    public void detail(Emo4DSItem item, int position) {
        view.showDetail(item, position);
    }

    @Override
    public void onSuccess(Emo4DSItem item) {
                view.showMessage(R.string.items_deleted);
        view.refresh();
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic);
    }

}

