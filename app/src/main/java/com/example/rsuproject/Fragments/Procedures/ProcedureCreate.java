package com.example.rsuproject.Fragments.Procedures;

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
import com.example.rsuproject.Fragments.Families.Families;
import com.example.rsuproject.Fragments.Families.FamiliesAdapterA;
import com.example.rsuproject.Fragments.ProcedureType.ProcedureType;
import com.example.rsuproject.Fragments.ProcedureType.ProcedureTypeAdapterA;
import com.example.rsuproject.Fragments.Responsibles.Responsibles;
import com.example.rsuproject.Fragments.Responsibles.ResponsiblesAdapterA;
import com.example.rsuproject.Fragments.Trees.TreesFragment;
import com.example.rsuproject.R;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProcedureCreate extends Fragment {

    private TextInputEditText pro_date_c, pro_description_c;
    private AutoCompleteTextView pro_type_procedure_id_c, pro_responsible_id_c;
    private int dia, mes, annio;
    private String apiTipoPro, apiResponsables, apiProcedimientos;

    private Button btnRegistrarPro, btnCancelarPro;

    private RequestQueue requestQueue;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private ProcedureTypeAdapterA procedureTypeAdapterA;
    private List<ProcedureType> ListTypePro;
    private int procedure_type_id = 0;

    private ResponsiblesAdapterA responsiblesAdapterA;
    private List<Responsibles> ListResponsible;
    private int responsible_id = 0;

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
        View view = inflater.inflate(R.layout.fragment_procedure_create, container, false);

        pro_date_c = view.findViewById(R.id.pro_date_c);
        pro_type_procedure_id_c = view.findViewById(R.id.pro_type_procedure_id_c);
        pro_responsible_id_c = view.findViewById(R.id.pro_responsible_id_c);
        pro_description_c = view.findViewById(R.id.pro_description_c);

        btnRegistrarPro = view.findViewById(R.id.btnRegistrarPro);
        btnCancelarPro = view.findViewById(R.id.btnCancelarPro);

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

        //getActivity().setTitle("Registro de Procedimiento");

        final Calendar calendar = Calendar.getInstance();

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH);
        annio = calendar.get(Calendar.YEAR);

        Bundle datosRecuperados = getArguments();
        tree_id_s = datosRecuperados.getString("tree_id");

        tree_data.putString("tree_id", tree_id_s);

        Config config = new Config();
        apiTipoPro = config.getApi() + "api_obtenerprocedimientotipos";
        apiResponsables = config.getApi() + "api_obtenerresponsables";
        apiProcedimientos = config.getApi() + "api_guardarprocedimiento";

        ListarTipoProcedimientos();
        ListarResposables();

        pro_date_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar(pro_date_c);
            }
        });

        pro_type_procedure_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                procedure_type_id = Integer.parseInt(view.getTag().toString());
            }
        });

        pro_responsible_id_c.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                responsible_id = Integer.parseInt(view.getTag().toString());
            }
        });

        btnRegistrarPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validacion = "";

                if (pro_date_c.getText().toString().isEmpty()) {
                    validacion = validacion + "Indique la fecha del procedimiento";
                } else if (pro_type_procedure_id_c.getText().toString().isEmpty()) {
                    validacion = validacion + "Indique el tipo de procedimiento";
                } else if (pro_responsible_id_c.getText().toString().isEmpty()) {
                    validacion = validacion + "Indique el responsable";
                }
                if (validacion.isEmpty()) {
                    RegistrarProcedimiento();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

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

        btnCancelarPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new ProcedureFragment();
                fragment.setArguments(tree_data);

                fragmentTransaction.replace(R.id.tree_container, fragment);
                fragmentTransaction.commit();
            }
        });

        return view;
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

    private void ListarTipoProcedimientos() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiTipoPro, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListTypePro = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ListTypePro.add(new ProcedureType(jsonObject.getInt("id"),
                                jsonObject.getString("name"), jsonObject.getString("description")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                procedureTypeAdapterA = new ProcedureTypeAdapterA(getContext(), R.layout.fragment_procedure_create, R.id.item_data, ListTypePro);
                pro_type_procedure_id_c.setAdapter(procedureTypeAdapterA);

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

    private void ListarResposables() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(apiResponsables, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;

                ListResponsible = new ArrayList<>();

                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        ListResponsible.add(new Responsibles(jsonObject.getInt("id"),
                                jsonObject.getString("name") + " " + jsonObject.getString("lastname"), jsonObject.getString("dni")));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                responsiblesAdapterA = new ResponsiblesAdapterA(getContext(), R.layout.fragment_procedure_create, R.id.item_data, ListResponsible);
                pro_responsible_id_c.setAdapter(responsiblesAdapterA);

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

    private void RegistrarProcedimiento() {

        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Registro de Procedimientos"
                , "Registrando procedimiento", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, apiProcedimientos, new Response.Listener<String>() {
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

                            Fragment fragment = new ProcedureFragment();
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
                    parametros.put("date", pro_date_c.getText().toString());
                    parametros.put("description", pro_description_c.getText().toString());
                    parametros.put("procedure_type_id", procedure_type_id);
                    parametros.put("responsible_id", responsible_id);
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
}