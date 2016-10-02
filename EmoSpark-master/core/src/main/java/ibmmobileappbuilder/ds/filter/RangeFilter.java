package ibmmobileappbuilder.ds.filter;

public interface RangeFilter<T> extends Filter<T> {

    T getMin();
    T getMax();

}
