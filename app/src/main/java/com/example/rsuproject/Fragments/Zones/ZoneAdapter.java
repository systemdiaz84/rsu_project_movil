package com.example.rsuproject.Fragments.Zones;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.rsuproject.Fragments.Species.Species;
import com.example.rsuproject.R;

import java.util.List;

public class ZoneAdapter extends BaseAdapter {


    private Context context;
    private List<Zones> ListZones;

    private TextView zone_id_list;
    private TextView zone_name_list;

    public ZoneAdapter(Context context, List<Zones> listZones) {
        this.context = context;
        ListZones = listZones;
    }

    @Override
    public int getCount() {
        return ListZones.size();
    }

    @Override
    public Object getItem(int i) {
        return ListZones.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View vista = View.inflate(context, R.layout.list_item,null);

        //zone_id_list=(TextView) vista.findViewById(R.id.zone_id_list);
        zone_name_list=(TextView) vista.findViewById(R.id.item_data);

        //zone_id_list.setText(String.valueOf(ListZones.get(i).getId()));
        zone_name_list.setText(ListZones.get(i).getName());

        vista.setTag(ListZones.get(i));

        return vista;
    }
}
