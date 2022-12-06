package com.example.rsuproject.Fragments.Maps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

public class TreeMapsFragment extends Fragment {

    private RequestQueue requestQueue;
    private Double lat = 0.0, lon = 0.0;
    private String tree_id_s, apiTree, treeName, treeFamily, treeSpecie, treeZone;

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

            Config config = new Config();

            Bundle datosRecuperados = getArguments();
            tree_id_s = datosRecuperados.getString("tree_id");

            apiTree = config.getApi_lav() + "trees/" + tree_id_s + "/editar";

            MostrarArbol(googleMap);


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tree_maps, container, false);
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

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Ubicación de árbol"
                , "Cargando ubicación", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiTree, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
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

                        //getActivity().setTitle("Ubicación de Árbol - " + treeName);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LatLng ubicacion = new LatLng(lat, lon);
                    //googleMap.addMarker(new MarkerOptions().position(ubicacion));
                    googleMap.addMarker(new MarkerOptions().position(ubicacion).title(treeName + "-" + treeFamily + "-" + treeSpecie + "-" + treeZone)
                            .snippet(treeSpecie));

                    //googleMap.moveCamera(CameraUpdateFactory.newLatLng(ubicacion));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ubicacion, 18.0f));
                    googleMap.getUiSettings().setZoomControlsEnabled(true);
                    googleMap.setInfoWindowAdapter(new InfoWindowsAdapter(getContext()));
                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    googleMap.setContentDescription("hola");
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