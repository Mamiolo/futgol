package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;
import java.util.ArrayList;

public class Reservar extends AppCompatActivity {

    EditText editTextFecha;
    EditText editTextHora;
    EditText editTextNombre;
    EditText editTextTelefono;
    EditText editTextDocumento;
    Spinner spinnerCancha;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        editTextFecha = findViewById(R.id.editTextFecha);
        editTextHora = findViewById(R.id.editTextHora);
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextTelefono = findViewById(R.id.editTextTelefono);
        editTextDocumento = findViewById(R.id.editTextDocumento);
        spinnerCancha = findViewById(R.id.spinnerCancha);

        db = FirebaseFirestore.getInstance();

        String fechaStr = getIntent().getStringExtra("FECHA");
        String hora = getIntent().getStringExtra("HORA");

        editTextFecha.setText(fechaStr);
        editTextHora.setText(hora);

        cargarNumerosCanchasDisponibles();
    }

    private void cargarNumerosCanchasDisponibles() {
        db.collection("canchas")
                .whereEqualTo("estado", "disponible")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<String> numerosCanchas = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Cancha cancha = document.toObject(Cancha.class);
                        if (cancha != null) {
                            numerosCanchas.add(String.valueOf(cancha.getNumeroCancha()));
                        }
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numerosCanchas);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCancha.setAdapter(adapter);
                })
                .addOnFailureListener(e -> {
                    Log.e("Reservar", "Error al cargar las canchas disponibles: " + e.getMessage());
                    Toast.makeText(this, "Error al cargar las canchas disponibles.", Toast.LENGTH_SHORT).show();
                });
    }

    public void volver(View v) {
        Intent i = new Intent(this, Calendario.class);
        startActivity(i);
    }

    public void reservar(View v) {
        String fechaStr = editTextFecha.getText().toString();
        String hora = editTextHora.getText().toString();
        String nombre = editTextNombre.getText().toString();
        String telefono = editTextTelefono.getText().toString();
        String documento = editTextDocumento.getText().toString();

        if (fechaStr.isEmpty() || hora.isEmpty() || nombre.isEmpty() || telefono.isEmpty() || documento.isEmpty()) {
            Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
        } else {
            String numeroCanchaSeleccionada = spinnerCancha.getSelectedItem().toString();

            Cancha cancha = new Cancha(Integer.parseInt(numeroCanchaSeleccionada), "Disponible");

            long idTiquet = System.currentTimeMillis();

            TiquetReserva tiquetReserva = new TiquetReserva(idTiquet, fechaStr, hora, nombre, telefono, documento, cancha);

            db.collection("reservas").document(String.valueOf(idTiquet)).set(tiquetReserva)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Reserva realizada con éxito.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Reservar", "Error al guardar la reserva: " + e.getMessage());
                        Toast.makeText(this, "Error al realizar la reserva.", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
