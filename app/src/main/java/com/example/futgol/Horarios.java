package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;


public class Horarios extends AppCompatActivity {

    TextView textViewFecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        textViewFecha = findViewById(R.id.textViewFecha);

        String fecha = getIntent().getStringExtra("FECHA");

        textViewFecha.setText(fecha);

        Button btnHora21 = findViewById(R.id.btnHora21);
        Button btnHora22 = findViewById(R.id.btnHora22);
        Button btnHora23 = findViewById(R.id.btnHora23);
        Button btnHora00 = findViewById(R.id.btnHora00);

        btnHora21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarReserva(fecha, "21:00");
            }
        });

        btnHora22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarReserva(fecha, "22:00");
            }
        });

        btnHora23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarReserva(fecha, "23:00");
            }
        });

        btnHora00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarReserva(fecha, "00:00");
            }
        });
    }

    private void iniciarReserva(String fecha, String hora) {
        Intent intent = new Intent(this, Reservar.class);
        intent.putExtra("FECHA", fecha);
        intent.putExtra("HORA", hora);
        startActivity(intent);
    }


    public void volver(View v){
        Intent i = new Intent(this, Calendario.class);
        startActivity(i);
    }
}
