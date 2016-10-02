
package com.ibm.mobileappbuilder.emospark20161001070745.presenters;

import com.ibm.mobileappbuilder.emospark20161001070745.R;
import com.ibm.mobileappbuilder.emospark20161001070745.ds.Emo3DSItem;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.ds.Datasource;
import ibmmobileappbuilder.mvp.presenter.BasePresenter;
import ibmmobileappbuilder.mvp.presenter.DetailCrudPresenter;
import ibmmobileappbuilder.mvp.view.DetailView;

public class DepressedDetailPresenter extends BasePresenter implements DetailCrudPresenter<Emo3DSItem>,
      Datasource.Listener<Emo3DSItem> {

    private final CrudDatasource<Emo3DSItem> datasource;
    private final DetailView view;

    public DepressedDetailPresenter(CrudDatasource<Emo3DSItem> datasource, DetailView view){
        this.datasource = datasource;
        this.view = view;
    }

    @Override
    public void deleteItem(Emo3DSItem item) {
        datasource.deleteItem(item, this);
    }

    @Override
    public void editForm(Emo3DSItem item) {
        view.navigateToEditForm();
    }

    @Override
    public void onSuccess(Emo3DSItem item) {
                view.showMessage(R.string.item_deleted, true);
        view.close(true);
    }

    @Override
    public void onFailure(Exception e) {
        view.showMessage(R.string.error_data_generic, true);
    }
}

