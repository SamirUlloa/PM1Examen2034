package com.example.pm1examen20.configuraciones;

public class TransaccionesContactos {
    //Nombre de la base de datos
    public static final String NameDatabase = "PM1EXAMEN1";

    //Creación de las Tabla Contactos de la base de datos
    public static final String tablacontactos = "contactos";

    //Campos especificos de la  tabla contacto
    public static final String id = "id";
    public static final String pais = "pais";
    public static final String nombres = "nombres";
    public static final String telefono = "telefono";
    public static final String nota = "nota";

    //Transacciones DDL (data definition lenguage)
    public static final String CreateTableContactos = "CREATE TABLE " + tablacontactos +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "pais TEXT, nombres TEXT, telefono INTEGER, nota TEXT)";

    public static final String DropTableContactos = "DROP TABLE IF EXISTS " + tablacontactos;
}
