package ibmmobileappbuilder.ds.restds;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

/**
 * Representation of a GeoJson point:
 * http://geojson.org/geojson-spec.html#point
 * [longitude, latitude]
 */
public class GeoPoint {
    public static final int LONGITUDE_INDEX = 0;
    public static final int LATITUDE_INDEX = 1;

    @SerializedName("type")
    public String type = "Point";
    @SerializedName("coordinates")
    public double[] coordinates = new double[]{0, 0};

    public GeoPoint(){}

    public GeoPoint(double lon, double lat){
        this.coordinates[LONGITUDE_INDEX] = lon;
        this.coordinates[LATITUDE_INDEX] = lat;
    }

    public GeoPoint(double[] coords){
        this.coordinates = coords;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,
                "%.8f, %.8f",
                coordinates[LATITUDE_INDEX],
                coordinates[LONGITUDE_INDEX]);
    }
}