package com.example.rsuproject.Fragments.Trees;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.Families.Families;
import com.example.rsuproject.Fragments.Families.FamiliesAdapter;
import com.example.rsuproject.Fragments.Families.FamiliesCreateFragment;
import com.example.rsuproject.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class TreesFragment extends Fragment {


    private ListView lv_trees;
    private FloatingActionButton btnAgregarArbol;
    private ImageButton btnBuscarArbol;

    private RequestQueue requestQueue;

    private String api, api_name;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private TreesAdapter treesAdapter;
    private List<Trees> ListTree;

    private TextInputEditText tree_name_search;

    private Bundle tree_data = new Bundle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trees, container, false);

        getActivity().setTitle("Listado de Árboles");

        lv_trees = view.findViewById(R.id.lv_trees_list);
        tree_name_search = view.findViewById(R.id.tree_name_search);

        Config config = new Config();
        //api = config.getApi_lav() + "trees";


        btnAgregarArbol = view.findViewById(R.id.btnAgregarArbol);
        btnBuscarArbol = view.findViewById(R.id.btnBuscarArbol);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();


        btnAgregarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentTransaction.replace(R.id.container_fragment, new TreesCreate());
                fragmentTransaction.commit();
            }
        });

        btnBuscarArbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api_name = config.getApi_lav() + "trees/"+tree_name_search.getText().toString();
                ListarArbolesNombre();
            }
        });

        api_name = config.getApi_lav() + "trees";
        ListarArbolesNombre();
        //ListarArboles();

        lv_trees.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tree_data.putString("tree_id", view.getTag().toString());

                Fragment fragmento = new TreesDetails();

                fragmento.setArguments(tree_data);

                fragmentTransaction.replace(R.id.container_fragment, fragmento);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    /*private void ListarArboles() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Árboles"
                , "Cargando árboles", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //familiesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);

                ListTree = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        ListTree.add(new Trees(jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("birth_date"),
                                jsonObject.getString("planting_date"),
                                jsonObject.getString("description"),
                                jsonObject.getString("latitude"),
                                jsonObject.getString("longitude"),
                                jsonObject.getInt("specie_id"),
                                jsonObject.getInt("zone_id"),
                                jsonObject.getInt("user_id"),
                                jsonObject.getString("family_name"),
                                jsonObject.getString("species_name")
                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                treesAdapter = new TreesAdapter(getContext(), ListTree);

                lv_trees.setAdapter(treesAdapter);
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

    }*/

    private void ListarArbolesNombre() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Listado de Árboles"
                , "Cargando árboles", false, false);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(api_name, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                //familiesAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);

                ListTree = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);

                        ListTree.add(new Trees(jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("birth_date"),
                                jsonObject.getString("planting_date"),
                                jsonObject.getString("description"),
                                jsonObject.getString("latitude"),
                                jsonObject.getString("longitude"),
                                jsonObject.getInt("specie_id"),
                                jsonObject.getInt("zone_id"),
                                jsonObject.getInt("user_id"),
                                jsonObject.getString("family_name"),
                                jsonObject.getString("species_name")
                        ));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                treesAdapter = new TreesAdapter(getContext(), ListTree);

                lv_trees.setAdapter(treesAdapter);
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