package com.example.rsuproject.Fragments.Trees;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Config.Tools;
import com.example.rsuproject.Fragments.Families.Families;
import com.example.rsuproject.Fragments.Families.FamiliesAdapterA;
import com.example.rsuproject.Fragments.Species.Species;
import com.example.rsuproject.Fragments.Species.SpeciesAdapterA;
import com.example.rsuproject.Fragments.Zones.Zones;
import com.example.rsuproject.Fragments.Zones.ZonesAdapterA;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class TreesEdit extends Fragment {


    private RequestQueue requestQueue;

    private String apiFamilies, apiSpecies, apiZones, apiTrees, apiTreesData;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private FamiliesAdapterA familiesAdapter;
    private List<Families> ListFamily;
    private int family_id = 0;

    private SpeciesAdapterA speciesAdapter;
    private List<Species> ListSpecies;
    private int specie_id = 0;

    private ZonesAdapterA zoneAdapter;
    private List<Zones> ListZones;
    private int zone_id = 0;

    private AutoCompleteTextView family_id_c, species_id_c, zones_id_c;

    public TextInputEditText tree_birthdate_c, tree_plantingdate_c, tree_latitude_c, tree_longitude_c, tree_name_c, tree_description_c;
    private Button btnRegistrarArbol, btnCancelarArbol;

    private Tools tools = new Tools();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_trees_edit, container, false);

        getActivity().setTitle("Registro de Árboles");

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        family_id_c = view.findViewById(R.id.family_id_e);
        species_id_c = view.findViewById(R.id.specie_id_e);
        zones_id_c = view.findViewById(R.id.zone_id_e);


        tree_birthdate_c = view.findViewById(R.id.pro_date_e);
        tree_plantingdate_c = view.findViewById(R.id.tree_plantingdate_e);
        tree_latitude_c = view.findViewById(R.id.tree_latitude_e);
        tree_longitude_c = view.findViewById(R.id.tree_longitude_e);
        tree_name_c = view.findViewById(R.id.tree_name_e);
        tree_description_c = view.findViewById(R.id.pro_description_e);

        btnRegistrarArbol = view.findViewById(R.id.btnActualizarArbol);
        btnCancelarArbol = view.findViewById(R.id.btnCancelarArbolEdit);

        Config config = new Config();
        apiFamilies = config.getApi() + "api_obtenerfamilias";
        apiSpecies = config.getApi() + "api_obtenerespecies";
        apiZones = config.getApi() + "api_obtenerzonas";
        apiTrees = config.getApi() + "api_guardararbol";

        Bundle datosRecuperados = getArguments();
        String tree_id_s = datosRecuperados.getString("tree_id");

        apiTreesData = config.getApi_lav() + "trees/"+tree_id_s+"/editar";

        tree_birthdate_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools.Calendar(getContext(), tree_birthdate_c);
            }
        });

        tree_plantingdate_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tools.Calendar(getContext(), tree_plantingdate_c);
            }
        });

        ListarFamilia();
        ListarEspecie();
        ListarZona();
        MostrarDatos();
        //Iniciar_localizacion();

        family_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                family_id = Integer.parseInt(view.getTag().toString());
            }
        });

        species_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                specie_id = Integer.parseInt(view.getTag().toString());
            }
        });

        zones_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                zone_id = Integer.parseInt(view.getTag().toString());
            }
        });

        btnRegistrarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String validacion = "";


                if (tree_name_c.getText().toString().isEmpty()) {
                    validacion = validacion + "Ingrese nombre de árbol\n";
                } else if (family_id == 0) {
                    validacion = validacion + "Indique la familia\n";
                } else if (specie_id == 0) {
                    validacion = validacion + "Indique la especie\n";
                } else if (tree_birthdate_c.getText().toString().isEmpty()) {
                    validacion = validacion + "Ingrese la fecha de nacimiento\n";
                } else if (tree_plantingdate_c.getText().toString().isEmpty()) {
                    validacion = validacion + "Ingrese la fecha de sembrado\n";
                } else if (zone_id == 0) {
                    validacion = validacion + "Indique la zona";
                }

                if (validacion.isEmpty()) {
                    RegistrarArbol();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Error de Registro");
                    alert.setMessage(validacion);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }
            }
        });

        Bundle tree_data = new Bundle();
        tree_data.putString("tree_id",tree_id_s);

        btnCancelarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new TreesShow();
                fragment.setArguments(tree_data);
                fragmentTransaction.replace(R.id.tree_container, fragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void ListarFamilia() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiFamilies, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListFamily = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ListFamily.add(new Families(jsonObject.getInt("id"),
                                jsonObject.getString("name"), jsonObject.getString("description")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                familiesAdapter = new FamiliesAdapterA(getContext(), R.layout.fragment_trees_create, R.id.item_data, ListFamily);
                family_id_c.setAdapter(familiesAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }

    private void ListarEspecie() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiSpecies, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListSpecies = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        //lista.add(jsonObject.getString("name"));
                        ListSpecies.add(new Species(
                                jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("description"),
                                jsonObject.getInt("family_id")
                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                speciesAdapter = new SpeciesAdapterA(getContext(), R.layout.fragment_trees_create, R.id.item_data, ListSpecies);
                species_id_c.setAdapter(speciesAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    private void ListarZona() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiZones, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListZones = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        ListZones.add(new Zones(
                                jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("area"),
                                jsonObject.getString("description")
                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                zoneAdapter = new ZonesAdapterA(getContext(), R.layout.fragment_trees_create, R.id.item_data, ListZones);
                zones_id_c.setAdapter(zoneAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }

    /*private void Iniciar_localizacion() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        Localizacion_Clase localizacion_clase = new Localizacion_Clase();
        localizacion_clase.setLocalizacion(this);

        final Boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!gpsEnabled) {
            Intent propiedades = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(propiedades);
        }

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);

        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) localizacion_clase);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) localizacion_clase);

    }

    public void Establecer_localizacion(Location location){
        if (location.getLongitude()!=0.0 && location.getLatitude()!=0.0){


            try {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        location.getLatitude(),location.getLongitude(),1);

                if (!list.isEmpty()){
                    Address address = list.get(0);
                    tv_direccion.setText(address.getAddressLine(0));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }*/

    private void RegistrarArbol() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Registro de Arboles"
                , "Registrando árbol", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiTrees, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    progressDialog.cancel();

                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");

                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                            fragmentTransaction.replace(R.id.container_fragment, new TreesFragment());
                            fragmentTransaction.commit();
                        }
                    });

                    AlertDialog dialog = alert.create();
                    dialog.show();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            public byte[] getBody() throws AuthFailureError {

                JSONObject parametros = new JSONObject();

                try {
                    parametros.put("name", tree_name_c.getText().toString());
                    parametros.put("birth_date", tree_birthdate_c.getText().toString());
                    parametros.put("planting_date", tree_plantingdate_c.getText().toString());
                    parametros.put("description", tree_description_c.getText().toString());
                    parametros.put("latitude", tree_latitude_c.getText().toString());
                    parametros.put("longitude", tree_longitude_c.getText().toString());
                    parametros.put("specie_id", specie_id);
                    parametros.put("zone_id", zone_id);
                    parametros.put("user_id", 1);
                    parametros.put("created_at", tree_plantingdate_c.getText().toString());
                    parametros.put("updated_at", tree_plantingdate_c.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String requestBody = parametros.toString();

                return requestBody.getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public String getBodyContentType() {
                return "application/json";
            }
        };

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    private void MostrarDatos() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiTreesData, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        tree_name_c.setText(jsonObject.getString("name"));
                        tree_plantingdate_c.setText(jsonObject.getString("planting_date"));
                        tree_birthdate_c.setText(jsonObject.getString("birth_date"));
                        tree_latitude_c.setText(jsonObject.getString("latitude"));
                        tree_longitude_c.setText(jsonObject.getString("longitude"));
                        tree_description_c.setText(jsonObject.getString("description"));
                        family_id_c.setText(jsonObject.getString("family_name"),false);
                        species_id_c.setText(jsonObject.getString("species_name"),false);
                        zones_id_c.setText(jsonObject.getString("zones_name"),false);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }


}