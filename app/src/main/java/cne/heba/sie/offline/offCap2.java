package cne.heba.sie.offline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;


public class offCap2 extends Fragment {

    EditText nombre1,ap1,am1;
    View Offcap2;
    Button contN;
    LottieAnimationView doneIIAn;
    TextView titulo2, subtit2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Offcap2 = inflater.inflate(R.layout.fragment_off_cap2, container, false);

        nombre1 = Offcap2.findViewById(R.id.txtNombre);
        ap1 = Offcap2.findViewById(R.id.txtAPaterno);
        am1 = Offcap2.findViewById(R.id.txtAMaterno);
        titulo2= Offcap2.findViewById(R.id.TitulCap21);
        subtit2= Offcap2.findViewById(R.id.texcap11);
        contN= Offcap2.findViewById(R.id.contNombre);

        nombre1.addTextChangedListener(new TextWatcher() {

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

        ap1.addTextChangedListener(new TextWatcher() {

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

        am1.addTextChangedListener(new TextWatcher() {

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

        contN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(nombre1.getText().toString()!=null || nombre1.getText().toString() == "0" ){
                    Constantes.NOMBRE = nombre1.getText().toString();
                    nombre1.setEnabled(false);
                }else {
                    nombre1.setError("Falta Nombre");
                    nombre1.requestFocus();
                }

                if(ap1.getText().toString()!=null || ap1.getText().toString() == "0"){
                    Constantes.AP = ap1.getText().toString();
                    ap1.setEnabled(false);
                }else {
                    ap1.setError("Falta Apellido paterno");
                    ap1.requestFocus();
                }

                if(am1.getText().toString()!=null){
                    Constantes.AM = am1.getText().toString();
                    am1.setEnabled(false);
                }else {
                    am1.setError("Falta Apellido materno");
                    am1.requestFocus();
                }

                Log.e("Names", Constantes.NOMBRE + " "+ Constantes.AP+" "+Constantes.AM);

            }
        });


        return Offcap2;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {


    }
}