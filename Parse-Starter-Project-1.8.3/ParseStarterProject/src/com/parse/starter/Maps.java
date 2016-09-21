package com.parse.starter;

import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class Maps extends FragmentActivity implements GoogleMap.OnMapLongClickListener
{
    private GoogleMap map;
    private boolean flag2;
    String search_store;
    int c;
    private double lat, lon;
    EditText search;
    ImageButton search_button;
    Criteria criteria = new Criteria();
    // Might be null if Google Play services APK is not available.

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        search = (EditText) findViewById(R.id.search);
        search_button = (ImageButton) findViewById(R.id.search_button);

        if( getIntent().getDoubleExtra( "latitude", 1234567890) == 1234567890 || getIntent().getDoubleExtra( "longitude", 1234567890) == 1234567890)
            setUpMapIfNeeded();
        else
        {
            // eventdisplay = getIntent().getStringExtra("address");
            setUpMapIfNeeded();
            //markLocationsEventDisplay(eventdisplay);
            markLocationsEventDisplay();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #map} is not null.
     * <p/>
     * If it isn't installed {@link com.google.android.gms.maps.SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(android.os.Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {

            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * This should only be called once and when we are sure that {@link #map} is not null.
     */
    private void setUpMap()
    {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        criteria.setAccuracy(Criteria.ACCURACY_COARSE);

        String provider = locationManager.getBestProvider(criteria, true);

        LocationListener locationListener = new LocationListener()
        {

            @Override
            public void onLocationChanged(Location location) {
                showCurrentLocation(location);
            }
            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {
            }
            @Override
            public void onProviderEnabled(String s) {
            }
            @Override
            public void onProviderDisabled(String s) {
            }
        };
        // locationManager.requestLocationUpdates( provider, 2000, 0, locationListener);
        // Getting initial Location
        Location location = locationManager.getLastKnownLocation(provider);
        // Show the initial location
        if(location != null)
        {
            showCurrentLocation(location);
        }

        if( getIntent().getBooleanExtra( "listener", true))
            map.setOnMapLongClickListener( this);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_store = search.getText().toString();
                if( !search_store.equals( ""))
                    markLocations(search_store);
                else
                    Toast.makeText( Maps.this, "Type address first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showCurrentLocation(Location location)
    {
        //criteria.setAccuracy(Criteria.ACCURACY_FINE);
        map.setMyLocationEnabled( true);
        map.clear();
        LatLng currentPosition = new LatLng(location.getLatitude(),location.getLongitude());
        // Zoom in, animating the camera.
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 5f));
    }

    @Override
    public void onMapLongClick (LatLng point)
    {
        flag2 = true;
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        List<Address> addresses = null;
        try {
            lat = (point.latitude);
            lon = (point.longitude);
            addresses = geocoder.getFromLocation(lat,lon,1);
            map.addMarker(new MarkerOptions().position(point));
            Address returnedAddress = addresses.get(0);
            StringBuilder place = new StringBuilder("");
            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex() - 1; i++) {
                place.append(returnedAddress.getAddressLine(i)).append(",");
                c = i;
            }
            c++;
            place.append(returnedAddress.getAddressLine(c));
            Intent back = new Intent();
            back.putExtra("ADDRESS", (CharSequence) place);
            back.putExtra("isMapClicked", flag2);
            back.putExtra( "lat" , lat);
            back.putExtra( "lon" , lon);
            setResult(RESULT_OK, back);
            finish();

        } catch (IOException e) {
            Toast.makeText( this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void markLocations(String strAddress)
    {
        Geocoder coder = new Geocoder(this);
        List<Address> address = null;
        LatLng p1;

        try {
            address = coder.getFromLocationName(strAddress,1);
            Address location = address.get(0);
            double l1 = location.getLatitude();
            double l2 = location.getLongitude();

            p1 = new LatLng (l1,l2);
            onMapLongClick(p1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void markLocationsEventDisplay()
    {
        LatLng p1 = new LatLng( getIntent().getDoubleExtra("latitude", 0), getIntent().getDoubleExtra("longitude", 0));
        map.addMarker(new MarkerOptions().position(p1));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(p1, 18));
    }

    /*public void markLocationsEventDisplay(String address_input)
    {

        Geocoder coder = new Geocoder(this);
        List<Address> address = null;
        LatLng p1;
        int count = 0;
        try {
            while( count < 5) {
                address = coder.getFromLocationName(address_input, 1);
                count++;
            }
                Address location = address.get(0);

                double l1 = location.getLatitude();
                double l2 = location.getLongitude();

                p1 = new LatLng(l1, l2);
                map.addMarker(new MarkerOptions().position(p1));
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(p1, 18));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

}
