package com.example.rsuproject.Fragments.Trees;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rsuproject.Fragments.Evolutions.EvolutionsFragment;
import com.example.rsuproject.Fragments.MainFragment;
import com.example.rsuproject.Fragments.Maps.TreeMapsFragment;
import com.example.rsuproject.Fragments.Procedures.ProcedureFragment;
import com.example.rsuproject.R;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class TreesDetails extends Fragment implements BottomNavigationView.OnNavigationItemSelectedListener {


    private BottomNavigationView bottomNavigationView;
    private Bundle tree_data = new Bundle();

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_trees_details, container, false);

        bottomNavigationView = view.findViewById(R.id.bottomTreeMenu);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        this.onNavigationItemSelected(bottomNavigationView.getMenu().getItem(0));

        return view;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = new Fragment();

        int id = item.getItemId();

        Bundle datosRecuperados = getArguments();
        String tree_id_s = datosRecuperados.getString("tree_id");

        tree_data.putString("tree_id", tree_id_s);

        if (id == R.id.opTreeShow) {
            fragment = new TreesShow();
            fragment.setArguments(tree_data);
        } else if (id == R.id.opTreeEvo) {
            fragment = new EvolutionsFragment();
            fragment.setArguments(tree_data);
        } else if (id == R.id.opTreePRo) {
            fragment = new ProcedureFragment();
            fragment.setArguments(tree_data);
        }else if (id == R.id.opTreeMap) {
            fragment = new TreeMapsFragment();
            fragment.setArguments(tree_data);
        }

        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.tree_container, fragment);
        fragmentTransaction.commit();

        /*FragmentTransaction fragmentTransaction = this.getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.tree_container, fragment);
        fragmentTransaction.commit();*/

        return true;
    }
}