package cne.heba.sie.offline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;


public class offCap3 extends Fragment {

    EditText ocr1, cic1, se1, nemision1, vigencia1;
    LottieAnimationView doneIIIAn1;
    TextView titulo31, subtit31, subtit41;
    Button btnC3;

    int comOCR = 0, comCIC = 0, comSECC = 0, comEMISI = 0, comVIGENC = 0;

    View OffCap3;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        OffCap3 = inflater.inflate(R.layout.fragment_off_cap3, container, false);

        //doneIIIAn1 = OffCap3.findViewById(R.id.doneAnIII1);
        titulo31= OffCap3.findViewById(R.id.TitulCap31);
        subtit31= OffCap3.findViewById(R.id.recomcap1);
        subtit41= OffCap3.findViewById(R.id.remon21);

        //Elementos de EditTex
        ocr1 = OffCap3.findViewById(R.id.txtOCR);
        cic1 = OffCap3.findViewById(R.id.txtCIC);
        se1 = OffCap3.findViewById(R.id.txtSeccionElectoral);
        nemision1 = OffCap3.findViewById(R.id.txtNumEmision);
        vigencia1 = OffCap3.findViewById(R.id.txtnVigencia);
        btnC3 = OffCap3.findViewById(R.id.btnCap3);



        ocr1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 13) {
                    Constantes.OCR =  ocr1.getText().toString();
                } else {
                    comOCR = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cic1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 13) {
                    Constantes.CIC =  cic1.getText().toString();
                } else {
                    comCIC = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        se1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 4) {
                    Constantes.SECCION =  se1.getText().toString();
                } else {
                    comSECC = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nemision1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 2) {
                    Constantes.EMIS =  nemision1.getText().toString();
                } else {
                    comEMISI = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        vigencia1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 4) {
                    Constantes.VIGENCIA =  vigencia1.getText().toString();
                } else {
                    comVIGENC = 0;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnC3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ocr1.getText().toString()!=null){
                    Constantes.OCR = ocr1.getText().toString();
                    ocr1.setEnabled(false);
                }else {
                    ocr1.setError("Falta Nombre");
                    ocr1.requestFocus();
                }

                if(cic1.getText().toString()!=null){
                    Constantes.CIC = cic1.getText().toString();
                    cic1.setEnabled(false);
                }else {
                    cic1.setError("Falta Nombre");
                    cic1.requestFocus();
                }

                if(se1.getText().toString()!=null){
                    Constantes.SECCION = se1.getText().toString();
                    se1.setEnabled(false);
                }else {
                    se1.setError("Falta Nombre");
                    se1.requestFocus();
                }

                if(nemision1.getText().toString()!=null){
                    Constantes.EMIS = nemision1.getText().toString();
                    nemision1.setEnabled(false);
                }else {
                    nemision1.setError("Falta Nombre");
                    nemision1.requestFocus();
                }

                if(vigencia1.getText().toString()!=null){
                    Constantes.VIGENCIA = vigencia1.getText().toString();
                    nemision1.setEnabled(false);
                }else {
                    vigencia1.setError("Falta Nombre");
                    vigencia1.requestFocus();
                }

            }
        });

        return OffCap3;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

    }

}
