package cne.heba.sie.menu.Carteo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
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
import cne.heba.sie.menu.Asistencia.asiste;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class CarteoUbicacion extends Fragment {

    Button btnBuscarCarteo;
    View CarteoUbica;
    SweetAlertDialog pDialog;
    checkaInternet chk;
    String typeErro;

    Cypher cyfo;

    String qrre=null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CarteoUbica = inflater.inflate(R.layout.fragment_carteo_ubicacion, container, false);

       btnBuscarCarteo = CarteoUbica.findViewById(R.id.btnCarteoBs);

       chk = new checkaInternet();

       cyfo = new Cypher();
        try {
            cyfo.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnBuscarCarteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator.forSupportFragment(CarteoUbicacion.this)
                        .setPrompt("Por Favor pocisione el QR debajo de la cámara")
                        .setBarcodeImageEnabled(true)
                        //.setTimeout(10000)  Limite de tiempo Cámara encendida
                        .setTorchEnabled(true)
                        .setOrientationLocked(false)
                        .initiateScan();

            }
        });

        return CarteoUbica;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_LONG).show();
            } else {
                try {
                    qrre = cyfo.encrypt(result.getContents());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                consultarQR(qrre);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void consultarQR(String QRazo) {

        pDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFB5F6"));
        pDialog.show();

        String url = Constantes.SERVER+"Android_Elecciones_Carta"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){

                        Log.e("RESPONSE",response);

                        if(pDialog.isShowing()){
                            pDialog.dismissWithAnimation();
                        }

                        try {
                            JSONObject responde = new JSONObject(response);
                            switch (responde.getInt("success")){

                                case 0:
                                    SweetAlertDialog alerto = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                                    alerto.setTitleText(responde.getString("message"));
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
                                            consultarQR(qrre);
                                        }
                                    });
                                    alerto.show();
                                    break;
                                case 1:
                                    SweetAlertDialog alertoa = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                                    alertoa.setTitleText("¡Exito!");
                                    alertoa.show();
                                    String lat="";
                                    String lon="";
                                    try {
                                        lat = cyfo.decrypt(responde.getString("Latitud"));
                                        lon = cyfo.decrypt(responde.getString("Longitud"));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    abrirMaps(lat,lon);
                                    break;
                                default:
                                    SweetAlertDialog alertop = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    alertop.setTitleText(responde.getString("message"));
                                    alertop.setContentText("¿Volver a intentarlo?");
                                    alertop.setCancelText("No");
                                    alertop.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            alertop.dismissWithAnimation();
                                        }
                                    });
                                    alertop.setConfirmText("Sí");
                                    alertop.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            alertop.dismissWithAnimation();
                                            consultarQR(qrre);
                                        }
                                    });
                                    alertop.show();
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error 500",Constantes.id_act+" "+Constantes.token+" "+QRazo);
                if(chk.isOnline(getContext())){
                    typeErro = "Error 500";
                }else {
                    typeErro = "Sin Conexión a Internet";
                }

                pDialog.dismissWithAnimation();
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
                        consultarQR(qrre);
                    }
                });
                alerto.show();
            }
        });

        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("QRCartaRecibida", QRazo);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });

    }

    public void abrirMaps(String Lati, String Longi){

        Uri intentUri = Uri.parse("geo:"+Lati+","+Longi+"?z=16&q="+Lati+","+Longi+"(Esta+Es+La+Etiqueta)");
        Intent intent = new Intent(Intent.ACTION_VIEW, intentUri);
        startActivity(intent);

    }

}