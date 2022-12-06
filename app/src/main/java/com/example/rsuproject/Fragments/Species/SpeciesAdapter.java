package com.example.rsuproject.Fragments.Species;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Families.Families;
import com.example.rsuproject.R;

import java.util.List;

public class SpeciesAdapter extends BaseAdapter {

    private Context context;
    private List<Species> ListSpecies;

    public SpeciesAdapter(Context context, List<Species> listSpecies) {
        this.context = context;
        ListSpecies = listSpecies;
    }

    private TextView specie_id_list;
    private TextView specie_name_list;

    @Override
    public int getCount() {
        return ListSpecies.size();
    }

    @Override
    public Object getItem(int i) {
        return ListSpecies.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = View.inflate(context, R.layout.list_item,null);

        //specie_id_list=(TextView) vista.findViewById(R.id.specie_id_list);
        specie_name_list=(TextView) vista.findViewById(R.id.item_data);

        //specie_id_list.setText(String.valueOf(ListSpecies.get(i).getId()));
        specie_name_list.setText(ListSpecies.get(i).getName());

        vista.setTag(ListSpecies.get(i));

        return vista;
    }
}
