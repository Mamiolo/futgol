package com.example.futgol;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class Canchas extends AppCompatActivity {

    private Button btnAgregarCancha;
    private LinearLayout canchasLayout;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private int canchasAgregadas = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canchas);

        btnAgregarCancha = findViewById(R.id.btnAgregarCancha);
        canchasLayout = findViewById(R.id.canchasLayout);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        btnAgregarCancha.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (canchasAgregadas < 5) {
                    agregarNuevaCancha();
                }
            }
        });

        mostrarCanchas();
    }

    private void agregarNuevaCancha() {
        if (canchasAgregadas < 5) {
            generarNumeroCanchaUnico(new OnUniqueNumberGeneratedListener() {
                public void onUniqueNumberGenerated(int numeroCancha) {
                    if (numeroCancha != -1) {
                        Cancha nuevaCancha = new Cancha(numeroCancha, "disponible");

                        db.collection("canchas")
                                .add(nuevaCancha);
                    } else {
                    }
                }
            });
        } else {
            Toast.makeText(this, "No se pueden agregar más canchas. Ya existen 5 canchas.", Toast.LENGTH_SHORT).show();
        }
    }




    private void generarNumeroCanchaUnico(final OnUniqueNumberGeneratedListener listener) {
        db.collection("canchas")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();
                        List<Integer> numerosCancha = new ArrayList<>();

                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Cancha cancha = document.toObject(Cancha.class);
                            if (cancha != null) {
                                numerosCancha.add(cancha.getNumeroCancha());
                            }
                        }

                        for (int i = 1; i <= 5; i++) {
                            if (!numerosCancha.contains(i)) {
                                listener.onUniqueNumberGenerated(i);
                                return;
                            }
                        }

                        listener.onUniqueNumberGenerated(-1);
                    } else {
                    }
                });
    }

    interface OnUniqueNumberGeneratedListener {
        void onUniqueNumberGenerated(int numeroCancha);
    }



    private void mostrarCanchas() {
        db.collection("canchas")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (e != null) {
                        return;
                    }

                    for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                        switch (dc.getType()) {
                            case ADDED:
                                Cancha cancha = dc.getDocument().toObject(Cancha.class);
                                if (cancha != null) {
                                    agregarCanchaVista(cancha, dc.getDocument().getId());
                                }
                                break;
                        }
                    }
                });
    }

    private void agregarCanchaVista(Cancha cancha, final String documentId) {
        View canchaView = getLayoutInflater().inflate(R.layout.cancha, null);
        canchasLayout.addView(canchaView);

        TextView textCancha = canchaView.findViewById(R.id.textCancha);
        textCancha.setText("Cancha " + cancha.getNumeroCancha());

        Switch switchCancha = canchaView.findViewById(R.id.switchCancha);

        switchCancha.setOnCheckedChangeListener((buttonView, isChecked) -> {
            String nuevoEstado = isChecked ? "deshabilitada" : "disponible";
            actualizarEstadoCancha(documentId, nuevoEstado);
        });

        Button btnEliminar = canchaView.findViewById(R.id.btnEliminar);
        btnEliminar.setOnClickListener(view -> {
            db.collection("canchas")
                    .document(documentId)
                    .delete();
            canchasLayout.removeView(canchaView);
        });
    }

    private void actualizarEstadoCancha(String documentId, String nuevoEstado) {
        db.collection("canchas")
                .document(documentId)
                .update("estado", nuevoEstado);
    }


    public void volver(View v) {
        Intent i = new Intent(this, MenuAdmin.class);
        startActivity(i);
    }
}
