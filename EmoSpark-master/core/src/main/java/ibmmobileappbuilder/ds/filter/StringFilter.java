package ibmmobileappbuilder.ds.filter;

public class StringFilter implements ContainsFilter {

    private final String value;
    private final String field;

    public StringFilter(String field, String value){
        this.field = field;
        this.value = value;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getQueryString() {
        return "\"" + field + "\":{\"$regex\":\"" + value + "\",\"$options\":\"i\"}";
    }

    @Override
    public boolean applyFilter(String fieldValue) {
        return (value == null) ||
                (fieldValue != null && fieldValue.toLowerCase().contains(value.toLowerCase()));
    }

    @Override
    public String getValue() {
        return value;
    }
}
