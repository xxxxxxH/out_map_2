package net.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import net.basicmodel.R;
import net.entity.ResourceEntity;
import net.utils.ResourceManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    @BindView(R.id.mapview)
    MapView mapView;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.mapNormal)
    TextView mapNormal;
    @BindView(R.id.mapHybrid)
    TextView mapHybrid;
    @BindView(R.id.mapSat)
    TextView mapSat;
    @BindView(R.id.mapTer)
    TextView mapTer;

    private GoogleMap mMap = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initMap(savedInstanceState);
        initView();
        ArrayList<ResourceEntity> list = ResourceManager.getInstance().getAllResource(getActivity());
    }

    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        MapsInitializer.initialize(getActivity());
        int code = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (code != 0) {
            GooglePlayServicesUtil.getErrorDialog(code, getActivity(), 0).show();
        } else {
            mapView.getMapAsync(this);
        }
    }

    private void initView() {
        search.setOnClickListener(this);
        mapNormal.setOnClickListener(this);
        mapHybrid.setOnClickListener(this);
        mapSat.setOnClickListener(this);
        mapTer.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.search:
                try {
                    startActivityForResult(new PlacePicker.IntentBuilder().build(getActivity()),1);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.mapNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case R.id.mapSat:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTer:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
    }

    private CameraUpdate getPosition(double la, double lo) {
        CameraPosition position = CameraPosition.builder().target(new LatLng(la, lo))
                .zoom(15.5f)
                .bearing(0f)
                .tilt(25f)
                .build();
        return CameraUpdateFactory.newCameraPosition(position);
    }

    @Override
    public boolean onMyLocationButtonClick() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
            mMap.animateCamera(getPosition(location.getLatitude(), location.getLongitude()), 1000, null);
        } else {
            mMap.animateCamera(getPosition(33.92, -117.94), 1000, null);
        }
        mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location)));
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull @NotNull Location location) {
        Toast.makeText(getActivity(), "Current location: " + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(1);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(40.00, -73.00)));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.00, -73.00)).title("Marker")
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.location)));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Place place = PlacePicker.getPlace(data, getActivity());
            String msg = String.format("Place: %s", place.getName());
            Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            mMap.animateCamera(getPosition(place.getLatLng().latitude, place.getLatLng().longitude), 1000, null);
        }
    }
}
