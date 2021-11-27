package cne.heba.sie.menu.Asistencia;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.R;
import cne.heba.sie.myUIX.DataAsiste;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class asiste extends Fragment {

   View asisteV;
    RequestQueue queue;
    Cypher cyfo;
    EditText observa;
    String[] reun;
    Spinner reuni;
    checkaInternet com;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        asisteV = inflater.inflate(R.layout.fragment_asiste, container, false);

        Button readerLaunch = asisteV.findViewById(R.id.asisteLaunch);
        reuni = asisteV.findViewById(R.id.spinReun);
        observa = asisteV.findViewById(R.id.observaciones);

        cyfo = new Cypher();
        com=new checkaInternet();

        try {
            cyfo.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        obtenerReuniones();

        readerLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constantes.DRegio = null;
                Constantes.Desta = null;
                Constantes.Dsec = null;
                Constantes.Dequipo = null;
                Constantes.DsubE = null;
                Constantes.DseccioElec = null;
                Constantes.Dcurp = null;
                Constantes.DNombre = null;
                Constantes.Dtel = null;
                Constantes.Dnivel = null;
                Constantes.Drespo = null;

                IntentIntegrator.forSupportFragment(asiste.this)
                        .setPrompt("Por Favor pocisione el QR debajo de la cámara")
                        .setBarcodeImageEnabled(true)
                        //.setTimeout(10000)  Limite de tiempo Cámara encendida
                        .setTorchEnabled(true)
                        .setOrientationLocked(false)
                        .initiateScan();

            }
        });

        reuni.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = reuni.getSelectedItem().toString().split("-");

                    Constantes.id_reu  = cyfo.encrypt(cricko[0]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),"El Elemento seleccionado es posición número:" +position + " El String es: " +zon.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return asisteV;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_LONG).show();
            } else {
                try {
                    Constantes.DCodecito = cyfo.encrypt(result.getContents());
                    Constantes.Observa = cyfo.encrypt(observa.getText().toString());
                    QRVa(Constantes.DCodecito,Constantes.id_reu,Constantes.Observa,cyfo.encrypt("1"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void obtenerReuniones(){

        String url = Constantes.SERVER+"Android_Obtener_Reuniones"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response){

                        try {
                            JSONArray arrayReun = new JSONArray(response);
                            JSONObject[] reuni = new JSONObject[arrayReun.length()];
                            reun = new String[reuni.length];
                            for(int i=0; i<reuni.length; i++){
                                reuni[i] = arrayReun.getJSONObject(i);
                                try {
                                    Log.e("Fecha-Hora",cyfo.decrypt(reuni[i].getString("FR")));
                                    reun[i] = cyfo.decrypt(reuni[i].getString("R"))+"- Reunion de: "+cyfo.decrypt(reuni[i].getString("TR"))+" Fecha: "+cyfo.decrypt(reuni[i].getString("FR"));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        reuni.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, reun));

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                String typeErro="Nulo";

                if(com.isOnline(getContext())){
                    typeErro="Error 500";
                }else {
                    typeErro="Sin Conexión";
                    SweetAlertDialog alerto = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                    alerto.setTitleText(typeErro);
                    alerto.setContentText("¿Volver a intentarlo?");
                    alerto.setCancelText("No");
                    alerto.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            alerto.dismissWithAnimation();
                        }
                    });
                    alerto.setConfirmText("Sí");
                    alerto.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            alerto.dismissWithAnimation();
                            obtenerReuniones();
                        }
                    });
                    alerto.show();
                }

                //Log.e("ERROR REUNI", Constantes.id_act+" : "+Constantes.token+" : "+Constantes.ID_Regional+" : "+Constantes.ID_Estatal+" : "+ Constantes.ID_Seccional);

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);
        multiPartRequestWithParams.addStringParam("ID_Estatal", Constantes.ID_Estatal);
        multiPartRequestWithParams.addStringParam("ID_Seccional", Constantes.ID_Seccional);

        queue = Volley.newRequestQueue(getContext());
        if (queue == null)
            queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
    }

    public void QRVa(String code, String reun, String observ, String conAss){

        String url = Constantes.SERVER+"elecciones_paseLista"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Data QR", response);
                        try {
                            JSONObject resku = new JSONObject(response);
                            switch (resku.getInt("success")){
                                case 0:
                                    SweetAlertDialog alerto = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    alerto.setTitleText(resku.getString("message"));
                                    alerto.show();
                                    break;
                                case 1:
                                    JSONObject data = resku.getJSONObject("data");
                                    try {
                                        Constantes.DRegio = cyfo.decrypt(data.getString("NombreRegional"));
                                        Constantes.Desta = cyfo.decrypt(data.getString("NombreEstatal"));
                                        Constantes.Dsec = cyfo.decrypt(data.getString("Seccional"));
                                        Constantes.Dequipo = cyfo.decrypt(data.getString("Equipo"));
                                        Constantes.DsubE = cyfo.decrypt(data.getString("Subequipo"));
                                        Constantes.DseccioElec = cyfo.decrypt(data.getString("Seccion"));
                                        Constantes.Dcurp = cyfo.decrypt(data.getString("CURP"));
                                        Constantes.DNombre = cyfo.decrypt(data.getString("Nombre"));
                                        Constantes.Dtel = cyfo.decrypt(data.getString("Telefono"));
                                        Constantes.Dnivel = cyfo.decrypt(data.getString("Nivel"));
                                        Constantes.Drespo = cyfo.decrypt(data.getString("ID_Responsable"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Intent startInfo = new Intent(getContext(), DataAsiste.class);
                                    startActivity(startInfo);
                                    break;

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.e("ERROR", Constantes.id_act+" : "+Constantes.token+" : "+code+" : "+reun+" : "+ observ+ ":"+ conAss);

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Asistente", code);
        multiPartRequestWithParams.addStringParam("ID_Reunion", reun);
        multiPartRequestWithParams.addStringParam("Observaciones", observ);
        multiPartRequestWithParams.addStringParam("ConfirmarAsistencia", conAss);

        queue = Volley.newRequestQueue(getContext());
        if (queue == null)
            queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
    }
}