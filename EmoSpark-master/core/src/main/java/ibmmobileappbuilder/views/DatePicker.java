package ibmmobileappbuilder.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import ibmmobileappbuilder.core.R;

public class DatePicker extends LinearLayout {

    TextView mLabelView;
    TextView mValueView;
    ImageButton mReset;
    private DateSelectedListener mListener;
    private Date mDate;
    private final TextView mErrorView;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.ListSelectionPicker, 0, 0);
        String label = a.getString(R.styleable.ListSelectionPicker_label);

        a.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.date_picker, this, true);

        mLabelView = (TextView) getChildAt(0);
        mLabelView.setText(label);

        mValueView = (TextView) findViewById(R.id.dateValue);
        mReset = (ImageButton) findViewById(R.id.dateReset);
        mReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate(null);
                if(getListener() != null)
                    getListener().onSelected(null);
            }
        });

        mErrorView = (TextView) findViewById(R.id.errorView);

        mValueView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                if(mDate != null)
                    cal.setTime(mDate);

                // Try to restore previous suspended fragment
                FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
                CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) fm.findFragmentByTag("DatePicker");

                if(calendarDatePickerDialog == null) {
                    calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(dateCallback,
                            cal.get(Calendar.YEAR),
                            cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH)
                    );
                }
                else
                    calendarDatePickerDialog.setOnDateSetListener(dateCallback);

                calendarDatePickerDialog.show(fm, "DatePicker");
            }
        });
    }

    private void setDateText(Date date) {
        mValueView.setText(date != null ? DateFormat.getDateInstance().format(date) : null);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());

        if (mDate != null) {
            bundle.putLong("theDate", mDate.getTime());
        }

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle b = (Bundle) state;
            long dateVal = b.getLong("theDate", -1);

            if (dateVal != -1) {
                setDate(new Date(dateVal));
            }

            state = ((Bundle) state).getParcelable("instanceState");
        }

        super.onRestoreInstanceState(state);
    }

    public void setDate(Date date){
        this.mDate = date;
        setDateText(date);
    }

    public Date getDate(){
        return mDate;
    }

    public void setListener(DateSelectedListener listener){
        this.mListener = listener;
    }

    public DateSelectedListener getListener(){
        return mListener;
    }

    public void setError(int errorRes){
        setError(getContext().getString(errorRes));
    }

    public void setError(CharSequence errorMsg){
        if(errorMsg != null) {
            if (mErrorView.getVisibility() == GONE) {
                mErrorView.setText(errorMsg);
                mErrorView.setAlpha(0.0F);
                mErrorView.setVisibility(VISIBLE);
                mErrorView.animate()
                        .alpha(1.0F)
                        .setDuration(200L)
                        .setInterpolator(new FastOutSlowInInterpolator())
                        .setListener(null).start();
            }
        }
        else {
            mErrorView.animate()
                    .alpha(0.0F)
                    .setDuration(200L)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mErrorView.setText((CharSequence) null);
                            mErrorView.setVisibility(GONE);
                        }
                    });
        }
    }

    CalendarDatePickerDialog.OnDateSetListener dateCallback = new CalendarDatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(
                CalendarDatePickerDialog calendarDatePickerDialog,
                int year,
                int month,
                int day) {

            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, 0, 0, 0);

            mDate = cal.getTime();
            setDateText(mDate);

            if (getListener() != null) {
                getListener().onSelected(cal.getTime());
            }
        }
    };

    public interface DateSelectedListener {
        /**
         * callback for listeners. Dates can be null
         */
        public void onSelected(Date selected);
    }
}
