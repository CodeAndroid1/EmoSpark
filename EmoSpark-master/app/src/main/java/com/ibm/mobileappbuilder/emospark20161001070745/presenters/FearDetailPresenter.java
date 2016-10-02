
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo5DSItem;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.DetailCrudPresenter;
import ibmmobileappbuilder.mvp.view.DetailView;

public class FearDetailPresenter extends BasePresenter implements DetailCrudPresenter<Emo5DSItem>,
      Datasource.Listener<Emo5DSItem> {

    private final CrudDatasource<Emo5DSItem> datasource;
    private final DetailView view;

    public FearDetailPresenter(CrudDatasource<Emo5DSItem> datasource, DetailView view){
        this.datasource = datasource;
        this.view = view;
    }

    @Override
    public void deleteItem(Emo5DSItem item) {
        datasource.deleteItem(item, this);
    }

    @Override
    public void editForm(Emo5DSItem item) {
        view.navigateToEditForm();
    }

    @Override
    public void onSuccess(Emo5DSItem item) {
                view.showMessage(R.string.item_deleted, true);
        view.close(true);
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic, true);
    }
}

