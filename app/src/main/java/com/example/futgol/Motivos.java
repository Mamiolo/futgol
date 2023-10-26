package com.example.futgol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Motivos extends AppCompatActivity {

    private ListView listViewMotivos;
    private List<Motivo> motivosList;
    private MotivoAdapter motivoAdapter;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motivos);

        listViewMotivos = findViewById(R.id.listViewMotivos);
        db = FirebaseFirestore.getInstance();

        motivosList = new ArrayList<>();
        motivoAdapter = new MotivoAdapter(this, R.layout.motivo_item, motivosList);

        listViewMotivos.setAdapter(motivoAdapter);

        cargarMotivosDesdeFirestore();
    }

    private void cargarMotivosDesdeFirestore() {
        db.collection("motivos")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot queryDocumentSnapshots = task.getResult();

                        for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Motivo motivo = dc.getDocument().toObject(Motivo.class);
                                    motivosList.add(motivo);
                                    motivoAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }
                    } else {
                        Toast.makeText(this, "Error al cargar motivos.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private class MotivoAdapter extends ArrayAdapter<Motivo> {
        private int resource;

        public MotivoAdapter(Context context, int resource, List<Motivo> objects) {
            super(context, resource, objects);
            this.resource = resource;
        }


        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(resource, parent, false);
            }

            final Motivo motivo = getItem(position);

            TextView textViewNombre = convertView.findViewById(R.id.textViewNombre);
            TextView textViewMotivo = convertView.findViewById(R.id.textViewMotivo);
            Button buttonEliminar = convertView.findViewById(R.id.buttonEliminar);

            textViewNombre.setText(motivo.getNombre());
            textViewMotivo.setText(motivo.getMotivo());

            buttonEliminar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String nombreMotivo = motivo.getNombre();

                    db.collection("motivos")
                            .whereEqualTo("nombre", nombreMotivo)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (DocumentSnapshot document : task.getResult()) {
                                        db.collection("motivos")
                                                .document(document.getId())
                                                .delete()
                                                .addOnSuccessListener(aVoid -> {
                                                    motivosList.remove(motivo);
                                                    motivoAdapter.notifyDataSetChanged();
                                                    Toast.makeText(Motivos.this, "Motivo eliminado.", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                } else {
                                    Toast.makeText(Motivos.this, "Error al buscar el motivo.", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });
            return convertView;
        }
    }

    public void volver(View v) {
        Intent i = new Intent(this, MenuAdmin.class);
        startActivity(i);
    }
}
