
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo4DSItem;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.DetailCrudPresenter;
import ibmmobileappbuilder.mvp.view.DetailView;

public class AngryDetailPresenter extends BasePresenter implements DetailCrudPresenter<Emo4DSItem>,
      Datasource.Listener<Emo4DSItem> {

    private final CrudDatasource<Emo4DSItem> datasource;
    private final DetailView view;

    public AngryDetailPresenter(CrudDatasource<Emo4DSItem> datasource, DetailView view){
        this.datasource = datasource;
        this.view = view;
    }

    @Override
    public void deleteItem(Emo4DSItem item) {
        datasource.deleteItem(item, this);
    }

    @Override
    public void editForm(Emo4DSItem item) {
        view.navigateToEditForm();
    }

    @Override
    public void onSuccess(Emo4DSItem item) {
                view.showMessage(R.string.item_deleted, true);
        view.close(true);
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic, true);
    }
}

