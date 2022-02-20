package com.example.pm1examen20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm1examen20.clases.AlertDialogo;
import com.example.pm1examen20.configuraciones.SQLiteConexion;
import com.example.pm1examen20.configuraciones.TransaccionesPaises;

public class ActivityGuardarPaises extends AppCompatActivity {

    EditText idPais, nombrePais, codigoPais;
    Button btnGuardarPaises;
    Button AGPbtnRegresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_paises);

        idPais = (EditText) findViewById(R.id.AGPtxtId);
        nombrePais = (EditText) findViewById(R.id.AGPtxtPais);
        codigoPais = (EditText) findViewById(R.id.AGPtxtCodigo);

        btnGuardarPaises = (Button) findViewById(R.id.btnGuardarPaises);
        btnGuardarPaises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!nombrePais.getText().toString().isEmpty() && !codigoPais.getText().toString().isEmpty())
                {
                    AgregarPaises();
                }
                else{
                    Mensaje();
                }
            }
        });

        AGPbtnRegresar = (Button) findViewById(R.id.AGPbtnRegresar);
        AGPbtnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityGuardarPaises.this, ActivityGuardarContacto.class);
                startActivity(intent);
            }
        });
    }

    private void Mensaje() {
        AlertDialogo alerta = new AlertDialogo();
        alerta.show(getSupportFragmentManager(),"Mensaje");
    }

    private void AgregarPaises() {
        SQLiteConexion conexion = new SQLiteConexion(this, TransaccionesPaises.NameDatabase,null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(TransaccionesPaises.nombrepais, nombrePais.getText().toString());
        valores.put(TransaccionesPaises.codigopais, codigoPais.getText().toString());

        Long resultado = db.insert(TransaccionesPaises.tablapaises, TransaccionesPaises.idpais, valores);

        Toast.makeText(getApplicationContext(),
                "Registro ingresado con exito!! Codigo " + resultado.toString(),
                Toast.LENGTH_LONG).show();

        db.close();

        LimpiarPantalla();
    }

    private void LimpiarPantalla() {
        idPais.setText("");
        nombrePais.setText("");
        codigoPais.setText("");
    }
}