package ibmmobileappbuilder.ds.filter;

import java.util.regex.Pattern;

/**
 * A filter that matches a field against a regular expression
 */
public class RegularExpFilter implements Filter<String> {

    private String field;
    private String value;

    public RegularExpFilter(String field, String value) {
        this.field = field;
        this.value = value;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getQueryString() {
        StringBuilder sb = new StringBuilder();
        //searches.add("{\"" + col + "\":{\"$regex\":\"" + st + "\",\"$options\":\"i\"}}");
        sb.append("\"")
                .append(field)
                .append("\":{\"$regex\":\"")
                .append(value)
                .append("\",\"$options\":\"i\"}");

        return sb.toString();
    }

    @Override
    public boolean applyFilter(String fieldValue) {
        Pattern p = Pattern.compile(value);
        return p.matcher(fieldValue).matches();
    }
}
