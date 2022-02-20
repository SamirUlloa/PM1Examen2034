package com.example.pm1examen20;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm1examen20.clases.AlertDialogo;
import com.example.pm1examen20.configuraciones.SQLiteConexion;
import com.example.pm1examen20.configuraciones.TransaccionesContactos;
import com.example.pm1examen20.configuraciones.TransaccionesPaises;
import com.example.pm1examen20.tablas.Contactos;
import com.example.pm1examen20.tablas.Paises;

import java.io.File;
import java.util.ArrayList;

public class ActivityGuardarContacto extends AppCompatActivity {

    EditText id, pais, nombres, telefono, nota;
    Button btnGuardarContactos;
    Button btnContactosSalvados;
    Button btnAgregarPais;

    ImageView ivFoto;
    Button btnTomarFoto, btnSeleccionarImagen;

    Uri imagenUri;

    int TOMAR_FOTO = 100;
    int SELEC_IMAGEN = 200;

    String CARPETA_RAIZ = "MisFotosApp";
    String CARPETAS_IMAGENES = "imagenes";
    String RUTA_IMAGEN = CARPETA_RAIZ + CARPETAS_IMAGENES;
    String path;

    //
    SQLiteConexion conexion;
    Spinner spPaises;

    ArrayList<String> lista_paises;
    ArrayList<Paises> lista;

    String spin; //Contenido del pais seleccionado en el Spinner.
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_contacto);

        //
        conexion = new SQLiteConexion(this, TransaccionesPaises.NameDatabase, null, 1);
        spPaises = (Spinner) findViewById(R.id.spPaises);
        //
        id = (EditText) findViewById(R.id.AGCtxtId);
        //pais = (Spinner) findViewById(R.id.spPaises);
        nombres = (EditText) findViewById(R.id.AGCtxtNombre);
        telefono = (EditText) findViewById(R.id.AGCtxtTelefono);
        nota = (EditText) findViewById(R.id.AGCtxtNota);

        //
        ObtenerListaPaises();

        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item, lista_paises);
        spPaises.setAdapter(adp);

        btnGuardarContactos = (Button) findViewById(R.id.btnGuardarContactos);
        btnGuardarContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!nombres.getText().toString().isEmpty() && !telefono.getText().toString().isEmpty()
                        && !nota.getText().toString().isEmpty())
                {
                    AgregarPaises();
                }
                else{
                    Mensaje();
                }
            }
        });

        //Muestra los contactos salvados
        btnContactosSalvados = (Button) findViewById(R.id.btnContactosSalvados);
        btnContactosSalvados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityGuardarContacto.this, ActivityContactosSalvados.class);
                startActivity(intent);
            }
        });

        btnAgregarPais = (Button) findViewById(R.id.btnAgregarPais);
        btnAgregarPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityGuardarContacto.this, ActivityGuardarPaises.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void ObtenerListaPaises() {
        Paises pai = null;
        lista = new ArrayList<Paises>();

        SQLiteDatabase db = conexion.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TransaccionesPaises.tablapaises, null);

        while (cursor.moveToNext()){
            pai = new Paises();
            pai.setIdpais(cursor.getInt(0));
            pai.setNombrepais(cursor.getString(1));
            pai.setCodigopais(cursor.getString(2));

            lista.add(pai);
        }
        cursor.close();
        fillcombo();
    }

    //Rellena el Spinner
    private void fillcombo() {
        lista_paises = new ArrayList<String>();
        for(int i=0; i < lista.size(); i++){
            lista_paises.add(lista.get(i).getNombrepais()+ " (" +
                    lista.get(i).getCodigopais()+ ")");
        }
    }

    //
    private void Mensaje() {
        AlertDialogo alerta = new AlertDialogo();
        alerta.show(getSupportFragmentManager(),"Mensaje");
    }

    private void AgregarPaises() {
        SQLiteConexion conexion = new SQLiteConexion(this, TransaccionesPaises.NameDatabase,null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        spin = spPaises.getSelectedItem().toString();

        ContentValues valores = new ContentValues();
        //valores.put(TransaccionesContactos.pais, spin.getText().toString());
        valores.put(TransaccionesContactos.pais, spin.toString());
        valores.put(TransaccionesContactos.nombres, nombres.getText().toString());
        valores.put(TransaccionesContactos.telefono, telefono.getText().toString());
        valores.put(TransaccionesContactos.nota, nota.getText().toString());

        Long resultado = db.insert(TransaccionesContactos.tablacontactos, TransaccionesContactos.id, valores);

        Toast.makeText(getApplicationContext(),
                "Registro ingresado con exito!! Codigo " + resultado.toString(),
                Toast.LENGTH_LONG).show();

        db.close();

        LimpiarPantalla();
    }

    private void LimpiarPantalla() {
        id.setText("");
        nombres.setText("");
        telefono.setText("");
        nota.setText("");
    }
}