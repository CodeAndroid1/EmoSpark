
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo1DSItem;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.DetailCrudPresenter;
import ibmmobileappbuilder.mvp.view.DetailView;

public class CherishedDetailPresenter extends BasePresenter implements DetailCrudPresenter<Emo1DSItem>,
      Datasource.Listener<Emo1DSItem> {

    private final CrudDatasource<Emo1DSItem> datasource;
    private final DetailView view;

    public CherishedDetailPresenter(CrudDatasource<Emo1DSItem> datasource, DetailView view){
        this.datasource = datasource;
        this.view = view;
    }

    @Override
    public void deleteItem(Emo1DSItem item) {
        datasource.deleteItem(item, this);
    }

    @Override
    public void editForm(Emo1DSItem item) {
        view.navigateToEditForm();
    }

    @Override
    public void onSuccess(Emo1DSItem item) {
                view.showMessage(R.string.item_deleted, true);
        view.close(true);
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic, true);
    }
}

