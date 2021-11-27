package cne.heba.sie.offline;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.MarkerCanva.Lienzo;
import cne.heba.sie.R;
import cne.heba.sie.abeh.DbHelper;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;



public class firmaInitoff extends AppCompatActivity {
    private Context TheThis;

    String subcarp;
    Button saver,lim, btnMostrar;
    TextView titulo5, subtit5;
    String TipoCaptura;


    private static Lienzo lienzo;
    SweetAlertDialog pDialog;
    Cypher cyf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_init_off);

        lienzo = (Lienzo) findViewById(R.id.lienzo);
        saver = findViewById(R.id.sav);
        btnMostrar = findViewById(R.id.limpis);
        //lim = findViewById(R.id.limpis);
        titulo5 = findViewById(R.id.titlePriva);
        subtit5 = findViewById(R.id.subtit6);

        cyf = new Cypher();
        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }



        saver.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                darAlta();
                //Toast.makeText(firmaInitoff.this, "Datos resgistrados con éxito", Toast.LENGTH_LONG).show();

            }
        });

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(firmaInitoff.this, "Datos Mostrados", Toast.LENGTH_LONG).show();
            }
        });

    }


    private void LipiezaVar() {

        Constantes.CURP = null;
        Constantes.CLAVEL = null;
        Constantes.NOMBRE = null;
        Constantes.AP = null;
        Constantes.AM = null;
        Constantes.NOMBREVIAL = null;
        Constantes.NI = null;
        Constantes.NE = null;
        Constantes.MZ = null;
        Constantes.LTE = null;
        Constantes.CP = null;
        Constantes.OCR = null;
        Constantes.CIC = null;
        Constantes.SECCION = null;
        Constantes.ID_SECCION = null;
        Constantes.EMIS = null;
        Constantes.VIGENCIA = null;
        Constantes.FACE = null;
        Constantes.TWTT = null;
        Constantes.TEL = null;
        Constantes.PFACE = null;
        Constantes.PTWIT = null;
        Constantes.STATUS = null;

    }

   // @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void darAlta() {

        encriptar();

        String fecha = null;
        String hora = null;

        try {
            fecha = cyf.encrypt(obtenerFechaConFormato("yyyy-MM-dd"));
            hora = cyf.encrypt(obtenerFechaConFormato("HH:mm:ss"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        DbHelper dbHelper = new DbHelper(this);
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();


        ContentValues values = new ContentValues();
        values.put("CURP", Constantes.CURP);
        values.put("ClaveElector", Constantes.CLAVEL);
        values.put("Nombre", Constantes.NOMBRE);
        values.put("Apaterno", Constantes.AP);
        values.put("Amaterno", Constantes.AM);
        values.put("Calle", Constantes.NOMBREVIAL);
        values.put("NumInt", Constantes.NI);
        values.put("NumExt", Constantes.NE);
        values.put("Manzana", Constantes.MZ);
        values.put("nLote", Constantes.LTE);
        values.put("cPostal", Constantes.CP);
        values.put("OCR", Constantes.OCR);
        values.put("CIC", Constantes.CIC);
        values.put("SeccionElectoral", Constantes.SECCION);
        values.put("NumEmision", Constantes.EMIS);
        values.put("aVige", Constantes.VIGENCIA);
        values.put("Facebook", Constantes.FACE);
        values.put("Twitter", Constantes.TWTT);
        values.put("NumeroTelefono", Constantes.TEL);
        values.put("PartiF", Constantes.PFACE);
        values.put("PartiT", Constantes.PTWIT);
        values.put("Status", Constantes.STATUS);
        values.put("Date", fecha);
        values.put("Time", hora);

        //Log.e("Nombre",Constantes.NOMBRE);

        Long resultado = sqLiteDatabase.insert("t_registros", null, values);
        if(Integer.parseInt(String.valueOf(resultado))>0){
            Toast.makeText(getApplicationContext(),"Se Registro",Toast.LENGTH_SHORT).show();
            LipiezaVar();
        }else {
            Toast.makeText(TheThis, "No se Registro", Toast.LENGTH_SHORT).show();
        }

        sqLiteDatabase.close();
        dbHelper.close();
        finish();


    }

    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaConFormato(String formato) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
        return sdf.format(date);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void encriptar() {



        try {

            if (Objects.equals(null,Constantes.CURP)){
                Constantes.CURP = cyf.encrypt("0");
            }else {
                Constantes.CURP = cyf.encrypt(Constantes.CURP);
            }
            if (Objects.equals(null,Constantes.CLAVEL)) {
                Constantes.CLAVEL = cyf.encrypt("0");
            }else {
                Constantes.CLAVEL = cyf.encrypt(Constantes.CLAVEL);
            }
            if (Objects.equals(null,Constantes.NOMBRE)) {
                Constantes.NOMBRE = cyf.encrypt("0");
            }else {
                Constantes.NOMBRE = cyf.encrypt(Constantes.NOMBRE);
            }
            if (Objects.equals(null,Constantes.AP)) {
                Constantes.AP = cyf.encrypt("0");
                //Constantes.AP = "0";
            }else {
                Constantes.AP = cyf.encrypt(Constantes.AP);
            }
            if (Objects.equals(null,Constantes.AM)) {
                Constantes.AM = cyf.encrypt("0");
            }else {
                Constantes.AM = cyf.encrypt(Constantes.AM);
            }
            if (Objects.equals(null,Constantes.NOMBREVIAL)) {
                Constantes.NOMBREVIAL = cyf.encrypt("0");
            }else {
                Constantes.NOMBREVIAL = cyf.encrypt(Constantes.NOMBREVIAL);
            }
            if (Objects.equals(null,Constantes.NI)) {
                Constantes.NI = cyf.encrypt("0");
            }else {
                Constantes.NI = cyf.encrypt(Constantes.NI);
            }
            if (Objects.equals(null,Constantes.NE)) {
                Constantes.NE = cyf.encrypt("0");
            }else {
                Constantes.NE = cyf.encrypt(Constantes.NE);
            }
            if (Objects.equals(null,Constantes.MZ)) {
                Constantes.MZ = cyf.encrypt("0");
            }else {
                Constantes.MZ = cyf.encrypt(Constantes.MZ);
            }
            if (Objects.equals(null,Constantes.LTE)) {
                Constantes.LTE = cyf.encrypt("0");
            }else {
                Constantes.LTE = cyf.encrypt(Constantes.LTE);
            }
            if (Objects.equals(null,Constantes.CP)) {
                Constantes.CP = cyf.encrypt("0");
            }else {
                Constantes.CP = cyf.encrypt(Constantes.CP);
            }
            if (Objects.equals(null,Constantes.OCR)) {
                Constantes.OCR = cyf.encrypt("0");
            }else {
                Constantes.OCR = cyf.encrypt(Constantes.OCR);
            }
            if (Objects.equals(null,Constantes.CIC)) {
                Constantes.CIC = cyf.encrypt("0");
            }else {
                Constantes.CIC = cyf.encrypt(Constantes.CIC);
            }
            if (Objects.equals(null,Constantes.SECCION)) {
                Constantes.SECCION = cyf.encrypt("0");
            }else {
                Constantes.SECCION = cyf.encrypt(Constantes.SECCION);
            }
            if (Objects.equals(null,Constantes.EMIS)) {
                Constantes.EMIS = cyf.encrypt("0");
            }else {
                Constantes.EMIS = cyf.encrypt(Constantes.EMIS);
            }
            if (Objects.equals(null,Constantes.VIGENCIA)) {
                Constantes.VIGENCIA = cyf.encrypt("0");
            }else {
                Constantes.VIGENCIA = cyf.encrypt(Constantes.VIGENCIA);
            }
            if (Objects.equals(null,Constantes.FACE)) {
                Constantes.FACE = cyf.encrypt("0");
            }else {
                Constantes.FACE = cyf.encrypt(Constantes.FACE);
            }
            if (Objects.equals(null,Constantes.TWTT)) {
                Constantes.TWTT = cyf.encrypt("0");
            }else {
                Constantes.TWTT = cyf.encrypt(Constantes.TWTT);
            }
            if (Objects.equals(null,Constantes.TEL)) {
                Constantes.TEL = cyf.encrypt("0");
            }else {
                Constantes.TEL = cyf.encrypt(Constantes.TEL);
            }
            if (Objects.equals(null,Constantes.PFACE)) {
                Constantes.PFACE = cyf.encrypt("0");
            }else {
                Constantes.PFACE = cyf.encrypt(Constantes.PFACE);
            }
            if (Objects.equals(null,Constantes.PTWIT)) {
                Constantes.PTWIT = cyf.encrypt("0");
            }else {
                Constantes.PTWIT = cyf.encrypt(Constantes.PTWIT);
            }

            Constantes.STATUS = cyf.encrypt(Constantes.STATUS);

        } catch (Exception e) {

            e.printStackTrace();


        }

    }

}
