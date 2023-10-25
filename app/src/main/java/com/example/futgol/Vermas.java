package com.example.futgol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class Vermas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vermas);

        checkFirestoreConnection();
    }

    private void checkFirestoreConnection() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("canchas")
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Vermas.this, "Conexión exitosa a Firestore", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Vermas.this, "No se pudo conectar a Firestore", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void volver(View v) {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}
