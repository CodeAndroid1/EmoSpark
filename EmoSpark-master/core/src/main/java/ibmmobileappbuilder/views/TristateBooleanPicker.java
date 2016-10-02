package ibmmobileappbuilder.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import ibmmobileappbuilder.core.R;

public class TristateBooleanPicker extends LinearLayout {

    private final Spinner mSpinner;
    Boolean mValue = null;
    private ChoiceListener mClickListener;

    public TristateBooleanPicker(Context context) {
        this(context, null);
    }

    public TristateBooleanPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                                                      R.styleable.DateTimePicker, 0, 0
        );
        String label = a.getString(R.styleable.TristateBooleanPicker_label);

        a.recycle();

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.tristate_picker, this, true);

        TextView labelView = (TextView) findViewById(R.id.label);
        labelView.setText(label);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(getContext(),
                                                                 R.layout.tristate_picker_item
        );

        mAdapter.addAll(
                getResources().getString(android.R.string.yes),
                getResources().getString(android.R.string.no),
                getResources().getString(R.string.not_set)
        );

        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                               @Override
                                               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                   mValue = (position == 2) ? null : (position != 1);
                                                   if (getListener() != null) {
                                                       getListener().onChoice(mValue);
                                                   }
                                               }

                                               @Override
                                               public void onNothingSelected(AdapterView<?> parent) {
                                               }
                                           }
        );

        setValue(null);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());

        if (mValue != null) {
            bundle.putInt("boolVal", mValue ? 0 : 1);
        }

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle b = (Bundle) state;
            int storedVal = b.getInt("boolVal", -1);

            if (storedVal != -1) {
                setValue(storedVal == 0);
            } else {
                setValue(null);
            }

            state = ((Bundle) state).getParcelable("instanceState");
        }

        super.onRestoreInstanceState(state);
    }

    /**
     * Sets the value for this control. Can be null
     *
     * @param choice the new value, or null to clear it.
     */
    public void setValue(Boolean choice) {
        mValue = choice;
        if (choice == null) {
            mSpinner.setSelection(2);
        } else {
            mSpinner.setSelection(choice ? 0 : 1);
        }
    }

    public Boolean getValue() {
        return mValue;
    }

    public void setListener(ChoiceListener listener) {
        mClickListener = listener;
    }

    public ChoiceListener getListener() {
        return mClickListener;
    }

    public interface ChoiceListener {
        void onChoice(Boolean value);
    }
}
