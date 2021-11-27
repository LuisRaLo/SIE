package cne.heba.sie.myUIX;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;


import cne.heba.sie.R;
import cne.heba.sie.menu.Captura.Step.controlCap;
import cne.heba.sie.util.Constantes;


public class alertDialogo extends DialogFragment {

    Activity actividad;

    ImageButton btnSalir;
    TextView barraSuperior;
    CardView cardFacil;

    public alertDialogo(){

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle saveInstanceState){
        return  crearDialogoGestionarJuego();
    }

    private Dialog crearDialogoGestionarJuego() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        LayoutInflater inflater=getActivity().getLayoutInflater();
        View v=inflater.inflate(R.layout.fragment_alert_dialogo,null);
        builder.setView(v);

        barraSuperior=v.findViewById(R.id.barraSuperior);

        btnSalir=v.findViewById(R.id.btnSalir);
        cardFacil=v.findViewById(R.id.cardFacil);
       // cardMedio=v.findViewById(R.id.cardMedio);

        barraSuperior.setText("PERSONA ENCONTRADA CON CLAVE "+ Constantes.TitleE);

        eventosBotones();
        return builder.create();

    }

    private void eventosBotones() {


        cardFacil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
                Constantes.eventoSalida=2;
                Intent edicio = new Intent(getActivity(), controlCap.class);
                startActivity(edicio);

            }
        });



       /* cardMedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "DAR DE ALTA", Toast.LENGTH_SHORT).show();
                daAltaID(Constantes.IF_ID_ACT);
                Toast.makeText(getContext(),"¡Alta Correcta!", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        */


        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

    private void daAltaID(String ID_USER) {

        String url = "https://189.240.232.89/appirets/AltadeA_ConElect"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Resp Alta", response);


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("ID_Usuario", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Antorchista_Cy", ID_USER);


        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

    }


    @Override
    public void onAttach (Context context){
        super.onAttach(context);
        if (context instanceof  Activity){
            this.actividad= (Activity) context;
        }else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_alert_dialogo, container, false);
    }
}