package com.example.rsuproject.Fragments.Trees;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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
import com.example.rsuproject.Fragments.Families.FamiliesFragment;
import com.example.rsuproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class TreesShow extends Fragment {

    private RequestQueue requestQueue;
    private String apiLista, apiElimina, treeName="";

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private TextView tree_name_s, tree_specie_s, tree_zone_s, tree_birthdate_s, tree_planting_s,
            tree_latitude_s, tree_longitude_s, tree_description_s;

    private Bundle tree_data = new Bundle();
    private String tree_id_s;

    private Button btnRetornarArbol, btnElimarArbol, btnEditarArbol;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trees_show, container, false);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        tree_name_s = view.findViewById(R.id.tree_name_s);
        tree_specie_s = view.findViewById(R.id.tree_specie_s);
        tree_zone_s = view.findViewById(R.id.tree_zone_s);
        tree_birthdate_s = view.findViewById(R.id.tree_birthdate_s);
        tree_planting_s = view.findViewById(R.id.tree_planting_s);
        tree_latitude_s = view.findViewById(R.id.tree_latitude_s);
        tree_longitude_s = view.findViewById(R.id.tree_longitude_s);
        tree_description_s = view.findViewById(R.id.tree_description_s);

        btnRetornarArbol = view.findViewById(R.id.btnRetornarArbol);
        btnElimarArbol = view.findViewById(R.id.btnElimarArbol);
        btnEditarArbol  = view.findViewById(R.id.btnEditarArbol);


        Config config = new Config();

        Bundle datosRecuperados = getArguments();
        tree_id_s = datosRecuperados.getString("tree_id");

        apiLista = config.getApi() + "api_obtenerarbol_desc/" + tree_id_s;
        apiElimina = config.getApi() + "api_eliminararbol";

        MostrarArbol();

        btnEditarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tree_data.putString("tree_id", tree_id_s);
                Fragment fragment = new TreesEdit();
                fragment.setArguments(tree_data);

                fragmentTransaction.replace(R.id.tree_container, fragment);
                fragmentTransaction.commit();
            }
        });


        btnRetornarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new TreesFragment());
                fragmentTransaction.commit();
            }
        });

        btnElimarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                alert.setMessage("¿Seguro de eliminar?");
                alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        EliminarArbol();
                    }
                });
                alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.show();
            }
        });

        return view;
    }

    private void MostrarArbol() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Detalle de Árbol"
                , "Cargando datos", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiLista, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        tree_name_s.setText(jsonObject.getString("name"));
                        tree_specie_s.setText(jsonObject.getString("especie"));
                        tree_zone_s.setText(jsonObject.getString("zona"));
                        tree_birthdate_s.setText(jsonObject.getString("birth_date"));
                        tree_planting_s.setText(jsonObject.getString("planting_date"));
                        tree_latitude_s.setText(jsonObject.getString("latitude"));
                        tree_longitude_s.setText(jsonObject.getString("longitude"));
                        tree_description_s.setText(jsonObject.getString("description"));
                        treeName=jsonObject.getString("name");
                        getActivity().setTitle(treeName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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

    private void EliminarArbol() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Elimación de Arbol"
                , "Eliminado árbol", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiElimina, new Response.Listener<String>() {
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
                    progressDialog.cancel();
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
                    parametros.put("id", tree_id_s);

                } catch (JSONException e) {
                    Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
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
}