package net.fragment;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.SupportStreetViewPanoramaFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;

import net.basicmodel.R;

import org.jetbrains.annotations.NotNull;

public class StreetFragment extends Fragment{

    LatLng sydeny = new LatLng(-33.87365, 151.20689);
     StreetViewPanorama mStreetViewPanorama = null;
     Marker marker = null;
    LatLng markerPosition = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_streetview, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initView() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            markerPosition = new LatLng(location.getLatitude(), location.getLongitude());
        } else {
            markerPosition = sydeny;
        }
        SupportStreetViewPanoramaFragment panoramaFragment = (SupportStreetViewPanoramaFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.streetviewpanorama);
        panoramaFragment.getStreetViewPanoramaAsync(new OnStreetViewPanoramaReadyCallback() {
            @Override
            public void onStreetViewPanoramaReady(@NonNull @NotNull StreetViewPanorama streetViewPanorama) {
                mStreetViewPanorama = streetViewPanorama;
                mStreetViewPanorama.setOnStreetViewPanoramaChangeListener(new StreetViewPanorama.OnStreetViewPanoramaChangeListener() {
                    @Override
                    public void onStreetViewPanoramaChange(StreetViewPanoramaLocation streetViewPanoramaLocation) {
                        if (streetViewPanoramaLocation != null) {
                            marker.setPosition(streetViewPanoramaLocation.position);
                        }
                    }
                });
                mStreetViewPanorama.setPosition(markerPosition);
            }
        });
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.mapstreet);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
                googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                    @Override
                    public void onMarkerDragStart(@NonNull @NotNull Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(@NonNull @NotNull Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(@NonNull @NotNull Marker marker) {
                        mStreetViewPanorama.setPosition(marker.getPosition(), 150);
                    }
                });
                googleMap.moveCamera(getPosition(markerPosition.latitude, markerPosition.longitude));
                marker = googleMap.addMarker(getMarkerOptions(markerPosition.latitude,markerPosition.longitude));

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(@NonNull @NotNull LatLng latLng) {
                        googleMap.clear();
                        marker = googleMap.addMarker(getMarkerOptions(latLng.latitude,latLng.longitude));
                        mStreetViewPanorama.setPosition(latLng);
                    }
                });
            }
        });


    }

    private CameraUpdate getPosition(double la, double lo) {
        CameraPosition position = CameraPosition.builder().target(new LatLng(la, lo))
                .zoom(15.5f)
                .bearing(0f)
                .tilt(25f)
                .build();
        return CameraUpdateFactory.newCameraPosition(position);
    }

    private MarkerOptions getMarkerOptions(double la, double lo) {
        return new MarkerOptions().position(new LatLng(la,lo)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.location)).draggable(true);
    }

}
