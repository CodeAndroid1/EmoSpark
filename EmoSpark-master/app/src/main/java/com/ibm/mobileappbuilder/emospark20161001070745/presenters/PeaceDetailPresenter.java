
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo2DSItem;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.DetailCrudPresenter;
import ibmmobileappbuilder.mvp.view.DetailView;

public class PeaceDetailPresenter extends BasePresenter implements DetailCrudPresenter<Emo2DSItem>,
      Datasource.Listener<Emo2DSItem> {

    private final CrudDatasource<Emo2DSItem> datasource;
    private final DetailView view;

    public PeaceDetailPresenter(CrudDatasource<Emo2DSItem> datasource, DetailView view){
        this.datasource = datasource;
        this.view = view;
    }

    @Override
    public void deleteItem(Emo2DSItem item) {
        datasource.deleteItem(item, this);
    }

    @Override
    public void editForm(Emo2DSItem item) {
        view.navigateToEditForm();
    }

    @Override
    public void onSuccess(Emo2DSItem item) {
                view.showMessage(R.string.item_deleted, true);
        view.close(true);
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic, true);
    }
}

