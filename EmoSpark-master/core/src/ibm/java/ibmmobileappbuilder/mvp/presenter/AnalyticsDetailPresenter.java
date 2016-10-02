package ibmmobileappbuilder.mvp.presenter;

import ibmmobileappbuilder.ds.CrudDatasource;
import ibmmobileappbuilder.mvp.view.DetailView;

public class AnalyticsDetailPresenter<T> extends DetailPresenter<T> {

    public AnalyticsDetailPresenter(CrudDatasource<T> datasource, DetailView view) {
        super(datasource, view);
    }


}
