package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        String fecha = editTextFecha.getText().toString();
        String hora = editTextHora.getText().toString();

        db.collection("canchas")
                .whereEqualTo("estado", "disponible")
                .get()
                .addOnSuccessListener(queryDocumentSnapshotsCanchas -> {
                    List<String> numerosCanchasDisponibles = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshotsCanchas) {
                        Cancha cancha = document.toObject(Cancha.class);
                        if (cancha != null) {
                            numerosCanchasDisponibles.add(String.valueOf(cancha.getNumeroCancha()));
                        }
                    }

                    db.collection("ocupaciones")
                            .whereEqualTo("fecha", fecha)
                            .whereEqualTo("hora", hora)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshotsOcupaciones -> {
                                for (QueryDocumentSnapshot document : queryDocumentSnapshotsOcupaciones) {
                                    for (int i = 1; i <= 5; i++) {
                                        String campoKey = "c" + i;
                                        String estado = document.getString(campoKey);
                                        if (estado != null && estado.equals("nodisponible")) {
                                            numerosCanchasDisponibles.remove(String.valueOf(i));
                                        }
                                    }
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, numerosCanchasDisponibles);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinnerCancha.setAdapter(adapter);
                            });
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
                        actualizarOcupacion(fechaStr, hora, numeroCanchaSeleccionada);
                        Toast.makeText(this, "Reserva realizada con éxito.", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(this, MainActivity.class);
                        startActivity(i);
                    });
        }
    }

    private void actualizarOcupacion(String fecha, String hora, String numeroCancha) {
        String campoCancha = "c" + numeroCancha;
        db.collection("ocupaciones")
                .whereEqualTo("fecha", fecha)
                .whereEqualTo("hora", hora)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            documentSnapshot.getReference().update(campoCancha, "nodisponible");
                        }
                    } else {
                        Ocupado ocupado = new Ocupado(fecha, hora, "disponible", "disponible", "disponible", "disponible", "disponible");
                        switch (numeroCancha) {
                            case "1":
                                ocupado.setC1("nodisponible");
                                break;
                            case "2":
                                ocupado.setC2("nodisponible");
                                break;
                            case "3":
                                ocupado.setC3("nodisponible");
                                break;
                            case "4":
                                ocupado.setC4("nodisponible");
                                break;
                            case "5":
                                ocupado.setC5("nodisponible");
                                break;
                        }

                        db.collection("ocupaciones").add(ocupado);
                    }
                });
    }
}
