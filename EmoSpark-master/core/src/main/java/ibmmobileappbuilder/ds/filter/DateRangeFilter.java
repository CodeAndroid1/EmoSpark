package ibmmobileappbuilder.ds.filter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * A date range filter
 */
public class DateRangeFilter implements RangeFilter<Date> {

    private String field;
    private Date min;
    private Date max;

    public DateRangeFilter(String field, Date min, Date max) {
        this.field = field;
        this.max = max;
        this.min = min;
    }

    @Override
    public String getField() {
        return field;
    }

    @Override
    public String getQueryString() {
        StringBuilder sb = new StringBuilder()
                .append("\"").append(field).append("\":");

        sb.append("{");
        if (min != null) {
            sb.append("\"$gte\":").append(dateToISO(min));
        }

        if (max != null) {
            if (min != null) {
                sb.append(",");
            }
            sb.append("\"$lte\":").append(dateToISO(max));
        }
        sb.append("}");

        return sb.toString();
    }

    @Override
    public boolean applyFilter(Date fieldValue) {
        if (fieldValue == null) {
            return false;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(fieldValue);

        if (min != null) {
            Calendar minCal = Calendar.getInstance();
            minCal.setTime(min);
            if (!cal.after(minCal)) {
                return false;
            }
        }
        if (max != null) {
            Calendar maxCal = Calendar.getInstance();
            maxCal.setTime(max);
            if (!cal.before(maxCal)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public Date getMin() {
        return min;
    }

    @Override
    public Date getMax() {
        return max;
    }

    private String dateToISO(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return new StringBuilder("\"")
                .append(format.format(date))
                        .append("\"")
                        .toString();
    }
}
