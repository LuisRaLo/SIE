package cne.heba.sie.menu.Captura.Step;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;
import cne.heba.sie.util.NukeSSLCerts;

public class Cap2 extends Fragment {

    EditText nombre,ap,am;
    View capII;
    Cypher cyf;
    LottieAnimationView doneIIAn;
    TextView titulo2, subtit2;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        capII = inflater.inflate(R.layout.fragment_cap2, container, false);

        nombre = capII.findViewById(R.id.nombre);
        ap = capII.findViewById(R.id.APaterno);
        am = capII.findViewById(R.id.AMaterno);
        titulo2= capII.findViewById(R.id.TitulCap2);
        subtit2= capII.findViewById(R.id.texcap2);

        new NukeSSLCerts().nuke();

        doneIIAn = capII.findViewById(R.id.doneAnII);

        cyf = new Cypher();

        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.equals("") || s.equals(" ")) {
                    Constantes.NOMBRE = "";
                }else {
                    Constantes.NOMBRE = s.toString();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ap.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.equals("") || s.equals(" ")) {
                    Constantes.AP = "";
                }else {
                    Constantes.AP = s.toString();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        am.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if(s.equals("") || s.equals(" ")) {
                    Constantes.AM = "";
                }else {
                    if(Constantes.NOMBRE!=null){
                        if(Constantes.NOMBRE.equals("") || Constantes.NOMBRE.equals(" ")){
                            nombre.setError("Vació");
                        }else {

                        }

                    }else if(Constantes.AP==null){
                        if(Constantes.AP.equals("") || Constantes.AP.equals(" ")){
                            ap.setError("Vació");
                        }else {

                        }
                    }else {

                        Constantes.AM = s.toString();
                        doneIIAn.playAnimation();
                        //swiped.setVisibility(View.VISIBLE);
                        //Toast.makeText(getContext(),"Deslize Para Continuar",Toast.LENGTH_SHORT).show();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return capII;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {

                if (Constantes.eventoSalida == 2) {
                    nombre.setText(Constantes.NOMBRE);
                    ap.setText(Constantes.AP);
                    am.setText(Constantes.AM);
                    nombre.setEnabled(false);
                    ap.setEnabled(false);
                    am.setEnabled(false);
                    Constantes.NOAPMA = 1;
                    doneIIAn.playAnimation();
                }

                if (Constantes.eventoSalida == 1) {

                        String Snom="",Sap="",Sam="";

                    try {

                        Snom = cyf.decrypt(Constantes.NOMBRE);
                        Sap = cyf.decrypt(Constantes.AP);
                        Sam = cyf.decrypt(Constantes.AM);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                        nombre.setText(Snom);
                        ap.setText(Sap);
                        am.setText(Sam);

                        nombre.setEnabled(false);
                        ap.setEnabled(false);
                        am.setEnabled(false);

                        doneIIAn.playAnimation();

                        Constantes.NOAPMA = 1;

                        repararData();

                }

        }
            }

    private void repararData() {

        try {
            Constantes.NOMBRE=cyf.encrypt(Constantes.NOMBRE);
            Constantes.AP=cyf.encrypt(Constantes.AP);
        } catch (Exception e) {
            e.printStackTrace();
        }

       // Log.e("Nombres ", "A ver "+ Constantes.NOMBRE+" "+Constantes.AP+" "+Constantes.AM);

    }


}