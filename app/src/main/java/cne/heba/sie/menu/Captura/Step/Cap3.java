package cne.heba.sie.menu.Captura.Step;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;
import cne.heba.sie.util.NukeSSLCerts;

public class Cap3 extends Fragment {

    View capIV;
    Button sigue;
    EditText ocr, cic, se, nemision, vigencia;
    LottieAnimationView doneIIIAn;
    TextView titulo3, subtit3, subtit4;

    Cypher cyf;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        capIV = inflater.inflate(R.layout.fragment_cap3, container, false);

        doneIIIAn = capIV.findViewById(R.id.doneAnIII);
        titulo3= capIV.findViewById(R.id.TitulCap3);
        subtit3= capIV.findViewById(R.id.recomcap);
        subtit4= capIV.findViewById(R.id.remon2);

        new NukeSSLCerts().nuke();

        cyf = new Cypher();

        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Elementos de EditTex
        ocr = capIV.findViewById(R.id.nOCR);
        cic = capIV.findViewById(R.id.nCIC);
        se = capIV.findViewById(R.id.nSE);
        nemision = capIV.findViewById(R.id.nEmision);
        vigencia = capIV.findViewById(R.id.nVigencia);


        sigue = capIV.findViewById(R.id.sigCap4);

        sigue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String textOcr = ocr.getText().toString();
                String textCic = cic.getText().toString();
                String textSecc = se.getText().toString();
                String textEmis = nemision.getText().toString();
                String textVige = vigencia.getText().toString();

                if(textOcr.length()==13){
                    try {
                        Constantes.OCR=cyf.encrypt(textOcr);
                        ocr.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(textOcr.length()>=1){
                    ocr.setError("Complete el dato o eliminelo");
                    try {
                        Constantes.OCR=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ocr.requestFocus();
                }else {
                    try {
                        Constantes.OCR=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(textCic.length()==13){
                    try {
                        Constantes.CIC=cyf.encrypt(textCic);
                        cic.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(textCic.length()>=1){
                    cic.setError("Complete el dato o eliminelo");
                    cic.requestFocus();
                    try {
                        Constantes.CIC=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        Constantes.CIC=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(textEmis.length()==2){
                    try {
                        Constantes.EMIS=cyf.encrypt(textEmis);
                        nemision.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if(textCic.length()>=1){
                    nemision.setError("Complete el dato o eliminelo");
                    nemision.requestFocus();
                    try {
                        Constantes.EMIS=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        Constantes.EMIS=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(textVige.length()==4){

                    if(Integer.parseInt(textVige)<=2018){
                        vigencia.setError("INE Vencida");
                        vigencia.requestFocus();
                    }else if(Integer.parseInt(textVige)>=2035){
                        vigencia.setError("Vigencia No Valida");
                        vigencia.requestFocus();
                    }else {
                        try {
                            Constantes.VIGENCIA=cyf.encrypt(textVige);
                            vigencia.setEnabled(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }else if(textCic.length()>=1){
                    vigencia.setError("Complete el dato o eliminelo");
                    vigencia.requestFocus();
                    try {
                        Constantes.VIGENCIA=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        Constantes.VIGENCIA=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(textSecc != null && textSecc.length() == 4){

                         if(Integer.parseInt(textSecc)>0){
                             try {
                                 Constantes.SECCION = cyf.encrypt(textSecc);
                             } catch (Exception e) {
                                 e.printStackTrace();
                             }

                             ocr.setEnabled(false);
                             cic.setEnabled(false);
                             nemision.setEnabled(false);
                             vigencia.setEnabled(false);
                             se.setEnabled(false);

                            sigue.setVisibility(View.INVISIBLE);
                            doneIIIAn.setVisibility(View.VISIBLE);
                            doneIIIAn.playAnimation();
                            Constantes.ELECTOR = 1;
                         }else {

                             se.setError("No es una Sección Valida");
                             se.requestFocus();
                         }

                     }else {

                            se.setError("No es una Sección Valida");
                            se.requestFocus();
                }
            }
        });

        return capIV;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(Constantes.ELECTOR == 1){

        }else {
            if (isVisibleToUser) {

                if (Constantes.eventoSalida == 1) {


                }

                if (Constantes.eventoSalida == 2) {

                    se.setText(Constantes.SECCION);
                    vigencia.setText(Constantes.VIGENCIA);
                    nemision.setText(Constantes.EMIS);
                    ocr.setText(Constantes.OCR);
                    cic.setText(Constantes.CIC);


                }

            }
        }

    }
}