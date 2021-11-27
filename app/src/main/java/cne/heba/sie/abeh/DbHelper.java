package cne.heba.sie.abeh;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    //VERSION DE LA BASE DATOS
    private static final int DATABASE_VERSION = 11; //version para Offline
    //NOMBRE DE LA BASE DE DATOS
    private static final String DATABASE_NOMBRE = "tripas.db";
    //TABLAS DE USO
    private static final String TABLE_USERS = "t_usuarios";
    private static final String TABLE_REGISTROS = "t_registros";
    private static final String TABLE_SECCIONES = "t_secciones";

    private static final String CREATE_USERS = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS + "(" +
            "id_user INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL," +
            "email TEXT," +
            "pass TEXT NOT NULL," +
            "tmp_pass TEXT," +
            "beta TEXT," +
            "nivelU TEXT," +
            "token TEXT)"; //update de database

    private static final String CREATE_REGISTROS = "CREATE TABLE IF NOT EXISTS " + TABLE_REGISTROS + "(" +
            "id_registro INTEGER PRIMARY KEY AUTOINCREMENT," +
            "CURP TEXT NOT NULL," +
            "ClaveElector TEXT," +
            "Nombre TEXT NOT NULL," +
            "Apaterno TEXT," +
            "Amaterno TEXT," +
            "Calle TEXT," +
            "NumInt TEXT," +
            "NumExt TEXT," +
            "Manzana TEXT," +
            "nLote TEXT," +
            "cPostal TEXT," +
            "OCR TEXT," +
            "CIC TEXT," +
            "SeccionElectoral TEXT," +
            "NumEmision TEXT," +
            "aVige TEXT," +
            "Facebook TEXT," +
            "Twitter TEXT," +
            "NumeroTelefono TEXT," +
            "PartiF TEXT," +
            "PartiT TEXT," +
            "Status TEXT," +
            "Date TEXT," +
            "Time TEXT)";

    private static final String CREATE_SECCIONES = "CREATE TABLE IF NOT EXISTS " + TABLE_SECCIONES + "(" +
            "id_seccion INTEGER PRIMARY KEY AUTOINCREMENT," +
            "id_secserver TEXT NOT NULL," +
            "seccion TEXT NOT NULL)";

    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_REGISTROS);
        db.execSQL(CREATE_SECCIONES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REGISTROS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SECCIONES);
        onCreate(db);
    }
}
