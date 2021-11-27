package cne.heba.sie.menu.Captura.Step;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import java.util.Objects;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;
import cne.heba.sie.util.NukeSSLCerts;


public class Cap4 extends Fragment {

   View vistaVI;
   Button finalc;
   EditText face, twiiter, telefono;
   CheckBox f,t;
   Cypher Cyf;
   String cF,cT;
   TextView titulo4,subtit5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vistaVI = inflater.inflate(R.layout.fragment_cap4, container, false);
        // Elementos del EdditTex
        face = vistaVI.findViewById(R.id.facebook);
        twiiter = vistaVI.findViewById(R.id.Twit);
        telefono = vistaVI.findViewById(R.id.celular);
        f = vistaVI.findViewById(R.id.checkF);
        t = vistaVI.findViewById(R.id.checkT);
        titulo4= vistaVI.findViewById(R.id.TitulCap4);
        subtit5= vistaVI.findViewById(R.id.subtit);
        new NukeSSLCerts().nuke();


        Cyf = new Cypher();

        try {
            Cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }



        finalc = vistaVI.findViewById(R.id.finalcap);

        f.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(face.getText().toString() == null || face.getText().toString().equals("") || face.getText().toString().equals(" ")){

                        face.setError("Llene Facebook");
                        face.requestFocus();
                        f.setChecked(false);

                    }else{

                    }
                }
            }
        });

        t.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(twiiter.getText().toString() == null || twiiter.getText().toString().equals("") || twiiter.getText().toString().equals(" ")){

                        twiiter.setError("Llene Twitter");
                        twiiter.requestFocus();
                        t.setChecked(false);

                    }else{

                    }
                }
            }
        });

        finalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pF = face.getText().toString();
                String pT = twiiter.getText().toString();
                String pTel = telefono.getText().toString();

                if(face.getText().toString().equals("") || face.getText().toString().equals(" ") || face.getText().toString()==null){
                    pF = "0";
                }
                if(twiiter.getText().toString().equals("") || twiiter.getText().toString().equals(" ") || twiiter.getText().toString()==null){
                    pF = "0";
                }

                if(pTel.equals("") || pTel.equals(" ") || pTel == null || pTel.length()<10){
                    telefono.setError("Ingrese Correctamente el Número Telefonico");
                    telefono.requestFocus();
                }else {
                    telefono.setEnabled(false);
                    Constantes.CONTAC = 1;
                }

                if(f.isChecked()){
                    cF = "1";
                }else {
                    cF = "0";
                }

                if(t.isChecked()){
                    cT = "1";
                }else {
                    cT = "0";
                }

                if(encripta(pF,pT,pTel,cF,cT)){

                    if(Constantes.INCUR == 0){
                        alertaData("VERIFIQUE CURP O CLAVE INE");
                    }else if(Constantes.NOAPMA == 0){
                        alertaData("VERIFIQUE NOMBRE");
                    }else if(Constantes.ELECTOR == 0){
                        alertaData("VERIFIQUE DATOS DE CREDENCIAL");
                    }else if(Constantes.CONTAC == 0){
                        alertaData("VERIFIQUE DATOS DE CONTACTO");
                    }else {
                        Constantes.INCUR = 0;
                        Constantes.NOAPMA = 0;
                        Constantes.ELECTOR = 0;
                        Constantes.CONTAC = 0;
                        pasarDeFragment();
                    }


                }


            }
        });



        return vistaVI;
    }

    private void alertaData(String alerto) {

        AlertDialog.Builder alrtas = new AlertDialog.Builder(getContext());
        alrtas.setTitle("DATOS INCOMPLETOS");
        alrtas.setMessage(alerto);
        alrtas.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertota = alrtas.create();
        alertota.show();

    }

    private void pasarDeFragment() {
            Intent firma = new Intent(getActivity(), firmaInit.class);
            startActivity(firma);
            getActivity().finish();
    }

    private boolean encripta(String Pface, String Ptuit, String Ptel, String PredF, String PredT) {

        try {
            if(Pface != null)
                Constantes.FACE = Cyf.encrypt(Pface);
            if(Ptuit != null)
                Constantes.TWTT = Cyf.encrypt(Ptuit);
            if(Ptel != null)
                Constantes.TEL = Cyf.encrypt(Ptel);
            if(PredF != null)
                Constantes.PFACE = Cyf.encrypt(PredF);
            if(PredT != null)
                Constantes.PTWIT = Cyf.encrypt(PredT);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {

            if (Constantes.CONTAC == 1) {

            } else {

                if (Constantes.eventoSalida == 2) {
                    if (Objects.equals(Constantes.FACE, null)) {

                    } else {
                        face.setText(Constantes.FACE);
                    }
                    if (Objects.equals(Constantes.TWTT, null)) {

                    } else {
                        twiiter.setText(Constantes.TWTT);
                    }
                    if (Objects.equals(Constantes.TEL, null)) {

                    } else {
                        telefono.setText(Constantes.TEL);
                    }

                }

                if (Constantes.eventoSalida == 1) {



                }

            }
        }


    }
}