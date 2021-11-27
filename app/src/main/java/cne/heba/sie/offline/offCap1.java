package cne.heba.sie.offline;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.airbnb.lottie.LottieAnimationView;
import cne.heba.sie.R;
import cne.heba.sie.certClaves.Claves;
import cne.heba.sie.util.Constantes;

public class offCap1 extends Fragment {

    EditText crp, ine;
    TextView title1, submit2;
    LottieAnimationView doneAnim;
    Claves claves;
    int comCRP = 0, comCLV = 0;
    View offView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        offView = inflater.inflate(R.layout.fragment_off_cap1, container, false);

        doneAnim = offView.findViewById(R.id.doneAn1);
        title1 = offView.findViewById(R.id.TitulCap1);
        submit2 = offView.findViewById(R.id.texcap1);

        claves=new Claves();

        crp = offView.findViewById(R.id.txtCURP);
        ine = offView.findViewById(R.id.txtClaveElector);

        ine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 18) {
                    Constantes.CLAVEL = ine.getText().toString();
                    if(claves.INE(Constantes.CLAVEL).equals("1")){
                        ine.setEnabled(false);
                        doneAnim.playAnimation();
                        comCLV=1;
                    }else {
                        ine.setError(claves.INE(Constantes.CLAVEL));
                        ine.requestFocus();
                    }

                } else {
                    comCLV = 0;
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        crp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(getContext(), "¡Feliz Cumpleaños!", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        crp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 18) {
                    Constantes.CURP = crp.getText().toString();
                    if(claves.CURP(Constantes.CURP).equals("1")){
                        crp.setEnabled(false);
                        doneAnim.playAnimation();
                    }else {
                        crp.setError(claves.CURP(Constantes.CURP));
                        crp.requestFocus();
                    }

                } else {
                    comCRP = 0;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



        return offView;
    }

}