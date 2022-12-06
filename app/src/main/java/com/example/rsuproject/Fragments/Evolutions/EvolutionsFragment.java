package com.example.rsuproject.Fragments.Evolutions;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.Procedures.ProcedureCreate;
import com.example.rsuproject.Fragments.Procedures.Procedures;
import com.example.rsuproject.Fragments.Procedures.ProceduresAdapter;
import com.example.rsuproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EvolutionsFragment extends Fragment {

    private ListView lv_evolutions_list;

    private RequestQueue requestQueue;

    private String api;

    private String tree_id_s;
    private Bundle tree_data = new Bundle();

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private EvolutionsAdapter evolutionsAdapter;
    private List<Evolutions> ListEvolutions;

    private FloatingActionButton btnAgregarEvolucion;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evolutions, container, false);


        lv_evolutions_list = view.findViewById(R.id.lv_evolutions_list);
        btnAgregarEvolucion = view.findViewById(R.id.btnAgregarEvolucion);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //getActivity().setTitle("Evolución de Árbol");

        Bundle datosRecuperados = getArguments();
        tree_id_s = datosRecuperados.getString("tree_id");

        tree_data.putString("tree_id", tree_id_s);

        Config config = new Config();
        api = config.getApi() + "api_obtenerevolucionporarbol/" + tree_id_s;

        btnAgregarEvolucion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new EvolutionsCreate();
                fragment.setArguments(tree_data);

                fragmentTransaction.replace(R.id.tree_container, fragment);
                fragmentTransaction.commit();
            }
        });

        ListarEvoluciones();


        return view;
    }

    private void ListarEvoluciones() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Evolución"
                , "Cargando", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListEvolutions = new ArrayList<>();

                if (response.length() == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    alert.setMessage("No se encontraron evoluciones");
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                } else {
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            jsonObject = response.getJSONObject(i);

                            ListEvolutions.add(new Evolutions(
                                    jsonObject.getInt("id"),
                                    jsonObject.getInt("tree_id"),
                                    jsonObject.getString("height"),
                                    jsonObject.getString("width"),
                                    jsonObject.getString("date")
                            ));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    evolutionsAdapter = new EvolutionsAdapter(getContext(), ListEvolutions);

                    lv_evolutions_list.setAdapter(evolutionsAdapter);
                }


                progressDialog.cancel();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.cancel();
                //Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setMessage("No se encontraron evoluciones");
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

            }
        });

        requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);

    }
}