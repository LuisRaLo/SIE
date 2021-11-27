package cne.heba.sie.menu.Captura.Step;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;

import java.util.Objects;

import cne.heba.sie.R;
import cne.heba.sie.menu.Captura.Ocr.ocrUp;
import cne.heba.sie.util.Constantes;


public class mainCap extends Fragment {

    View mainCap;
    Button startT, startOCR;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainCap = inflater.inflate(R.layout.fragment_main_cap, container, false);

        startT = mainCap.findViewById(R.id.startC);
        startOCR = mainCap.findViewById(R.id.btcapOCR);
        startOCR.setVisibility(View.GONE);

        if(Integer.parseInt(Constantes.BETA_TESTER)==1){
            startOCR.setVisibility(View.VISIBLE);
        }

        startT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),controlCap.class);
                startActivity(i);
                    limpiarVariables();
            }
        });


        startOCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ocrUp.class);
                startActivity(i);
                    limpiarVariables();
            }
        });

        return mainCap;
    }

    private void limpiarVariables() {


        Constantes.CLAVEL=null;
        Constantes.CURP=null;
        Constantes.AP=null;
        Constantes.AM=null;
        Constantes.NOMBRE=null;
        Constantes.TIPOVIAL=null;
        Constantes.NOMBREVIAL=null;
        Constantes.MZ=null;
        Constantes.LTE=null;
        Constantes.COLONIA=null;
        Constantes.CP=null;
        Constantes.MUNICDOM=null;
        Constantes.ENTIDOM=null;
        Constantes.SECCION=null;
        Constantes.EMIS=null;
        Constantes.VIGENCIA=null;
        Constantes.CIC=null;
        Constantes.OCR=null;
        Constantes.TEL=null;
        Constantes.PFACE=null;
        Constantes.PTWIT=null;
        Constantes.FACE=null;
        Constantes.TWTT=null;
        Constantes.codigoMilit=null;
        Constantes.estadoMilit=null;
        Constantes.partidoMilit=null;
        Constantes.fechMilit=null;
        Constantes.mensajeMilit=null;
        Constantes.firmaPath=null;
        Constantes.eventoSalida=0;
        Constantes.RutaAbsolutaFrente=null;
        Constantes.RutaAbsolutaAtras=null;
        Constantes.encontrado=0;
        Constantes.D1="0";
        Constantes.D2="0";
        Constantes.D3="0";

    }

}