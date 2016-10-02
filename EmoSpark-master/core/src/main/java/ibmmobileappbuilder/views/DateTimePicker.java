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
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import java.util.Calendar;
import java.util.Date;

import ibmmobileappbuilder.core.R;

public class DateTimePicker extends LinearLayout {

    TextView mLabelView;

    TextView mDateView;
    Calendar mDateTime;

    TextView mTimeView;

    ImageButton mReset;

    DateSelectedListener mListener;
    private TextView mErrorView;

    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DateTimePicker, 0, 0);
        String label = a.getString(R.styleable.DateTimePicker_label);

        a.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.datetime_picker, this, true);

        mLabelView = (TextView) getChildAt(0);

        mLabelView.setText(label);

        mDateView = (TextView) findViewById(R.id.dateValue);
        mTimeView = (TextView) findViewById(R.id.timeValue);
        mReset = (ImageButton) findViewById(R.id.dateReset);
        mErrorView = (TextView) findViewById(R.id.errorView);

        setDatePicker(mDateView, true);
        setTimePicker(mTimeView, false);
        setResetHandler();
    }

    private void setResetHandler() {
        mReset.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setDateTime(null);
                if(getListener() != null)
                    getListener().onDateSelected(null);
            }
        });
    }

    public void setLabel(String label) {
        mLabelView.setText(label);
    }

    /**
     * Set the date part
     * @param date a Date object
     * @return DateTimePicker instance for chaining
     */
    public DateTimePicker setDate(Date date) {
        if(date != null){
            if (this.mDateTime == null) {
                this.mDateTime = Calendar.getInstance();
                this.mDateTime.setTime(date);
            }
            else{
                // set the date part
                Calendar from = Calendar.getInstance();
                from.setTime(date);

                mDateTime.set(from.get(Calendar.YEAR), from.get(Calendar.MONTH), from.get(Calendar.DAY_OF_MONTH));
            }

            setDateText(mDateView, mDateTime);
        }

        return this;
    }

    public DateTimePicker setTime(Date date) {

        if(date != null){
            if (this.mDateTime == null) {
                this.mDateTime = Calendar.getInstance();
                this.mDateTime.setTime(date);
            }
            else{
                // set the time part
                Calendar from = Calendar.getInstance();
                from.setTime(date);

                mDateTime.set(Calendar.HOUR_OF_DAY, from.get(Calendar.HOUR_OF_DAY));
                mDateTime.set(Calendar.MINUTE, from.get(Calendar.MINUTE));
                mDateTime.set(Calendar.SECOND, from.get(Calendar.SECOND));
                mDateTime.set(Calendar.MILLISECOND, from.get(Calendar.MILLISECOND));
            }

            setTimeText(mTimeView, mDateTime);
        }

        return this;
    }



    public DateTimePicker setDateTime(Date date){

        if(date != null){
            if(this.mDateTime == null)
                this.mDateTime = Calendar.getInstance();

            this.mDateTime.setTime(date);
        }
        else
            this.mDateTime = null;

        setDateText(mDateView, mDateTime);
        setTimeText(mTimeView, mDateTime);
        return this;
    }

    public DateTimePicker setDateTimeSelectedListener(DateSelectedListener listener) {
        this.mListener = listener;
        return this;
    }

    public DateSelectedListener getListener(){
        return mListener;
    }

    private void setDateText(TextView view, Calendar cal){
        view.setText(cal != null ?
                DateFormat.getMediumDateFormat(getContext()).format(cal.getTime())
                : null);
    }

    private void setTimeText(TextView mTimeView, Calendar mDateTime) {
        mTimeView.setText(mDateTime != null ?
            DateFormat.getTimeFormat(getContext()).format(mDateTime.getTime())
            : null);
    }

    private void setDatePicker(final TextView view, final boolean isMin) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = mDateTime;
                if(cal == null)
                    cal = Calendar.getInstance();

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

    private void setTimePicker(final TextView view, final boolean isMin) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = mDateTime;
                if(cal == null)
                    cal = Calendar.getInstance();

                // Try to restore previous suspended fragment
                FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
                RadialTimePickerDialog radialTimePickerDialog = (RadialTimePickerDialog) fm.findFragmentByTag("TimePicker");

                if(radialTimePickerDialog == null) {
                    radialTimePickerDialog = RadialTimePickerDialog.newInstance(timeCallback,
                            cal.get(Calendar.HOUR_OF_DAY),
                            cal.get(Calendar.MINUTE),
                            false
                    );
                }
                else
                    radialTimePickerDialog.setOnTimeSetListener(timeCallback);

                radialTimePickerDialog.show(fm, "TimePicker");
            }
        });
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());

        if (mDateTime != null) {
            bundle.putLong("datetime", mDateTime.getTime().getTime());
        }

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle b = (Bundle) state;
            long datetimeVal = b.getLong("datetime", -1);

            if (datetimeVal != -1) {
                setDateTime(new Date(datetimeVal));
            }

            state = ((Bundle) state).getParcelable("instanceState");
        }

        super.onRestoreInstanceState(state);
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

            setDate(cal.getTime());
            notifyListener();
        }
    };

    RadialTimePickerDialog.OnTimeSetListener timeCallback = new RadialTimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(RadialTimePickerDialog radialTimePickerDialog, int h, int m) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, h);
            cal.set(Calendar.MINUTE, m);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);

            setTime(cal.getTime());
            notifyListener();
        }
    };

    private void notifyListener(){
        if (getListener() != null) {
            getListener().onDateSelected(mDateTime.getTime());
        }
    }


    public interface DateSelectedListener {

        /**
         * callback for listeners. Dates can be null
         */
        public void onDateSelected(Date date);
    }

}
