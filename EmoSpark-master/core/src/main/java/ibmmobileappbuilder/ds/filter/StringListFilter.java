package ibmmobileappbuilder.ds.filter;

import android.text.TextUtils;

import java.util.List;

/**
 * A filter that matches a field against a value list
 */
public class StringListFilter implements InFilter<String> {

    private String field;
    private List<String> values;

    public StringListFilter(String field, List<String> values) {
        this.field = field;
        this.values = values;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getQueryString() {
        return "\"" + field + "\":{\"$in\":[\"" + TextUtils.join("\", \"", values) + "\"]}";
    }

    @Override
    public boolean applyFilter(String fieldValue) {
        return values == null || values.isEmpty() || values.contains(fieldValue);
    }

    @Override
    public List<String> getValues() {
        return values;
    }
}
