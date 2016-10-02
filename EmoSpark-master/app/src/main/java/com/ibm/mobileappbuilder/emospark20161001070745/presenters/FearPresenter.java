
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo5DSItem;

import java.util.List;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.ListCrudPresenter;
import ibmmobileappbuilder.mvp.view.CrudListView;

public class FearPresenter extends BasePresenter implements ListCrudPresenter<Emo5DSItem>,
      Datasource.Listener<Emo5DSItem>{

    private final CrudDatasource<Emo5DSItem> crudDatasource;
    private final CrudListView<Emo5DSItem> view;

    public FearPresenter(CrudDatasource<Emo5DSItem> crudDatasource,
                                         CrudListView<Emo5DSItem> view) {
       this.crudDatasource = crudDatasource;
       this.view = view;
    }

    @Override
    public void deleteItem(Emo5DSItem item) {
        crudDatasource.deleteItem(item, this);
    }

    @Override
    public void deleteItems(List<Emo5DSItem> items) {
        crudDatasource.deleteItems(items, this);
    }

    @Override
    public void addForm() {
        view.showAdd();
    }

    @Override
    public void editForm(Emo5DSItem item, int position) {
        view.showEdit(item, position);
    }

    @Override
    public void detail(Emo5DSItem item, int position) {
        view.showDetail(item, position);
    }

    @Override
    public void onSuccess(Emo5DSItem item) {
                view.showMessage(R.string.items_deleted);
        view.refresh();
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic);
    }

}

