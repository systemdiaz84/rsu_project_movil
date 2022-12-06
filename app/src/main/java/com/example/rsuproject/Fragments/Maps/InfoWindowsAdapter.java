package com.example.rsuproject.Fragments.Maps;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.rsuproject.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindowsAdapter implements GoogleMap.InfoWindowAdapter {

    Context context;
    String treeName, treeFamily, treeSpecie, treeZone, treeData;
    TextView tree_name_m, tree_family_m, tree_specie_m, tree_zone_m;

    public InfoWindowsAdapter(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View getInfoContents(@NonNull Marker marker) {
        return null;
    }

    @Nullable
    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        View view = LayoutInflater.from(context).inflate(R.layout.info_window_map, null);
        tree_name_m = view.findViewById(R.id.tree_name_m);
        tree_family_m = view.findViewById(R.id.tree_family_m);
        tree_specie_m = view.findViewById(R.id.tree_specie_m);
        tree_zone_m = view.findViewById(R.id.tree_zone_m);

        treeData = marker.getTitle();
        final String[] data = treeData.split("-");

        tree_name_m.setText(data[0]);
        tree_family_m.setText("Familia: " + data[1]);
        tree_specie_m.setText("Especie: " + data[2]);
        tree_zone_m.setText("Zona: " + data[3]);

        return view;
    }
}
