package ibmmobileappbuilder.ds.filter;

public class StringIdentityFilter implements IdentityFilter<String> {

    private String field;
    private String value;

    public StringIdentityFilter(String field, String value){
        this.field = field;
        this.field = field;
        this.value = value;
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
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
    public boolean applyFilter(String fieldValue) {
        return (value == null) || (fieldValue != null && fieldValue.equalsIgnoreCase(value));
    }
}
