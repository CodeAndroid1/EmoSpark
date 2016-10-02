package ibmmobileappbuilder.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.dialogs.SelectionDialog;

public class ListSelectionPicker extends LinearLayout {

    TextView mLabelView;

    TextView mSelectionView;

    SelectionDialog mDialog;

    ArrayList<String> mValues;

    ListSelectedListener mListener;

    String mLabel;

    private String mTag;

    public ListSelectionPicker(Context context) {
        this(context, null);
    }

    public ListSelectionPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                                                      R.styleable.ListSelectionPicker, 0, 0
        );
        mLabel = a.getString(R.styleable.ListSelectionPicker_label);

        a.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.listselection_picker, this, true);

        mLabelView = (TextView) getChildAt(0);
        mLabelView.setText(mLabel);

        mSelectionView = (TextView) getChildAt(1);

        setPicker();
    }

    public void setLabel(String label) {
        this.mLabel = label;
        mLabelView.setText(label);
    }

    public ListSelectionPicker setSelectionDialog(SelectionDialog dialog) {
        this.mDialog = dialog;
        if (mDialog != null) {
            // Attach callback
            mDialog.setSelectionListener(
                    new SelectionDialog.SelectionListener() {
                        @Override
                        public void onSelected(ArrayList<String> terms) {
                            setSelectedValues(terms);

                            if (getSelectedListener() != null) {
                                getSelectedListener().onSelected(terms);
                            }
                        }
                    }
            );
        }

        return this;
    }

    public ListSelectionPicker setSelectedListener(ListSelectedListener listener) {
        this.mListener = listener;
        return this;
    }

    public ListSelectedListener getSelectedListener() {
        return this.mListener;
    }

    public ListSelectionPicker setTag(String tag) {
        this.mTag = tag;
        return this;
    }

    public ListSelectionPicker setSelectedValues(ArrayList<String> values) {
        this.mValues = values;
        if (mValues != null) {
            mSelectionView.setText(TextUtils.join(", ", mValues));
        } else {
            mSelectionView.setText(null);
        }
        return this;
    }

    private void setPicker() {

        this.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // initial values
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(SelectionDialog.INITIAL_VALUES,
                                                  mValues
                        );

                        mDialog.setArguments(bundle);
                        mDialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(),
                                     mTag
                        );
                    }

                }
        );
    }

    public interface ListSelectedListener {

        /**
         * callback for listeners. Dates can be null
         */
        void onSelected(ArrayList<String> selected);
    }

}
