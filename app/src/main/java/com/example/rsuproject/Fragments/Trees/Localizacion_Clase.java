package com.example.rsuproject.Fragments.Trees;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class Localizacion_Clase implements LocationListener {

    TreesCreate localizacion;

    public void setLocalizacion(TreesCreate localizacion) {
        this.localizacion = localizacion;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        String latitud = String.valueOf(location.getLatitude());
        String longitud = String.valueOf(location.getLongitude());

        this.localizacion.tree_latitude_c.setText(latitud);
        this.localizacion.tree_longitude_c.setText(longitud);
        //this.localizacion.Establecer_localizacion(location);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
}
