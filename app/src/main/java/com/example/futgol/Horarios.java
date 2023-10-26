package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class Horarios extends AppCompatActivity {

    TextView textViewFecha;
    private List<Integer> canchasDeshabilitadas = new ArrayList<>();

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


        verificarCanchasOcupadas(fecha, "21:00", btnHora21);
        verificarCanchasOcupadas(fecha, "22:00", btnHora22);
        verificarCanchasOcupadas(fecha, "23:00", btnHora23);
        verificarCanchasOcupadas(fecha, "00:00", btnHora00);

        btnHora21.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                iniciarReserva(fecha, "21:00");
            }
        });

        btnHora22.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                iniciarReserva(fecha, "22:00");
            }
        });

        btnHora23.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                iniciarReserva(fecha, "23:00");
            }
        });

        btnHora00.setOnClickListener(new View.OnClickListener() {

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

    private void verificarCanchasOcupadas(String fecha, String hora, Button button) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("ocupaciones")
                .whereEqualTo("fecha", fecha)
                .whereEqualTo("hora", hora)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean todasOcupadas = true;

                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        boolean algunaCanchaDisponible = false;

                        for (int i = 1; i <= 5; i++) {
                            String campoKey = "c" + i;
                            String estado = documentSnapshot.getString(campoKey);

                            if (estado != null && estado.equals("nodisponible")) {
                                if (canchasDeshabilitadas.contains(i)) {
                                    estado = "nodisponible";
                                }
                            }

                            if (estado != null && estado.equals("nodisponible")) {
                            } else {
                                algunaCanchaDisponible = true;
                                break;
                            }
                        }

                        if (!algunaCanchaDisponible) {
                            button.setBackgroundColor(getResources().getColor(R.color.ocupado));
                            button.setText("Ocupado");
                            button.setOnClickListener(null);
                            break;
                        }
                    }
                });

        db.collection("ocupaciones")
                .whereEqualTo("fecha", fecha)
                .whereEqualTo("hora", "todas")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    boolean todasOcupadas = true;
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()) {
                        boolean algunaCanchaDisponible = false;
                        button.setBackgroundColor(getResources().getColor(R.color.ocupado));
                        button.setText("Ocupado");
                        button.setOnClickListener(null);
                        Toast.makeText(Horarios.this, "No hay canchas disponibles", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void canchasDeshabilitada(int numeroCancha) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String canchaDocumentId = "cancha_" + numeroCancha;

        DocumentReference canchaRef = db.collection("canchas").document(canchaDocumentId);

        canchaRef.get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String estado = documentSnapshot.getString("estado");
                        if (estado != null && estado.equals("deshabilitado")) {
                            canchasDeshabilitadas.add(numeroCancha);
                        }
                    } else {
                    }
                });
    }

    public void volver(View v){
        Intent i = new Intent(this, Calendario.class);
        startActivity(i);
    }
}
