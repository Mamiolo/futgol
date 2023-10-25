package com.example.futgol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CalendarioAdmin extends AppCompatActivity {

    TextView fecha;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario_admin);

        calendarView = findViewById(R.id.calendarView);
        fecha = findViewById(R.id.fecha);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                String fechaget = i2 +"/"+ i1 +"/"+ i;
                fecha.setText(fechaget);

            }
        });

    }

    public void volver(View v){
        Intent i = new Intent(this, MenuAdmin.class);

        startActivity(i);
    }

    public void horarios(View v) {
        String fechaget = fecha.getText().toString();


        if (fechaget != null) {
            Log.d("Fecha", "Fecha ingresada correctamente: " + fechaget);
            Intent i = new Intent(this, Deshabilitar.class);
            i.putExtra("FECHA", fechaget);


            startActivity(i);
        } else {
            Toast.makeText(this, "Fecha no válida", Toast.LENGTH_SHORT).show();
        }
    }


}