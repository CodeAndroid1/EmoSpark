package ibmmobileappbuilder.views;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.TypedArray;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import ibmmobileappbuilder.core.R;
import ibmmobileappbuilder.ds.restds.GeoPoint;
import ibmmobileappbuilder.injectors.ApplicationInjector;

import static ibmmobileappbuilder.analytics.injector.AnalyticsReporterInjector.analyticsReporter;
import static ibmmobileappbuilder.injectors.ApplicationInjector.getApplicationContext;

/**
 * Picker for geolocation points
 */
public class GeoPicker extends LinearLayout {

    private static final String TAG = GeoPicker.class.getSimpleName();
    private final EditText mLatitudeView;
    private final EditText mLongitudeView;
    private final TextView mErrorView;

    private GeoPoint mGeoPoint;
    private PointChangedListener mListener;

    public GeoPicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.GeoPicker, 0, 0);
        String label = a.getString(R.styleable.GeoPicker_label);

        a.recycle();

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.geopoint_picker, this, true);

        TextView labelView = (TextView) getChildAt(0);
        labelView.setText(label);

        mLatitudeView = (EditText) findViewById(R.id.latitude);

        mLongitudeView = (EditText) findViewById(R.id.longitude);

        ImageButton myLocationBtn = (ImageButton) findViewById(R.id.my_location_button);
        myLocationBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              Location loc = getLastKnownLocation();
              if (loc != null)
                  setPoint(new GeoPoint(new double[]{
                                  loc.getLongitude(),
                                  loc.getLatitude()}
                          )
                  );
            }
         }
        );

        mErrorView = (TextView) findViewById(R.id.error);
        setWatchers();
    }

    public GeoPicker setPoint(GeoPoint point){
        this.mGeoPoint = point;
        updateViews();

        notifyListener();

        return this;
    }

    protected void notifyListener(){
        if(getListener() != null)
            getListener().onPointChanged(mGeoPoint);
    }

    public PointChangedListener getListener(){
        return mListener;
    }

    public GeoPoint getPoint(){
        return mGeoPoint;
    }

    public GeoPicker setListener(PointChangedListener listener){
        mListener = listener;
        return this;
    }

    public void setError(int error) {
        setError(getContext().getString(error));
    }

    public void setError(String errorMsg) {

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
                    });
        }
    }

    private void setWatchers(){
        mLatitudeView.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                Double value = null;
                try {
                    value = Double.parseDouble(s.toString());
                } catch (NumberFormatException e) {
                    analyticsReporter(getApplicationContext()).sendHandledException(
                            TAG,
                            "parseDouble",
                            e
                    );
                }

                setLatitude(value);
            }
        });

        mLongitudeView.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                Double value = null;
                try {
                    value = Double.parseDouble(s.toString());
                } catch (NumberFormatException e) {
                    analyticsReporter(getApplicationContext()).sendHandledException(TAG, "parseDouble", e);
                }

                setLongitude(value);
            }
        });
    }

    private void setLatitude(Double latitude){
        if (latitude != null) {
            createGeoPoint();
            mGeoPoint.coordinates[GeoPoint.LATITUDE_INDEX] = latitude;
            notifyListener();
        }
    }

    private void setLongitude(Double longitude){
        if (longitude != null){
            createGeoPoint();
            mGeoPoint.coordinates[GeoPoint.LONGITUDE_INDEX] = longitude;
            notifyListener();
        }
    }

    private void updateViews(){
        mLatitudeView.setText(mGeoPoint != null ?
                String.format("%.3f", mGeoPoint.coordinates[GeoPoint.LATITUDE_INDEX]) :
                null);
        mLongitudeView.setText(mGeoPoint != null ?
                String.format("%.3f", mGeoPoint.coordinates[GeoPoint.LONGITUDE_INDEX]) :
                null);
    }

    private void createGeoPoint(){
        if(mGeoPoint == null)
            mGeoPoint = new GeoPoint();
    }

    private @Nullable Location getLastKnownLocation(){

        // todo move this to background??
        LocationManager lm = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        Criteria cr = new Criteria();
        cr.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = lm.getBestProvider(cr, true);
        if(provider == null)
            return null;

        return lm.getLastKnownLocation(provider);
    }

    public interface PointChangedListener{
        void onPointChanged(GeoPoint point);
    }
}
