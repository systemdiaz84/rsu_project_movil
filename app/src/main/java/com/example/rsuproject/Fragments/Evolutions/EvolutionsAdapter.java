package com.example.rsuproject.Fragments.Evolutions;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Procedures.Procedures;
import com.example.rsuproject.R;

import java.util.List;

public class EvolutionsAdapter extends BaseAdapter {
    private Context context;
    private List<Evolutions> ListEvolutions;

    private TextView evo_date, evo_height, evo_width;

    public EvolutionsAdapter(Context context, List<Evolutions> listEvolutions) {
        this.context = context;
        ListEvolutions = listEvolutions;
    }

    @Override
    public int getCount() {
        return ListEvolutions.size();
    }

    @Override
    public Object getItem(int i) {
        return ListEvolutions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = View.inflate(context, R.layout.evolution_item, null);

        evo_date = vista.findViewById(R.id.evo_date);
        evo_height = vista.findViewById(R.id.evo_height);
        evo_width = vista.findViewById(R.id.evo_width);

        evo_date.setText(ListEvolutions.get(i).getDate());
        evo_height.setText("Alto: " + ListEvolutions.get(i).getHeight() + " cm.");
        evo_width.setText("Ancho " + ListEvolutions.get(i).getWidth() + " cm.");

        //vista.setTag(ListProcedures.get(i).getId());

        return vista;
    }
}
