package com.duytue.mapndirection;

import com.google.android.gms.maps.model.LatLngBounds;

import java.util.List;

/**
 * Created by duytu on 05-Jun-17.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route, LatLngBounds bounds);
}
