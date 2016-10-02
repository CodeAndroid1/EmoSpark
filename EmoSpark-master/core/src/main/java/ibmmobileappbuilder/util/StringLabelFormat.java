package ibmmobileappbuilder.util;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.List;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Format class used to control the X-Axis labels in charts
 */
public class StringLabelFormat extends Format {

    public List<String> labels;

    public StringLabelFormat(List<String> xValues) {
        labels = xValues;
    }

    @Override
    public StringBuffer format(Object object, StringBuffer buffer, FieldPosition field) {
        int parsedInt = Math.round(Float.parseFloat(object.toString()));
        String labelString = "";
        try {
            labelString = labels.get(parsedInt);
        } catch (IndexOutOfBoundsException e) {
            // do nothing
            analyticsReporter(getApplicationContext()).sendHandledException(
                    "StringLabelFormat",
                    "format",
                    e
            );
        }
        //string length max = 9
        if (labelString.length() > 9) {
            labelString = labelString.substring(0, 6) + "...";
        }

        buffer.append(labelString);
        return buffer;
    }

    @Override
    public Object parseObject(String string, ParsePosition position) {
        return labels.indexOf(string);
    }

}