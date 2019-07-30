package com.example.userlocationalert;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.managers.SharedPreferenceManager;
import com.example.service.LocationTrackingService;
import com.example.utils.GeoAPIClientLayer;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class SetLocationActivity extends BaseActivity implements OnMapReadyCallback, View.OnClickListener, GoogleMap.OnMapClickListener, PlaceSelectionListener, com.google.android.gms.location.LocationListener {


    private LatLng latLng;
    GoogleMap googleMap;
    PlaceAutocompleteFragment autocompleteFragment;

    EditText editTextradius;
    private GeoAPIClientLayer mGoogleApiClientLayer;

    @Override
    protected void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
//        checkIsLocationEnabled();
        mGoogleApiClientLayer = new GeoAPIClientLayer(this);
        setPreviousLatLng();
        editTextradius = getView(R.id.editTextRadius);
        attachClickListener(R.id.bt_done);
        setUpFragments();
    }

    private void setPreviousLatLng() {
        latLng = new LatLng(SharedPreferenceManager.getInstance(this).getLatitude(), SharedPreferenceManager.getInstance(this).getLongitude());
    }

    private void setUpFragments() {
        autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.editTextSearch);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapview);
        mapFragment.getMapAsync(this);
        autocompleteFragment.setOnPlaceSelectedListener(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapClickListener(this);
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json));
        if (latLng != null) {
            addMarker(latLng, "i'm here", true);
        }
    }

    public void addMarker(LatLng latLng, String title, Boolean move) {
        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng, 13);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(11);

        MarkerOptions mp = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.placeholder));
        mp.title(title);
        mp.position(latLng);
        this.googleMap.addMarker(mp);
        if (move) {
            this.googleMap.moveCamera(yourLocation);
            this.googleMap.animateCamera(zoom);
        }
    }


//    public void checkIsLocationEnabled(){
//        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
//        boolean gps_enabled = false;
//        boolean network_enabled = false;
//
//        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        if(!gps_enabled && !network_enabled)
//            DialogUtil.showAlertMessageForRadius(this, "Please Enabled your Location", "", "Open Settings", "Later", new MaterialDialog.SingleButtonCallback() {
//                @Override
//                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                    onBackPressed();
//                }
//            });
//        else{
//            mGoogleApiClientLayer = new GeoAPIClientLayer(this);
//        }
//
//
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_done:
                if (TextUtils.isEmpty(editTextradius.getText().toString().trim()))
                    editTextradius.setError("Please enter Radius");
                else {
                    SharedPreferenceManager.getInstance(this).setRadius(Integer.valueOf(editTextradius.getText().toString()));
                    startTrackerService();
                    finish();
                }
                break;
        }
    }

    private void startTrackerService() {
        startService(new Intent(this, LocationTrackingService.class));

//Notify the user that tracking has been enabled//

        Toast.makeText(this, "GPS tracking enabled", Toast.LENGTH_SHORT).show();

//Close MainActivity//

//        finish();
    }

    @Override
    public int getLayout() {
        return R.layout.activity_set_location;
    }


    @Override
    public void onMapClick(LatLng latLng) {
        this.latLng = latLng;
        this.googleMap.clear();
        addMarker(latLng, "i'm here", false);
    }

    @Override
    public void onPlaceSelected(Place place) {
        this.latLng = place.getLatLng();
        this.googleMap.clear();
        addMarker(place.getLatLng(), place.getName().toString(), true);
    }

    @Override
    public void onError(Status status) {

    }

    @Override
    public void onBackPressed() {
        if (TextUtils.isEmpty(editTextradius.getText().toString().trim())
                && SharedPreferenceManager.getInstance(this).getRadius() == 0)
            showAlertMessageForRadius(this, "Warning",
                    "You won't get any alert if don't give us any radius!",
                    "Ok", "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case BUTTON_POSITIVE:
                                    finish();
                                    break;
                                case BUTTON_NEGATIVE:
                                    dialogInterface.dismiss();
                            }
                        }
                    }
            );

    }

    @Override
    public void onLocationChanged(Location location) {
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        this.googleMap.clear();
        addMarker(latLng, "i'm here", true);
        SharedPreferenceManager.getInstance(this).setLatitude((float) latLng.latitude);
        SharedPreferenceManager.getInstance(this).setLongitude((float) latLng.longitude);
        mGoogleApiClientLayer.stopLocationUpdates();
    }

}