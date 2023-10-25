package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import android.util.Log;

public class Cancelar extends AppCompatActivity {
    private static final String TAG = "CancelarActivity";

    private Spinner spinner;
    private EditText editTextTextMultiLine;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar);

        db = FirebaseFirestore.getInstance();
        spinner = findViewById(R.id.spinner2);
        editTextTextMultiLine = findViewById(R.id.editTextTextMultiLine);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.cancel_reasons, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedReason = spinner.getSelectedItem().toString();
                if (selectedReason.equals("Otro")) {
                    editTextTextMultiLine.setEnabled(true);
                } else {
                    editTextTextMultiLine.setEnabled(false);
                    editTextTextMultiLine.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void cancelarReserva(View view) {
        // Obtén el número de documento ingresado
        String numeroDocumento = ((EditText) findViewById(R.id.fcuenta2)).getText().toString();
        Log.d(TAG, "Número de documento ingresado: " + numeroDocumento);

        Query reservaQuery = db.collection("reservas").whereEqualTo("numeroDocumento", numeroDocumento);
        reservaQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                Log.d(TAG, "Se encontró una reserva con el número de documento");

                // Obtén la reserva
                QuerySnapshot querySnapshot = task.getResult();
                String nombre = querySnapshot.getDocuments().get(0).getString("nombre");
                Log.d(TAG, "Nombre de la reserva: " + nombre);

                // Obtén el motivo seleccionado
                String selectedReason = spinner.getSelectedItem().toString();
                Log.d(TAG, "Motivo seleccionado: " + selectedReason);

                String motivoFinal;
                if (selectedReason.equals("Otro")) {
                    String otroMotivo = editTextTextMultiLine.getText().toString();
                    Log.d(TAG, "Motivo personalizado: " + otroMotivo);
                    motivoFinal = otroMotivo;
                } else {
                    motivoFinal = selectedReason;
                }

                db.collection("motivos")
                        .orderBy("idMotivo", Query.Direction.DESCENDING)
                        .limit(1)
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            int nextId = 1;
                            if (!queryDocumentSnapshots.isEmpty()) {
                                nextId = queryDocumentSnapshots.getDocuments().get(0).getLong("idMotivo").intValue() + 1;
                            }

                            Motivo motivo = new Motivo(nextId, nombre, motivoFinal);
                            db.collection("motivos")
                                    .add(motivo)
                                    .addOnSuccessListener(documentReference -> {

                                        querySnapshot.getDocuments().get(0).getReference().delete()
                                                .addOnSuccessListener(aVoid -> {
                                                    Log.d(TAG, "Documento eliminado de la colección 'reservas'");
                                                    Toast.makeText(this, "Cancelación realizada con éxito.", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(this, MainActivity.class);
                                                    startActivity(i);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Log.w(TAG, "Error al eliminar el documento de la colección 'reservas", e);
                                                });
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.w(TAG, "Error al agregar el motivo a la colección 'motivos", e);
                                    });
                        });
            } else {
                Toast.makeText(this, "No se encontró una reserva con ese número de documento", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void volver(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
