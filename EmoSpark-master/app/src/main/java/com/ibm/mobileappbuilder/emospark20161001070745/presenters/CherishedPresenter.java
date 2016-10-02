
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo1DSItem;

import java.util.List;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.ListCrudPresenter;
import ibmmobileappbuilder.mvp.view.CrudListView;

public class CherishedPresenter extends BasePresenter implements ListCrudPresenter<Emo1DSItem>,
      Datasource.Listener<Emo1DSItem>{

    private final CrudDatasource<Emo1DSItem> crudDatasource;
    private final CrudListView<Emo1DSItem> view;

    public CherishedPresenter(CrudDatasource<Emo1DSItem> crudDatasource,
                                         CrudListView<Emo1DSItem> view) {
       this.crudDatasource = crudDatasource;
       this.view = view;
    }

    @Override
    public void deleteItem(Emo1DSItem item) {
        crudDatasource.deleteItem(item, this);
    }

    @Override
    public void deleteItems(List<Emo1DSItem> items) {
        crudDatasource.deleteItems(items, this);
    }

    @Override
    public void addForm() {
        view.showAdd();
    }

    @Override
    public void editForm(Emo1DSItem item, int position) {
        view.showEdit(item, position);
    }

    @Override
    public void detail(Emo1DSItem item, int position) {
        view.showDetail(item, position);
    }

    @Override
    public void onSuccess(Emo1DSItem item) {
                view.showMessage(R.string.items_deleted);
        view.refresh();
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic);
    }

}

