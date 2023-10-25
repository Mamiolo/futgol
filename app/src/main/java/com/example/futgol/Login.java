package com.example.futgol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void volver(View v){
        Intent i = new Intent(this, MainActivity.class);

        startActivity(i);
    }
    public void login(View v){

        EditText campo1 = this.findViewById(R.id.fcuenta);
        String cuenta = campo1.getText().toString();
        EditText campo2 = this.findViewById(R.id.fcontra);
        String contra = campo2.getText().toString();

        if(cuenta.equals("Axel") && contra.equals("1234")) {
            Intent i = new Intent(this, MenuAdmin.class);
            startActivity(i);
        }else {
            Toast.makeText(this, "Crendeciales Incorrectas", Toast.LENGTH_SHORT).show();
        }
    }
}