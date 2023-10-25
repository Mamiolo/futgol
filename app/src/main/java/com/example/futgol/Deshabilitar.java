package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Deshabilitar extends AppCompatActivity {

    private Spinner spinnerOpcion;
    private Spinner spinnerHora;
    private EditText editTextFecha;
    private Button buttonGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deshabilitar);

        spinnerOpcion = findViewById(R.id.formopciones);
        spinnerHora = findViewById(R.id.formhoradh);
        editTextFecha = findViewById(R.id.formfechadh);
        buttonGuardar = findViewById(R.id.buttonGuardar);

        ArrayAdapter<String> opcionesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{
                "Deshabilitar día",
                "Deshabilitar hora",
                "Deshabilitar día y cancelar reservas"
        });
        opcionesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOpcion.setAdapter(opcionesAdapter);

        spinnerHora.setEnabled(false);
        ArrayAdapter<String> horasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{
                "21:00",
                "22:00",
                "23:00",
                "00:00"
        });
        horasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHora.setAdapter(horasAdapter);

        spinnerOpcion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinnerHora.setEnabled(position == 1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        String fecha = getIntent().getStringExtra("FECHA");
        editTextFecha.setText(fecha);
    }

    public void volver(View v) {
        Intent i = new Intent(this, CalendarioAdmin.class);
        startActivity(i);
    }

    public void guardar(View v) {
        String opcion = spinnerOpcion.getSelectedItem().toString();
        String hora = spinnerHora.isEnabled() ? spinnerHora.getSelectedItem().toString() : "";
        String fecha = editTextFecha.getText().toString();


        volver(null);
    }
}
