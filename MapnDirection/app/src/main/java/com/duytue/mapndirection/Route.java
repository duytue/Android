package com.duytue.mapndirection;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by duytu on 05-Jun-17.
 */

public class Route {
   public Distance distance;
    public Duration duration;
    public LatLng endLoc;
    public LatLng startLoc;
    public String endLocString;
    public String startLocString;
    public List<LatLng> points;
}
