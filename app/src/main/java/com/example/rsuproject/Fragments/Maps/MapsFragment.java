package com.example.rsuproject.Fragments.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.Trees.TreesDetails;
import com.example.rsuproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsFragment extends Fragment {

    private RequestQueue requestQueue;
    private Double lat = 0.0, lon = 0.0;
    private String apiTree, treeName, treeFamily, treeSpecie, treeZone, treeId;


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {

            getActivity().setTitle("Visualización en mapa");

            Config config = new Config();

            apiTree = config.getApi_lav() + "trees";

            fragmentManager = getActivity().getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();

            MostrarArbol(googleMap);
            /*LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void MostrarArbol(GoogleMap googleMap) {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Ubicación de Árboles"
                , "Cargando ubicaciones", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiTree, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Bundle tree_data = new Bundle();
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        lat = Double.parseDouble(jsonObject.getString("latitude"));
                        lon = Double.parseDouble(jsonObject.getString("longitude"));

                        treeName = jsonObject.getString("name");
                        treeFamily = jsonObject.getString("family_name");
                        treeSpecie = jsonObject.getString("species_name");
                        treeZone = jsonObject.getString("zones_name");
                        treeId = jsonObject.getString("id");

                        LatLng ubicacion = new LatLng(lat, lon);
                        googleMap.addMarker(new MarkerOptions().position(ubicacion).
                                title(treeName + "-" + treeFamily + "-" + treeSpecie + "-" + treeZone + "-" + treeId)
                                .snippet(treeSpecie));

                        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 12.0f));
                        googleMap.setInfoWindowAdapter(new InfoWindowsAdapter(getContext()));
                        googleMap.setOnInfoWindowClickListener(marker -> {
                            String treeData = marker.getTitle();
                            final String[] data = treeData.split("-");
                            tree_data.putString("tree_id", data[4]);
                            Fragment fragmento = new TreesDetails();
                            fragmento.setArguments(tree_data);
                            fragmentTransaction.replace(R.id.container_fragment, fragmento);
                            fragmentTransaction.commit();
                        });

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.getUiSettings().setMyLocationButtonEnabled(true);


                }
                progressDialog.cancel();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }
}