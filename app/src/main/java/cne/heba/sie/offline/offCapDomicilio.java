package cne.heba.sie.offline;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;


public class offCapDomicilio extends Fragment {

    EditText Call, NumInt, NumExt, Manzana, nLote, cPostal;
    LottieAnimationView doneAnCapDomici;
    TextView TitCpaDomi, txtDomi;
    Button btnCapDomici;
    View offCapDomici;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        offCapDomici = inflater.inflate(R.layout.fragment_off_cap_domicilio, container, false);

        //doneIIIAn1 = OffCap3.findViewById(R.id.doneAnIII1);
        TitCpaDomi= offCapDomici.findViewById(R.id.TitCpaDomi);
        txtDomi= offCapDomici.findViewById(R.id.txtDomi);
        doneAnCapDomici = offCapDomici.findViewById(R.id.doneAnCapDomici);


        //Elementos de EditTex
        Call = offCapDomici.findViewById(R.id.Call);
        NumInt = offCapDomici.findViewById(R.id.NumInt);
        NumExt = offCapDomici.findViewById(R.id.NumExt);
        Manzana = offCapDomici.findViewById(R.id.Manzana);
        nLote = offCapDomici.findViewById(R.id.nLote);
        cPostal = offCapDomici.findViewById(R.id.cPostal);
        btnCapDomici = offCapDomici.findViewById(R.id.btnCapDomici);

        btnCapDomici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Call.getText().toString()!=null){
                    Constantes.NOMBREVIAL = Call.getText().toString();
                    Call.setEnabled(false);
                }else {
                    Call.setError("Falta Nombre");
                    Call.requestFocus();
                }

                if(NumInt.getText().toString()!=null){
                    Constantes.NI = NumInt.getText().toString();
                    NumInt.setEnabled(false);
                }else {
                    NumInt.setError("Falta Apellido paterno");
                    NumInt.requestFocus();
                }

                if(NumExt.getText().toString()!=null){
                    Constantes.NE = NumExt.getText().toString();
                    NumExt.setEnabled(false);
                }else {
                    NumExt.setError("Falta Apellido materno");
                    NumExt.requestFocus();
                }

                if(Manzana.getText().toString()!=null){
                    Constantes.MZ = Manzana.getText().toString();
                    NumExt.setEnabled(false);
                }else {
                    Manzana.setError("Falta Apellido materno");
                    Manzana.requestFocus();
                }

                if(nLote.getText().toString()!=null){
                    Constantes.LTE = nLote.getText().toString();
                    nLote.setEnabled(false);
                }else {
                    nLote.setError("Falta Apellido materno");
                    nLote.requestFocus();
                }

                if(cPostal.getText().toString()!=null){
                    Constantes.CP = cPostal.getText().toString();
                    cPostal.setEnabled(false);
                }else {
                    cPostal.setError("Falta Apellido materno");
                    cPostal.requestFocus();
                }
            }
        });


        return offCapDomici;
    }
}