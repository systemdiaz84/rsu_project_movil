package com.example.rsuproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.rsuproject.Config.Config;
import com.example.rsuproject.Fragments.Families.FamiliesFragment;
import com.example.rsuproject.Fragments.MainFragment;
import com.example.rsuproject.Fragments.Maps.MapsFragment;
import com.example.rsuproject.Fragments.Trees.TreesFragment;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolbar;
    private NavigationView navigationView;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private RequestQueue requestQueue;

    private TextView user_data, user_email;

    private String APILogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("RSU Project");

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigationview);

        setSupportActionBar(toolbar);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new MainFragment());
        fragmentTransaction.commit();

        View cabeceraMenu = navigationView.getHeaderView(0);

        user_data = cabeceraMenu.findViewById(R.id.user_data);
        user_email = cabeceraMenu.findViewById(R.id.user_email);

        //user_data.setText("hola");

        SharedPreferences datos = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        String email = datos.getString("email", "");

        Config config = new Config();
        APILogin = config.getApi_lav() + "login/data/" + email;

        obtener_datos();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = new Fragment();

        if (item.getItemId() == R.id.home) {

            fragment = new MainFragment();

        }
        if (item.getItemId() == R.id.trees) {

            fragment = new TreesFragment();

        }
        if (item.getItemId() == R.id.maps) {

            fragment = new MapsFragment();
        }
        if (item.getItemId() == R.id.families) {

            fragment = new FamiliesFragment();

        }
        if (item.getItemId() == R.id.session_out) {

            AlertDialog.Builder mensaje = new AlertDialog.Builder(this);
            mensaje.setTitle("Proyecto RSU");
            mensaje.setMessage("¿Seguro de cerrar la sesión?");
            mensaje.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    cerrar_sesion();
                }
            });
            mensaje.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });
            AlertDialog dialog = mensaje.create();
            dialog.show();

            fragment = new MainFragment();

        }

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment);
        fragmentTransaction.commit();

        drawerLayout.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       int id = item.getItemId();

        if (id == R.id.action_settings) {
            Toast.makeText(this, "Hola", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void obtener_datos() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(APILogin, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                JSONObject jsonObject = null;
                try {
                    for (int i = 0; i < response.length(); i++) {
                        jsonObject = response.getJSONObject(i);
                    }

                    /*SharedPreferences usuario = getSharedPreferences("sesion", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = usuario.edit();
                    editor.putInt("name", jsonObject.getInt("name"));
                    editor.commit();*/

                    user_data.setText(jsonObject.getString("name"));
                    user_email.setText(jsonObject.getString("email"));

                    /*String urlFoto=url.getUrlBase()+jsonObject.getString("usu_foto");

                    Picasso.get().load(urlFoto).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).
                            error(R.drawable.user_logo).into(iv_foto_perfil_menu);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }


    private void cerrar_sesion() {

        SharedPreferences sesion = getSharedPreferences("sesion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sesion.edit();
        editor.clear();
        editor.putBoolean("estado", false);
        editor.commit();
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
        finish();
    }

}