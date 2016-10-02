package ibmmobileappbuilder.ds.filter;

/**
 * Identity Filter
 * @param <T> the type to be filtered
 */
public interface IdentityFilter<T> extends Filter<T> {
    T getValue();
}
