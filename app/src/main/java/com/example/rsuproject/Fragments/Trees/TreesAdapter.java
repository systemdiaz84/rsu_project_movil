package com.example.rsuproject.Fragments.Trees;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.R;

import java.util.List;

public class TreesAdapter extends BaseAdapter {

    private Context context;
    private List<Trees> ListTrees;

    private TextView tree_name, family_name, species_name;

    public TreesAdapter(Context context, List<Trees> listTrees) {
        this.context = context;
        ListTrees = listTrees;
    }

    @Override
    public int getCount() {
        return ListTrees.size();
    }

    @Override
    public Object getItem(int i) {
        return ListTrees.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vista = View.inflate(context, R.layout.trees_item, null);

        tree_name = vista.findViewById(R.id.tree_name);
        family_name = vista.findViewById(R.id.tree_name_c);
        species_name = vista.findViewById(R.id.species_name);

        tree_name.setText("ID: " + ListTrees.get(i).getId() + " - " + ListTrees.get(i).getName());
        family_name.setText("Familia: " + ListTrees.get(i).getFamily_name());
        species_name.setText("Especie: " + ListTrees.get(i).getSpecies_name());

        vista.setTag(ListTrees.get(i).getId());

        return vista;
    }
}
