
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo2DSItem;

import java.util.List;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.ListCrudPresenter;
import ibmmobileappbuilder.mvp.view.CrudListView;

public class PeacePresenter extends BasePresenter implements ListCrudPresenter<Emo2DSItem>,
      Datasource.Listener<Emo2DSItem>{

    private final CrudDatasource<Emo2DSItem> crudDatasource;
    private final CrudListView<Emo2DSItem> view;

    public PeacePresenter(CrudDatasource<Emo2DSItem> crudDatasource,
                                         CrudListView<Emo2DSItem> view) {
       this.crudDatasource = crudDatasource;
       this.view = view;
    }

    @Override
    public void deleteItem(Emo2DSItem item) {
        crudDatasource.deleteItem(item, this);
    }

    @Override
    public void deleteItems(List<Emo2DSItem> items) {
        crudDatasource.deleteItems(items, this);
    }

    @Override
    public void addForm() {
        view.showAdd();
    }

    @Override
    public void editForm(Emo2DSItem item, int position) {
        view.showEdit(item, position);
    }

    @Override
    public void detail(Emo2DSItem item, int position) {
        view.showDetail(item, position);
    }

    @Override
    public void onSuccess(Emo2DSItem item) {
                view.showMessage(R.string.items_deleted);
        view.refresh();
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic);
    }

}

