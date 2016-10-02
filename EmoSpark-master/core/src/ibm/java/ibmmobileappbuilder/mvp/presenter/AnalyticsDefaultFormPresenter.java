package ibmmobileappbuilder.mvp.presenter;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.mvp.view.FormView;

public class AnalyticsDefaultFormPresenter<T> extends DefaultFormPresenter<T> {

    public AnalyticsDefaultFormPresenter(CrudDatasource<T> datasource, FormView<T> view) {
        super(datasource, view);
    }
}
