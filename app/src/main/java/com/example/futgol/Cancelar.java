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

public class Cancelar extends AppCompatActivity {

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


            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    public void cancelarReserva(View view) {
        String numeroDocumento = ((EditText) findViewById(R.id.fcuenta2)).getText().toString();

        Query reservaQuery = db.collection("reservas").whereEqualTo("numeroDocumento", numeroDocumento);
        reservaQuery.get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !task.getResult().isEmpty()) {


                QuerySnapshot querySnapshot = task.getResult();
                String nombre = querySnapshot.getDocuments().get(0).getString("nombre");
                String selectedReason = spinner.getSelectedItem().toString();

                String motivoFinal;
                if (selectedReason.equals("Otro")) {
                    String otroMotivo = editTextTextMultiLine.getText().toString();
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
                                                    Toast.makeText(this, "Cancelación realizada con éxito.", Toast.LENGTH_SHORT).show();
                                                    Intent i = new Intent(this, MainActivity.class);
                                                    startActivity(i);
                                                });
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
