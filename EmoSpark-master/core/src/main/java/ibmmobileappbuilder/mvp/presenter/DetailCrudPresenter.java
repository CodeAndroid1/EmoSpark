package ibmmobileappbuilder.mvp.presenter;

import ibmmobileappbuilder.mvp.presenter.Presenter;

public interface DetailCrudPresenter<T> extends Presenter {
    /**
     * delete item
     *
     * @param item
     */
    void deleteItem(T item);

    /**
     * Go to edit form
     *
     * @param item
     */
    void editForm(T item);

}
