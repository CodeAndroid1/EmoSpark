package ibmmobileappbuilder.views.org.sufficientlysecure.htmltextview;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;

import java.io.InputStream;

public class HtmlTextView extends JellyBeanSpanFixTextView {

    public static final String TAG = "HtmlTextView";

    public static final boolean DEBUG = false;

    public HtmlTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public HtmlTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HtmlTextView(Context context) {
        super(context);
    }

    /**
     * http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
     *
     * @param is the input stream
     * @return the string
     */
    static private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    /**
     * Loads HTML from a raw resource, i.e., a HTML file in res/raw/.
     * This allows translatable resource (e.g., res/raw-de/ for german).
     * The containing HTML is parsed to Android's Spannable format and then displayed.
     *
     * @param context the current context
     * @param id      for example: R.raw.help
     */
    public void setHtmlFromRawResource(Context context, int id, boolean useLocalDrawables) {
        // load html from html file from /res/raw
        InputStream inputStreamText = context.getResources().openRawResource(id);

        setHtmlFromString(convertStreamToString(inputStreamText), useLocalDrawables);
    }

    /**
     * Parses String containing HTML to Android's Spannable format and displays it in this
     * TextView.
     *
     * @param html String containing HTML, for example: "<b>Hello world!</b>"
     */
    public void setHtmlFromString(String html, boolean useLocalDrawables) {
        Html.ImageGetter imgGetter;
        if (useLocalDrawables) {
            imgGetter = new LocalImageGetter(getContext());
        } else {
            imgGetter = new UrlImageGetter(this, getContext());
        }
        // this uses Android's Html class for basic parsing, and HtmlTagHandler
        setText(Html.fromHtml(html, imgGetter, new HtmlTagHandler()));

        // make links work
        setMovementMethod(LinkMovementMethod.getInstance());

        // no flickering when clicking textview for Android < 4, but overriders color...
//        text.setTextColor(getResources().getColor(android.R.color.secondary_text_dark_nodisable));
    }
}
