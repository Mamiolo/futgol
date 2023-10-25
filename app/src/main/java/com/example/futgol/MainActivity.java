package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void cancelar(View v){
        Intent i = new Intent(this, Cancelar.class);

        startActivity(i);
    }
    public void vermas(View v){
        Intent i = new Intent(this, Vermas.class);

        startActivity(i);
    }

    public void calendario(View v){
        Intent i = new Intent(this, Calendario.class);

        startActivity(i);
    }
    public void login(View v){
        Intent i = new Intent(this, Login.class);

        startActivity(i);
    }

}