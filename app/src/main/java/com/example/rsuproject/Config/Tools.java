package com.example.rsuproject.Config;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class Tools {


    public void Calendar(Context context, final TextInputEditText textInputEditText) {

        final Calendar calendar = Calendar.getInstance();

        int dia = calendar.get(Calendar.DAY_OF_MONTH);
        int mes = calendar.get(Calendar.MONTH);
        int annio = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                textInputEditText.setText(dateFormat(i2, i1, i));
            }
        }, annio, mes, dia);

        datePickerDialog.show();

    }

    private String dateFormat(int d, int m, int y) {
        String s_mes, dia, fecha = "";
        int mes;

        if (d < 10) {
            dia = "0" + d;
        } else {
            dia = String.valueOf(d);
        }

        mes = m + 1;

        if (mes < 10) {
            s_mes = "0" + mes;
        } else {
            s_mes = String.valueOf(m);
        }

        fecha = y + "/" + s_mes + "/" + dia;

        return fecha;
    }
}
