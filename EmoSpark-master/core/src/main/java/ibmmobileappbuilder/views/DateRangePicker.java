package ibmmobileappbuilder.views;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import ibmmobileappbuilder.core.R;

public class DateRangePicker extends LinearLayout {

    TextView mLabelView;

    TextView mMinDateView;
    Date mMinDate;

    TextView mMaxDateView;
    Date mMaxDate;

    DateRangeSelectedListener mListener;
    private TextView mErrorView;

    public DateRangePicker(Context context) {
        this(context, null);
    }

    public DateRangePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.DateRangePicker, 0, 0
        );
        String label = a.getString(R.styleable.DateRangePicker_label);

        a.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_VERTICAL);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.daterange_picker, this, true);

        mLabelView = (TextView) getChildAt(0);

        mLabelView.setText(label);

        mErrorView = (TextView) findViewById(R.id.errorView);

        mMinDateView = (TextView) findViewById(R.id.min_value);
        mMaxDateView = (TextView) findViewById(R.id.max_value);

        setPicker(mMinDateView, true);
        setPicker(mMaxDateView, false);
    }

    public void setLabel(String label) {
        mLabelView.setText(label);
    }

    public DateRangePicker setMinDate(Date date) {
        this.mMinDate = date;
        setDateText(mMinDateView, date, true);

        return this;
    }

    public DateRangePicker setMaxDate(Date date) {
        this.mMaxDate = date;
        setDateText(mMaxDateView, date, false);

        return this;
    }

    public DateRangePicker setRangeSelectedListener(DateRangeSelectedListener listener) {
        this.mListener = listener;

        return this;
    }

    public DateRangeSelectedListener getListener() {
        return mListener;
    }

    private void setDateText(TextView view, Date date, boolean min) {
        view.setText(date != null ?
                        String.format(getResources().getString(
                                        min ? R.string.datemin_filter_format : R.string.datemax_filter_format
                                ),
                                DateFormat.getMediumDateFormat(getContext()).format(date)
                        )
                        : null
        );
    }

    private void setPicker(final TextView view, final boolean isMin) {

        view.setOnClickListener(new OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        int year, month, day;
                                        Calendar cal = Calendar.getInstance();

                                        if (isMin && mMinDate != null) {
                                            cal.setTime(mMinDate);
                                        } else if (!isMin && mMaxDate != null) {
                                            cal.setTime(mMaxDate);
                                        }

                                        FragmentManager fm = ((FragmentActivity) getContext()).getSupportFragmentManager();
                                        CalendarDatePickerDialog calendarDatePickerDialog = (CalendarDatePickerDialog) fm.findFragmentByTag(
                                                "DatePicker"
                                        );

                                        CalendarDatePickerDialog.OnDateSetListener callback = new CalendarDatePickerDialog.OnDateSetListener() {
                                            @Override
                                            public void onDateSet(
                                                    CalendarDatePickerDialog calendarDatePickerDialog,
                                                    int year,
                                                    int month,
                                                    int day) {

                                                Calendar cal = Calendar.getInstance();
                                                cal.set(year, month, day, 0, 0, 0);

                                                // set dates
                                                if (isMin) {
                                                    mMinDate = cal.getTime();
                                                } else {
                                                    mMaxDate = cal.getTime();
                                                }

                                                setDateText(view, cal.getTime(), isMin);

                                                if (getListener() != null) {
                                                    getListener().onRangeSelected(mMinDate,
                                                            mMaxDate
                                                    );
                                                }
                                            }
                                        };
                                        if (calendarDatePickerDialog == null) {
                                            calendarDatePickerDialog = CalendarDatePickerDialog.newInstance(
                                                    callback,
                                                    cal.get(Calendar.YEAR),
                                                    cal.get(Calendar.MONTH),
                                                    cal.get(Calendar.DAY_OF_MONTH)
                                            );
                                        } else {
                                            calendarDatePickerDialog.setOnDateSetListener(callback);
                                        }

                                        calendarDatePickerDialog.show(fm, "DatePicker");
                                    }
                                }
        );
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());

        if (mMinDate != null) {
            bundle.putLong("rangeMin", mMinDate.getTime());
        }

        if (mMaxDate != null) {
            bundle.putLong("rangeMax", mMaxDate.getTime());
        }

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle b = (Bundle) state;
            long minVal = b.getLong("rangeMin", -1);
            long maxVal = b.getLong("rangeMax", -1);

            if (minVal != -1) {
                this.mMinDate = new Date(minVal);
            }

            if (maxVal != -1) {
                this.mMaxDate = new Date(maxVal);
            }

            state = ((Bundle) state).getParcelable("instanceState");
        }

        super.onRestoreInstanceState(state);
    }

    public void setError(int errorRes) {
        setError(getContext().getString(errorRes));
    }

    public void setError(CharSequence errorMsg) {
        if (errorMsg != null) {
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
        } else {
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
                                 }
                    );
        }
    }

    public interface DateRangeSelectedListener {

        /**
         * callback for listeners. Dates can be null
         *
         * @param dateMin the minimun date selected. Can be null
         * @param dateMax the maximum date selected. Can be null
         */
        void onRangeSelected(Date dateMin, Date dateMax);
    }

}
