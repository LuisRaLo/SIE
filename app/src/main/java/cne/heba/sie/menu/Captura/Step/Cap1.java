package cne.heba.sie.menu.Captura.Step;

import android.content.Intent;
import android.graphics.Rect;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.R;
import cne.heba.sie.myUIX.alertDialogo;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;
import cne.heba.sie.util.NukeSSLCerts;


public class Cap1 extends Fragment {

    EditText crp, ine;
    //Button compr;
    TextView titulo1, subtit2;

    LottieAnimationView doneAnim;

    SweetAlertDialog parti;

    int comCRP = 0, comCLV = 0;

    String APICURP = "Android_Comprobacion_Ants_CURP";
    String APICLAVE = "Android_Comprobacion_Ants_ClaveElec";
    Cypher cyf;

    View capI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        capI = inflater.inflate(R.layout.fragment_cap1, container, false);

        doneAnim = capI.findViewById(R.id.doneAn);
        titulo1 = capI.findViewById(R.id.TitulCap);
        subtit2 = capI.findViewById(R.id.texcap1);

        new NukeSSLCerts().nuke();
        //PASAR DATOS DE UNA ACTIVIDAD A UN FRAGMENTO CON BUNDLE
        /*
        TextView titulazo = capI.findViewById(R.id.TitulCap);
        String titula = getArguments().getString("title");

        titulazo.setText(titula);
         */

        cyf = new Cypher();
        try {
            cyf.AESCrypt();

        } catch (Exception e) {
            e.printStackTrace();
        }

        crp = capI.findViewById(R.id.CURP);
        ine = capI.findViewById(R.id.ClaveElec);


        ine.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 18) {
                    comprobarINE();
                } else {

                    comCLV = 0;

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        crp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.length() == 18) {
                    comCRP = 1;
                    ine.setEnabled(true);
                    consultaCRP();
                } else {

                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        if(Constantes.eventoSalida==2){

            TextView titulazo = capI.findViewById(R.id.TitulCap);
            titulazo.setText("MODO EDICIÓN");
            crp.setText(Constantes.CURP);
            ine.setText(Constantes.CLAVEL);
            //wcomprobarINE();
            crp.setEnabled(false);
            ine.setEnabled(false);
            doneAnim.playAnimation();
            Constantes.INCUR=1;

        }

        if(Constantes.eventoSalida==1){

            TextView titulazo = capI.findViewById(R.id.TitulCap);
            titulazo.setText("MODO OCR");
            crp.setText(Constantes.CURP);
            ine.setText(Constantes.CLAVEL);
            //consultaCRP();
            //comprobarINE();

        }

        return capI;
    }

    private void consultaCRP() {

        try {
            Constantes.CURP = cyf.encrypt(crp.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = Constantes.SERVER + APICURP; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    //Log.e("RESPUESTA CONSULTA CURP", response.toString());

                        try {
                            JSONObject resCRP = new JSONObject(response);
                            switch (resCRP.getInt("success")) {
                                case 0:
                                    crp.setError(resCRP.getString("message"));
                                    crp.requestFocus();
                                    break;
                                case 1:
                                    crp.setEnabled(false);
                                    JSONObject find = resCRP.getJSONObject("message");
                                    Constantes.AP = find.getString("APATERNO");
                                    Constantes.AM = find.getString("AMATERNO");
                                    Constantes.NOMBRE = find.getString("NOMBRE");
                                    Constantes.eventoSalida=1;
                                    continua();
                                    break;
                                case 2:
                                    if(resCRP.getInt("EsAnt")==1){
                                        JSONObject repet = resCRP.getJSONObject("Ants");
                                        Constantes.IF_ID_ACT = repet.getString("id_antorchistas");
                                        Constantes.AP = repet.getString("APaterno");
                                        Constantes.AM = repet.getString("AMaterno");
                                        Constantes.NOMBRE = repet.getString("Nombre");
                                        Constantes.CURP = repet.getString("CURP");
                                        Constantes.CLAVEL = repet.getString("ClaveElector");
                                        Constantes.SECCION = repet.getString("SeccionalElectoral");
                                        Constantes.OCR = repet.getString("OCR");
                                        Constantes.CIC = repet.getString("CIC");
                                        Constantes.EMIS = repet.getString("NumeroEmision");
                                        Constantes.VIGENCIA = repet.getString("VigenciaINE");
                                        Constantes.TEL = repet.getString("Telefono");
                                        Constantes.FACE = repet.getString("nameFace");
                                        Constantes.TWTT = repet.getString("nameTwit");
                                        Constantes.PFACE = repet.getString("ActivoRedesSociales");
                                        Constantes.PTWIT = repet.getString("ActivoRedesSociales_Twitter");
                                        eventoAnt();
                                    }else {
                                        new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("¡Atención!")
                                                .setContentText(resCRP.getString("message"))
                                                .setConfirmText("Entendido")
                                                .show();
                                        crp.setError(resCRP.getString("message"));
                                        crp.requestFocus();
                                    }
                                    break;
                                default:
                                    crp.setError(resCRP.getString("message"));
                                    crp.requestFocus();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkaInternet chk = new checkaInternet();
                if(chk.isOnline(getContext())){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Vuelve a iniciar sesión por favor")
                            .show();
                }else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Su Conexión a Internet es inestable.")
                            .show();
                }
            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("CURP", Constantes.CURP);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);
        multiPartRequestWithParams.addStringParam("iu", Constantes.id_act);


        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

    }


    private void comprobarINE() {

        try {
            Constantes.CLAVEL = cyf.encrypt(ine.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        String url = Constantes.SERVER + APICLAVE; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                      //  Log.e("RESPUESTA POR INE",response);

                        try {
                            JSONObject dat = new JSONObject(response);
                            switch (dat.getInt("success")){
                                case 0:
                                    ine.setError(dat.getString("message"));
                                    ine.requestFocus();
                                    break;
                                case 1:
                                    try {
                                        ine.setEnabled(false);
                                        JSONObject militA = new JSONObject(dat.getString("Mil_INE_"));
                                        Constantes.mensajeMilit = militA.getString("mensaje");
                                        Constantes.codigoMilit = militA.getString("codigo");
                                        Constantes.nombreMilit = militA.getString("nombre");
                                        Constantes.apMilit = militA.getString("apellidoPaterno");
                                        Constantes.amMilit = militA.getString("apellidoMaterno");
                                        Constantes.estadoMilit = militA.getString("nombreEstado");
                                        Constantes.partidoMilit = militA.getString("nombrePartido");
                                        Constantes.fechMilit = militA.getString("fechaAfiliacion");
                                        if(Integer.parseInt(Constantes.codigoMilit)==200){
                                            parti = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);

                                            parti.setTitleText("¡ATENCIÓN!");
                                            parti.setContentText("EL CONTACTO MILITA EN EL: " + Constantes.partidoMilit);
                                            parti.setCancelable(false);
                                            parti.setConfirmText("Entendido");
                                            parti.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    parti.cancel();
                                                    continua();
                                                }
                                            });
                                            parti.show();
                                        }else {
                                            continua();
                                        }

                                    }catch (Exception milit){

                                    }
                                    break;
                                case 2:
                                    ine.setEnabled(false);
                                    JSONObject DATine = new JSONObject(dat.getString("Ants"));
                                    Constantes.IF_ID_ACT = DATine.getString("id_antorchistas");
                                    Constantes.AP = DATine.getString("APaterno");
                                    Constantes.AM = DATine.getString("AMaterno");
                                    Constantes.NOMBRE = DATine.getString("Nombre");
                                    Constantes.CURP = DATine.getString("CURP");
                                    Constantes.CLAVEL = DATine.getString("ClaveElector");
                                    Constantes.SECCION = DATine.getString("SeccionalElectoral");
                                    Constantes.OCR = DATine.getString("OCR");
                                    Constantes.CIC = DATine.getString("CIC");
                                    Constantes.EMIS = DATine.getString("NumeroEmision");
                                    Constantes.VIGENCIA = DATine.getString("VigenciaINE");
                                    Constantes.TEL = DATine.getString("Telefono");
                                    Constantes.FACE = DATine.getString("nameFace");
                                    Constantes.TWTT = DATine.getString("nameTwit");
                                    Constantes.PFACE = DATine.getString("ActivoRedesSociales");
                                    Constantes.PTWIT = DATine.getString("ActivoRedesSociales_Twitter");

                                    JSONObject militA = new JSONObject(dat.getString("Mil_INE_"));
                                    Constantes.mensajeMilit = militA.getString("mensaje");
                                    Constantes.codigoMilit = militA.getString("codigo");
                                    Constantes.nombreMilit = militA.getString("nombre");
                                    Constantes.apMilit = militA.getString("apellidoPaterno");
                                    Constantes.amMilit = militA.getString("apellidoMaterno");
                                    Constantes.estadoMilit = militA.getString("nombreEstado");
                                    Constantes.partidoMilit = militA.getString("nombrePartido");
                                    Constantes.fechMilit = militA.getString("fechaAfiliacion");

                                    if(Integer.parseInt(Constantes.codigoMilit)==200){
                                        parti = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);

                                        parti.setTitleText("¡ATENCIÓN!");
                                        parti.setContentText("EL CONTACTO MILITA EN EL: " + Constantes.partidoMilit);
                                        parti.setCancelable(false);
                                        parti.setConfirmText("Entendido");
                                        parti.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                parti.cancel();
                                                eventoAnt();
                                            }
                                        });
                                        parti.show();
                                    }else {
                                        eventoAnt();
                                    }


                                    break;
                                default:
                                    ine.setError(dat.getString("message"));
                                    ine.requestFocus();
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkaInternet chk = new checkaInternet();
                if(chk.isOnline(getContext())){
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Quite el ultimo dígito de su Clave INE y coloquelo de nuevo por favor.")
                            .show();
                }else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Su Conexión a Internet es inestable.")
                            .show();
                }


            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("CURP", Constantes.CURP);
        multiPartRequestWithParams.addStringParam("ClaveElector", Constantes.CLAVEL);
        multiPartRequestWithParams.addStringParam("iu", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);


        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

    }


    private void eventoAnt() {
        if (Constantes.encontrado == 1) {
            Constantes.INCUR = 1;
        } else {
            Constantes.INCUR = 1;
            Constantes.encontrado = 1;
            getActivity().finish();
            Constantes.eventoSalida = 2;
            Intent edicio = new Intent(getActivity(), controlCap.class);
            startActivity(edicio);
        }
    }

    private void continua() {

        //Log.e("CAP 1", Constantes.CURP + ", "+ Constantes.CLAVEL);
        doneAnim.playAnimation();
        Constantes.INCUR = 1;
    }

}