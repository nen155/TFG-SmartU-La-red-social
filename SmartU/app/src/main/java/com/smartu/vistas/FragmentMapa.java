package com.smartu.vistas;

import android.Manifest;
import android.content.Context;
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
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.smartu.R;
import com.smartu.modelos.Proyecto;

import java.util.ArrayList;

/**
 * Una subclase simple {@link Fragment}.
 * Las Activities que lo contentgan deben de implementar
 * {@link OnProyectoSeleccionadoMapaListener} interfaz
 * para manejar la interacción de eventos.
 * Usa el método factoría {@link FragmentMapa#newInstance} para
 * crear una instancia de este fragmento.
 */
public class FragmentMapa extends Fragment implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    //ArrayList de obras para cargar y pasar cuando se cambie de Fragment
    private ArrayList<Proyecto> proyectos = new ArrayList<>();
    //El argumento que tienen que pasarme o que tengo que pasar en los Intent
    private static final String ARG_PROYECTOS = "proyectos";
    //El mapa de google
    private GoogleMap mMap;
    //Mi vista del mapa
    private MapView mMapView;

    private Bundle mBundle;
    //El escuchador para pasar la información a la activity
    private OnProyectoSeleccionadoMapaListener mListener;
    //Mi localización
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;

    private LatLng miPosicion =null;

    public FragmentMapa() {
        // Required empty public constructor
    }

    /**
     * Usar este constructor para crear una instancia de
     * este fragment con parámetros
     *
     * @param proyectos Parametro 1.
     * @return A devuelve una nueva instancia del fragment.
     */
    public static FragmentMapa newInstance(ArrayList<Proyecto> proyectos) {
        FragmentMapa fragment = new FragmentMapa();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PROYECTOS, proyectos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = savedInstanceState;
        if (getArguments() != null) {
            proyectos = getArguments().getParcelableArrayList(ARG_PROYECTOS);
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
        View inflatedView = inflater.inflate(R.layout.fragment_mapa, container, false);

        mMapView = (MapView) inflatedView.findViewById(R.id.mapa);
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void onButtonPressed(Proyecto proyecto) {
        if (mListener != null) {
            mListener.onProyectoSeleccionadoMapa(proyecto);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnProyectoSeleccionadoMapaListener) {
            mListener = (OnProyectoSeleccionadoMapaListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnProyectoSeleccionadoMapaListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Inicializo el mapa
        MapsInitializer.initialize(getContext());
        mMap = googleMap;
        //Compruebo los permisos de la localización
        //si no los tengo me salgo del método
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
           return;
        }
        //Pongo mi localización en el mapa
        googleMap.setMyLocationEnabled(true);
        //Si tengo proyectos los muestro en el mapa
        if (!proyectos.isEmpty()) {
            for (Proyecto p: proyectos) {
                String[]coordenadas=p.getCoordenadas().split(",");
                double lat = Double.parseDouble(coordenadas[0]);
                double lon = Double.parseDouble(coordenadas[1]);
                LatLng posicion = new LatLng(lat, lon);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.title(p.getNombre());
                //Guardo en el zIndex el ID del proyecto, pues no me va a importar que
                //unos markers se vean por encima de otros pero necesito pasar el ID
                markerOptions.zIndex(p.getId());
                //Pongo la descripción en el infowindow solo con 10 caracteres
                if(p.getDescripcion().length()>50)
                    markerOptions.snippet(p.getDescripcion().substring(0,50));
                else
                    markerOptions.snippet(p.getDescripcion());
                markerOptions.position(posicion);
                mMap.addMarker(markerOptions);
            }
        }
        // Movemos la camara a la posicion del usuario
        if(miPosicion!=null)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miPosicion,12));

        //Creamos el evento click de los marker
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                long id=(long)marker.getZIndex();
                Proyecto p = proyectos.stream().filter(proyecto -> proyecto.getId()==id).findFirst().get();
                mListener.onProyectoSeleccionadoMapa(p);
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Compruebo los permisos de la localización
        //si no los tengo me salgo del método
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
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

    /**
     * Esta interfaz debe ser implementada por las activities que contienen este
     * fragment para permitir una interacción con este fragment y comunicar a
     * a la activity y potencialmente otros fragments contenidos en esta
     * activity.
     * <p>
     * Referencias a la lección Android Training <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnProyectoSeleccionadoMapaListener {
        void onProyectoSeleccionadoMapa(Proyecto proyecto);
    }
}
