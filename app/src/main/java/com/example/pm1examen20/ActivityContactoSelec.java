package com.example.pm1examen20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pm1examen20.configuraciones.SQLiteConexion;
import com.example.pm1examen20.configuraciones.TransaccionesContactos;
import com.example.pm1examen20.tablas.Contactos;

public class ActivityContactoSelec extends AppCompatActivity {

    TextView codigo;
    EditText pais, nombre, telefono, nota, num, nom;
    Button btnActualiza;
    Button btnElimi;
    Button btnLLamar;
    Button btnRegres;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto_selec);

        codigo = (TextView) findViewById(R.id.ACCSlblId);
        pais = (EditText) findViewById(R.id.ACCStxtPais);
        nombre = (EditText) findViewById(R.id.ACCStxtNombre);
        telefono = (EditText) findViewById(R.id.ACCStxtTelefono);
        nota = (EditText) findViewById(R.id.ACCStxtNota);

        btnActualiza = (Button) findViewById(R.id.ACCSbtnRegresar);

        Bundle obj = getIntent().getExtras();

        Contactos conta = null;

        if (obj != null) {
            conta = (Contactos) obj.getSerializable("contacto");

            codigo.setText(conta.getId().toString());
            pais.setText(conta.getPaises().toString());
            nombre.setText(conta.getNombres().toString());
            telefono.setText(conta.getTelefono().toString());
            nota.setText(conta.getNota().toString());
        }

        btnActualiza = (Button) findViewById(R.id.ACCSbtnActualizar2);
        btnActualiza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModificarPersonas();
            }
        });

        btnElimi = (Button) findViewById(R.id.ACCSbtnEliminar);
        btnElimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EliminarContactos();
            }
        });

        btnRegres = (Button) findViewById(R.id.ACCSbtnRegresar);
        btnRegres.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityContactoSelec.this, ActivityContactosSalvados.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        btnLLamar = findViewById(R.id.ACCSbtnLlamar);
        num = findViewById(R.id.ACCStxtTelefono);
        nom = findViewById(R.id.ACCStxtNombre);

        btnLLamar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ActivityContactoSelec.this);
                builder.setMessage("¿Llamar a " + nom.getText().toString() + "?")
                        .setTitle("Acción");

                builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + num.getText().toString()));
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void ModificarPersonas() {
        SQLiteConexion conexion = new SQLiteConexion(this, TransaccionesContactos.NameDatabase,null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        String cod = codigo.getText().toString();

        ContentValues valores = new ContentValues();

        valores.put(TransaccionesContactos.pais, pais.getText().toString());
        valores.put(TransaccionesContactos.nombres, nombre.getText().toString());
        valores.put(TransaccionesContactos.telefono, telefono.getText().toString());
        valores.put(TransaccionesContactos.nota, nota.getText().toString());

        if (!codigo.getText().toString().isEmpty()){
            db.update("contactos", valores, "id=" + cod, null);
            Toast.makeText(this, "Se Actualizo el Registro: " +cod, Toast.LENGTH_LONG).show();
        }
    }

    private void EliminarContactos() {

        SQLiteConexion conexion = new SQLiteConexion(this, TransaccionesContactos.NameDatabase,null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        String cod = codigo.getText().toString();

        db.delete("contactos", "id=" + cod, null);
        Toast.makeText(this, "Regristo " + cod + " Eliminado Correctamente", Toast.LENGTH_LONG).show();

        db.close();
        LimpiarPantalla();

        Intent intent = new Intent(ActivityContactoSelec.this, ActivityContactosSalvados.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();

    }

    private void LimpiarPantalla() {
        codigo.setText("");
        pais.setText("");
        nombre.setText("");
        telefono.setText("");
        nota.setText("");
    }
}