package ibmmobileappbuilder.ds.filter;

/**
 * Created by jiparis on 15/09/2015.
 */
public class NumberFilter implements IdentityFilter<Number>{

    private final Number value;
    private final String field;

    public NumberFilter(String field, Number value){
        this.field = field;
        this.value = value;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getQueryString() {
        return "\"" + field + "\":{\"$eq\":" + value + "}";
    }

    @Override
    public boolean applyFilter(Number fieldValue) {
        return (value == null) || value.equals(fieldValue);
    }

    @Override
    public Number getValue() {
        return value;
    }
}
