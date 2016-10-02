package ibmmobileappbuilder.mvp.model;

/**
 * Provides a bean with a mechanism to set ids. Methods here have a weird name to avoid name collisions
 */
public interface MutableIdentifiableBean extends IdentifiableBean {

    void setIdentifiableId(String id);

}
