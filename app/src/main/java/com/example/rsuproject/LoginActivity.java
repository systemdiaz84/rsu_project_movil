package com.example.rsuproject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText login_email, login_password;
    private Button btnLogin;

    private String ApiLogin, message;
    private int code;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);

        btnLogin = findViewById(R.id.btnLogin);

        Config config = new Config();

        ApiLogin = config.getApi_lav() + "login";

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();
            }
        });

    }

    private void login() {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Validando",
                "Espere un momento...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ApiLogin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    progressDialog.cancel();
                    JSONObject jsonObject = new JSONObject(response);
                    message = jsonObject.getString("message");
                    code = jsonObject.getInt("code");


                    AlertDialog.Builder alert = new AlertDialog.Builder(LoginActivity.this);
                    alert.setTitle("RSU - Login");
                    alert.setMessage(message);
                    alert.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            dialogInterface.cancel();

                            if (code == 1) {
                                iniciar_sesion();
                                Intent menu = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(menu);
                                finish();
                            }
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
                progressDialog.cancel();
                Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> parametros = new HashMap<>();

                parametros.put("email", login_email.getText().toString());
                parametros.put("password", login_password.getText().toString());

                return parametros;

            }
        };
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void iniciar_sesion(){
        SharedPreferences sesion = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sesion.edit();
        editor.putString("email",login_email.getText().toString());
        //editor.putString("clave",et_clave_login.getText().toString());
        editor.putBoolean("estado",true);
        editor.commit();
    }
}