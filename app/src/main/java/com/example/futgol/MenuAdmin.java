package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MenuAdmin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);
    }

    public void volver(View v){
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }

    public void motivos(View v){
        Intent i = new Intent(this, Motivos.class);
        startActivity(i);
    }

    public void horarios(View v){
        Intent i = new Intent(this, CalendarioAdmin.class);
        startActivity(i);
    }

    public void canchas(View v){
        Intent i = new Intent(this, Canchas.class);
        startActivity(i);
    }
}