package ibmmobileappbuilder.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import ibmmobileappbuilder.core.R;

/**
 * TextView used in drawer actions section
 */
public class ActionView extends LinearLayout {

    public ActionView(Context context) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View v = inflater.inflate(R.layout.drawer_item, this);
            v.setBackgroundColor(getResources().getColor(R.color.window_background));
        }
    }

    public void setText(String s) {
        ((TextView) this.findViewById(android.R.id.text1)).setText(s);
    }

}