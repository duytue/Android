package com.duytue.mapndirection;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by duytu on 05-Jun-17.
 */

public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyDPBq6QBKDgG-fN8nkzLWOMog5pXILZz48";
    private DirectionFinderListener listener;
    private String origin;
    private String destination;

    public DirectionFinder(DirectionFinderListener listener, String origin, String destination) {
        this.listener = listener;
        this.origin = origin;
        this.destination = destination;
    }

    private class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {
            String result= "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(params[0]);
                urlConnection = (HttpURLConnection)url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while(data != -1)//eof
                {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "FAILED";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                genJSONDATA(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void genJSONDATA(String result) throws JSONException {
            if (result == null)
                return;
            List<Route> routes = new ArrayList<Route>();
            JSONObject fullJSONData = new JSONObject(result);
            JSONArray jsonRoutes = fullJSONData.getJSONArray("routes");
            for (int i =0 ; i< jsonRoutes.length(); ++i) {
                JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
                Route route = new Route();

                JSONObject overview_polyLineJSON = jsonRoute.getJSONObject("overview_polyline");
                JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
                JSONObject jsonLeg = jsonLegs.getJSONObject(0);
                JSONObject jsonDistance = jsonLeg.getJSONObject("distance");
                JSONObject jsonDuration = jsonLeg.getJSONObject("duration");
                JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
                JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

                route.distance = new Distance(jsonDistance.getString("text"), jsonDistance.getInt("value"));
                route.duration = new Duration(jsonDuration.getString("text"), jsonDuration.getInt("value"));
                route.endLoc = new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng"));
                route.endLocString = jsonLeg.getString("end_address");
                route.startLoc = new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng"));
                route.startLocString = jsonLeg.getString("start_address");

                route.points = decodePolyLine(overview_polyLineJSON.getString("points"));

                routes.add(route);
            }


        //retrieve bounds for map
        JSONObject jsonBounds = jsonRoutes.getJSONObject(0).getJSONObject("bounds");
        JSONObject northeast, southwest;
        northeast = jsonBounds.getJSONObject("northeast");
        southwest = jsonBounds.getJSONObject("southwest");
        LatLng NE, SW;
        NE = new LatLng(northeast.getDouble("lat"), northeast.getDouble("lng"));
        SW = new LatLng(southwest.getDouble("lat"), southwest.getDouble("lng"));
        //LatLngBounds bounds = new LatLngBounds(NE, SW);
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        bounds.include(NE);
        bounds.include(SW);
        listener.onDirectionFinderSuccess(routes, bounds.build());
    }

    private List<LatLng> decodePolyLine(String points) {
        int len = points.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;


        Log.i("decodePolyLine", "Ran");
        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = points.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = points.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }

        return decoded;
    }


    public void execute() {
        try {
            listener.onDirectionFinderStart();
            new DownloadTask().execute(createUrl());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private String createUrl() throws UnsupportedEncodingException{
        String urlOrigin = URLEncoder.encode(origin, "utf-8");
        String urlDestination = URLEncoder.encode(destination, "utf-8");

        String url = DIRECTION_URL_API + "origin= " + urlOrigin + "&destination=" + urlDestination + "&key=" + GOOGLE_API_KEY;
        Log.i("URL", url);
        return url;
    }
}
