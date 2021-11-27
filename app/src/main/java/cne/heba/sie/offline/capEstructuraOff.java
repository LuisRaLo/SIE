package cne.heba.sie.offline;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import cne.heba.sie.R;
import cne.heba.sie.abeh.DbHelper;
import cne.heba.sie.adaptadores.ListElement;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class capEstructuraOff extends Fragment {

    View vistazo;

    DbHelper conn;

    String[] secos;

    Cypher cyf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vistazo = inflater.inflate(R.layout.fragment_cap_estructura, container, false);

        cyf = new Cypher();

        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Spinner spinSecc = vistazo.findViewById(R.id.seccioSpin);
        conn = new DbHelper(getContext());

        SQLiteDatabase db = conn.getReadableDatabase();

        Cursor u = db.rawQuery("SELECT id_secserver,seccion FROM t_secciones",null);
        secos = new String[u.getCount()];

        int a = 0;
        if(u.moveToFirst()){

            do {
                try {
                    secos[a]=cyf.decrypt(u.getString(u.getColumnIndex("id_secserver")))+"- "+cyf.decrypt(u.getString(u.getColumnIndex("seccion")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                a=a+1;
            } while (u.moveToNext());

        }else {

        }

        spinSecc.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, secos));


        spinSecc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = spinSecc.getSelectedItem().toString().split("-");
                    Constantes.ID_SECCION = cyf.encrypt(cricko[0]);
                   // Toast.makeText(getContext(), Constantes.ID_SECCION, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   Toast.makeText(getApplicationContext(),"El Elemento seleccionado es posición número:" +position + " El String es: " +secc.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        return vistazo;
    }
}