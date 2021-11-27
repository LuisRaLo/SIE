package cne.heba.sie.offline;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class offCap4 extends Fragment {

    Button finalc1;
    EditText face1, twiiter1, telefono1;
    CheckBox f1, t1;
    Cypher Cyf1;
    String cF1, cT1;
    TextView titulo41, subtit51;
    private Button btnGuardar, btnMostrar, btnC4;

    View OffCap4;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        OffCap4 = inflater.inflate(R.layout.fragment_off_cap4, container, false);

        // Elementos del EdditTex
        face1 = OffCap4.findViewById(R.id.txtfacebook);
        twiiter1 = OffCap4.findViewById(R.id.txtTewitter);
        telefono1 = OffCap4.findViewById(R.id.txtNumeroTelefono);
        f1 = OffCap4.findViewById(R.id.checkF1);
        t1 = OffCap4.findViewById(R.id.checkT1);
        titulo41 = OffCap4.findViewById(R.id.TitulCap41);
        subtit51 = OffCap4.findViewById(R.id.subtit1);
        btnC4 = OffCap4.findViewById(R.id.btnCap4);

        //finalc1 = OffCap4.findViewById(R.id.finalcap1);


        if(f1.isChecked()){
            Constantes.PFACE = "1";
        }else {
            Constantes.PFACE = "0";
        }

        if(t1.isChecked()){
            Constantes.PTWIT = "1";
        }else {
            Constantes.PTWIT = "0";
        }

        face1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        twiiter1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        telefono1.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 10) {
                    Constantes.TEL = telefono1.getText().toString();
                } else {
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnC4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(face1.getText().toString()!=null){
                    Constantes.FACE = face1.getText().toString();
                    face1.setEnabled(false);
                }else {
                    face1.setError("Falta Facebook");
                    face1.requestFocus();
                }

                if(twiiter1.getText().toString()!=null){
                    Constantes.TWTT = twiiter1.getText().toString();
                    twiiter1.setEnabled(false);
                }else {
                    twiiter1.setError("Falta Twitter");
                    twiiter1.requestFocus();
                }

                if(telefono1.getText().toString()!=null){
                    Constantes.TEL = telefono1.getText().toString();
                    telefono1.setEnabled(false);
                }else {
                    telefono1.setError("Falta Número de teléfono");
                    telefono1.requestFocus();
                }

                if(f1.getText().toString()!=null){
                    Constantes.PFACE = f1.getText().toString();
                    f1.setEnabled(false);
                }else {
                    f1.setError("Falta marcar si participa en Facebook");
                    f1.requestFocus();
                }

                if(t1.getText().toString()!=null){
                    Constantes.PTWIT = t1.getText().toString();
                    t1.setEnabled(false);
                }else {
                    t1.setError("Falta marcar si participa en Twitter");
                    t1.requestFocus();
                }
                if(telefono1 != null) {
                    Intent SaveReg = new Intent(getContext(), firmaInitoff.class);
                    startActivity(SaveReg);
                }
                getActivity().finish();
            }

        });


        return OffCap4;
    }

}