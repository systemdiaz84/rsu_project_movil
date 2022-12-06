package com.example.rsuproject.Fragments.Procedures;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Trees.Trees;
import com.example.rsuproject.R;

import java.util.List;

public class ProceduresAdapter extends BaseAdapter {
    private Context context;
    private List<Procedures> ListProcedures;

    private TextView pro_date, pro_type;

    public ProceduresAdapter(Context context, List<Procedures> listProcedures) {
        this.context = context;
        ListProcedures = listProcedures;
    }

    @Override
    public int getCount() {
        return ListProcedures.size();
    }

    @Override
    public Object getItem(int i) {
        return ListProcedures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = View.inflate(context, R.layout.procedure_item, null);

        pro_date = vista.findViewById(R.id.pro_date);
        pro_type = vista.findViewById(R.id.pro_type);

        pro_date.setText(ListProcedures.get(i).getDate());
        pro_type.setText(ListProcedures.get(i).getProcedure_type_name());

        //vista.setTag(ListProcedures.get(i).getId());

        return vista;
    }
}
