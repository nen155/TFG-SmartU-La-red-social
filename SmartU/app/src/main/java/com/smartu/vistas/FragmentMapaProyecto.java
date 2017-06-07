package com.smartu.vistas;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartu.R;
import com.smartu.modelos.Proyecto;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentMapaProyecto#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentMapaProyecto extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private static final String ARG_PROYECTO = "proyecto";

    private Proyecto proyecto;
    //El mapa de google
    private GoogleMap mMap;
    //Mi vista del mapa
    private MapView mMapView;
    //Mi localización
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private LatLng miPosicion =null;
    private Bundle mBundle;

    public FragmentMapaProyecto() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param proyecto Parameter 1.
     * @return A new instance of fragment FragmentMapaProyecto.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentMapaProyecto newInstance(Proyecto proyecto) {
        FragmentMapaProyecto fragment = new FragmentMapaProyecto();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PROYECTO, proyecto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        if (getArguments() != null) {
            proyecto = getArguments().getParcelable(ARG_PROYECTO);
        }
        //Para localizar al usuario inicializo la variable de la API de Google Maps
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView = inflater.inflate(R.layout.fragment_mapa_proyecto, container, false);

        mMapView = (MapView) inflatedView.findViewById(R.id.mapa_proyecto);
        mMapView.onCreate(mBundle);

        mMapView.getMapAsync(this);
        return inflatedView;
    }
    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        mMapView.onStart();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();

    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Compruebo los permisos de la localización
        //si no los tengo me salgo del método
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //Obtengo la última localización conocida
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            miPosicion = new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Inicializo el mapa
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        //Compruebo los permisos de la localización
        //si no los tengo me salgo del método
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        //Pongo mi localización en el mapa
        googleMap.setMyLocationEnabled(true);
        //Si tengo proyectos los muestro en el mapa
        if (proyecto.getCoordenadas().compareTo("")!=0) {
                String[]coordenadas=proyecto.getCoordenadas().split(",");
                double lat = Double.parseDouble(coordenadas[0]);
                double lon = Double.parseDouble(coordenadas[1]);
                LatLng posicion = new LatLng(lat, lon);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(proyecto.getNombre());
                //Pongo la descripción en el infowindow solo con 10 caracteres
                if(proyecto.getDescripcion().length()>50)
                    markerOptions.snippet(proyecto.getDescripcion().substring(0,50));
                else
                    markerOptions.snippet(proyecto.getDescripcion());
                markerOptions.position(posicion);
                mMap.addMarker(markerOptions);
        }

        // Movemos la camara a la posicion del usuario
        if(miPosicion!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miPosicion,12));

    }
}
