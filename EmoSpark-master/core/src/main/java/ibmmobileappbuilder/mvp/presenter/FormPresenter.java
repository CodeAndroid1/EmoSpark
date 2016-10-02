package ibmmobileappbuilder.mvp.presenter;

import ibmmobileappbuilder.validation.Validator;

public interface FormPresenter<T> extends Presenter {
    /**
     * delete item
     *
     * @param item
     */
    void deleteItem(T item);

    /**
     * save the item
     *
     * @param item
     */
    void save(T item);

    /**
     * save a new item
     *
     * @param item
     */
    void create(T item);

    /**
     * cancel editing
     */
    void cancel();

    void addValidator(int id, Validator<T> v);

    boolean validate(T item);

}
