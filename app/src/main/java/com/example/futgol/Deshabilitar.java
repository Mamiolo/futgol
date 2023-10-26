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
import com.google.firebase.firestore.FirebaseFirestore;


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

            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                spinnerHora.setEnabled(position == 1);
            }

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
        String hora = "";
        String fecha = editTextFecha.getText().toString();

        if (opcion.equals("Deshabilitar hora")) {
            hora = spinnerHora.getSelectedItem().toString();
        } else if (opcion.equals("Deshabilitar día")) {
            hora = "todas";
        }

        Ocupado ocupado = new Ocupado(fecha, hora, "nodisponible", "nodisponible", "nodisponible", "nodisponible", "nodisponible");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ocupaciones")
                .add(ocupado);
    }
}
