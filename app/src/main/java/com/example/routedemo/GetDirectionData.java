package com.example.routedemo;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;

public class GetDirectionData extends AsyncTask<Object, String, String> {


    GoogleMap googleMap;
    String directionData;
    String url;

    String distance;
    String duration;

    LatLng latLng;

    LatLng latLngUser;

    @Override
    protected String doInBackground(Object... objects) {

        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        latLng = (LatLng) objects[2];
        latLngUser = (LatLng) objects[3];
        try {
            directionData = FetchURL.readURL( url );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directionData;

    }

    @Override
    protected void onPostExecute(String s) {
        HashMap<String, String> distanceData = null;
        DataParser distanceParser = new DataParser();
        distanceData = distanceParser.parseDistance(s);
        distance = distanceData.get("distance");
        duration = distanceData.get( "duration" );
        googleMap.clear();
        // create new marker with new title and snippet

        MarkerOptions options = new MarkerOptions().position( latLng )
                .draggable( true )
                .title( "Duration" + duration)
                .snippet( "Distance: " + distance )
                .icon( BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE ));



        googleMap.addMarker( options );

        googleMap.addMarker( new MarkerOptions().position( latLngUser )
                .title( "Your Location" ));



        if(MainActivity.directionRequest)
        {
            String[] directionsList;
            DataParser parserDirection = new DataParser();
            directionsList = parserDirection.parseDirections(s);
            displayDirections(directionsList);
        }

    }

    private void displayDirections(String[] directionsList) {
        int count = directionsList.length;
        for(int i = 0;i<count;i++)
        {
            PolylineOptions optionsline = new PolylineOptions()
                    .addAll( PolyUtil.decode( directionsList[i] ) )
                    .color( Color.WHITE)
                    .width(10);
            googleMap.addPolyline(optionsline);

        }
    }
}
