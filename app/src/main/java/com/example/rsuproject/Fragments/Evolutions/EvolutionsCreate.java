package com.example.rsuproject.Fragments.Evolutions;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import com.example.rsuproject.Fragments.ProcedureType.ProcedureType;
import com.example.rsuproject.Fragments.ProcedureType.ProcedureTypeAdapterA;
import com.example.rsuproject.Fragments.Procedures.ProcedureFragment;
import com.example.rsuproject.Fragments.Responsibles.Responsibles;
import com.example.rsuproject.Fragments.Responsibles.ResponsiblesAdapterA;
import com.example.rsuproject.Fragments.States.States;
import com.example.rsuproject.Fragments.States.StatesAdapterA;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EvolutionsCreate extends Fragment {

    private TextInputEditText evo_date_c, evo_description_c, evo_height_c, evo_width_c;
    private AutoCompleteTextView evo_state_id_c;
    private int dia, mes, annio;
    private String apiEstados, apiEvoluciones;

    private Button btnRegistrarEvo, btnCancelarEvo;

    private RequestQueue requestQueue;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private StatesAdapterA statesAdapterA;
    private List<States> ListStates;
    private int state_id = 0;

    private Bundle tree_data = new Bundle();
    private String tree_id_s;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_evolutions_create, container, false);

        evo_date_c = view.findViewById(R.id.evo_date_c);
        evo_description_c = view.findViewById(R.id.evo_description_c);
        evo_height_c = view.findViewById(R.id.evo_height_c);
        evo_width_c = view.findViewById(R.id.evo_width_c);
        evo_state_id_c = view.findViewById(R.id.evo_state_id_c);

        btnRegistrarEvo = view.findViewById(R.id.btnRegistrarEvo);
        btnCancelarEvo = view.findViewById(R.id.btnCancelarEvo);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //getActivity().setTitle("Registro de Evolución");

        final Calendar calendar = Calendar.getInstance();

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        annio = calendar.get(Calendar.YEAR);

        Bundle datosRecuperados = getArguments();
        tree_id_s = datosRecuperados.getString("tree_id");

        tree_data.putString("tree_id", tree_id_s);

        Config config = new Config();
        apiEstados = config.getApi() + "api_obtenerestados_desc";
        apiEvoluciones = config.getApi() + "api_guardarevolucion";

        evo_date_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar(evo_date_c);
            }
        });

        btnCancelarEvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new EvolutionsFragment();
                fragment.setArguments(tree_data);

                fragmentTransaction.replace(R.id.tree_container, fragment);
                fragmentTransaction.commit();
            }
        });

        evo_state_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                state_id= Integer.parseInt(view.getTag().toString());
            }
        });

        btnRegistrarEvo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegistrarEvolucion();
            }
        });

        ListarEstados();

        return view;
    }

    private void ListarEstados() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiEstados, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListStates = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ListStates.add(new States(jsonObject.getInt("id"),
                                jsonObject.getString("name"),
                                jsonObject.getString("description")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                statesAdapterA = new StatesAdapterA(getContext(), R.layout.fragment_procedure_create, R.id.item_data, ListStates);
                evo_state_id_c.setAdapter(statesAdapterA);

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

    private void RegistrarEvolucion() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Registro de Evoluciones"
                , "Registrando evolución", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiEvoluciones, new Response.Listener<String>() {
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

                            Fragment fragment = new EvolutionsFragment();
                            fragment.setArguments(tree_data);

                            fragmentTransaction.replace(R.id.tree_container, fragment);
                            fragmentTransaction.commit();
                        }
                    });

                    AlertDialog dialog = alert.create();
                    dialog.show();

                } catch (JSONException e) {
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
                    parametros.put("date", evo_date_c.getText().toString());
                    parametros.put("description", evo_description_c.getText().toString());
                    parametros.put("state_id", state_id);
                    parametros.put("height", evo_height_c.getText().toString());
                    parametros.put("width", evo_width_c.getText().toString());
                    parametros.put("tree_id", tree_id_s);
                    parametros.put("user_id", 1);

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

    private void Calendar(TextInputEditText textInputEditText) {

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                textInputEditText.setText(dateFormat(i2, i1, i));
            }
        }, annio, mes, dia);

        datePickerDialog.show();
    }

    private String dateFormat(int d, int m, int y) {
        String s_mes, dia, fecha = "";
        int mes;

        if (d < 10) {
            dia = "0" + d;
        } else {
            dia = String.valueOf(d);
        }

        mes = m + 1;

        if (mes < 10) {
            s_mes = "0" + mes;
        } else {
            s_mes = String.valueOf(m);
        }

        fecha = y + "/" + s_mes + "/" + dia;

        return fecha;
    }
}