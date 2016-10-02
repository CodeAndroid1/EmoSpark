package ibmmobileappbuilder.ds.filter;

import java.util.List;

public interface InFilter<T> extends Filter<T> {
    List<T> getValues();
}
