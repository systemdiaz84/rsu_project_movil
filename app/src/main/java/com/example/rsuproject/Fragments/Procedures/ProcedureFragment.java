package com.example.rsuproject.Fragments.Procedures;

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
import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.Fragments.Trees.TreesAdapter;
import com.example.rsuproject.Fragments.Trees.TreesCreate;
import com.example.rsuproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProcedureFragment extends Fragment {

    private ListView lv_procedure_list;

    private RequestQueue requestQueue;

    private String api;

    private String tree_id_s;
    private Bundle tree_data = new Bundle();

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ProceduresAdapter proceduresAdapter;
    private List<Procedures> ListProcedure;

    private FloatingActionButton btnAgregarProcedimiento;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_procedure, container, false);

        lv_procedure_list = view.findViewById(R.id.lv_procedure_list);
        btnAgregarProcedimiento = view.findViewById(R.id.btnAgregarProcedimiento);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //getActivity().setTitle("Procedimientos de Árbol");

        Bundle datosRecuperados = getArguments();
        tree_id_s = datosRecuperados.getString("tree_id");

        tree_data.putString("tree_id", tree_id_s);

        Config config = new Config();
        api = config.getApi() + "api_obtenerprocedimientosporarbolid/" + tree_id_s;

        btnAgregarProcedimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ProcedureCreate();
                fragment.setArguments(tree_data);

                fragmentTransaction.replace(R.id.tree_container, fragment);
                fragmentTransaction.commit();
            }
        });

        ListarArboles();

        return view;
    }

    private void ListarArboles() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Procedimientos"
                , "Cargando procedimientos", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListProcedure = new ArrayList<>();

                if (response.length() == 0) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                    alert.setMessage("No se encontraron procedimientos");
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

                            ListProcedure.add(new Procedures(
                                    jsonObject.getString("date"),
                                    jsonObject.getString("procedure_type_name")
                            ));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    proceduresAdapter = new ProceduresAdapter(getContext(), ListProcedure);

                    lv_procedure_list.setAdapter(proceduresAdapter);
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