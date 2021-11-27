package cne.heba.sie.menu.Carteo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.R;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class CarteoEntrega extends Fragment {

    Button btnCartEnt;
    View CartEntrega;

    SweetAlertDialog pDialog;
    checkaInternet chk;
    String typeErro;

    Cypher cyfo;
    Spinner situa;
    String qrre=null,confir="",situacion;
    String[] situaciones;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        CartEntrega = inflater.inflate(R.layout.fragment_carteo_entrega, container, false);

        btnCartEnt = CartEntrega.findViewById(R.id.btnCarteoEnt);

        situa = CartEntrega.findViewById(R.id.situ);

        situaciones =new String[] {"Carta Recibida","No Se Encontraba","Cambió de Domicilio","Finado +","No la aceptó"};

        chk = new checkaInternet();

        cyfo = new Cypher();
        try {
            cyfo.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        situa.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, situaciones));

        btnCartEnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator.forSupportFragment(CarteoEntrega.this)
                        .setPrompt("Por Favor pocisione el QR debajo de la cámara")
                        .setBarcodeImageEnabled(true)
                        //.setTimeout(10000)  Limite de tiempo Cámara encendida
                        .setTorchEnabled(true)
                        .setOrientationLocked(false)
                        .initiateScan();

            }
        });

        situa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko = situa.getSelectedItem().toString();
                    if(cricko.equals("Carta Recibida")){
                        confir=cyfo.encrypt("1");
                        situacion=cyfo.encrypt(cricko);
                    }else {
                        situacion=cyfo.encrypt(cricko);
                        confir=cyfo.encrypt("2");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),"El Elemento seleccionado es posición número:" +position + " El String es: " +zon.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return CartEntrega;

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
                consultarQR(qrre,confir,situacion);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void consultarQR(String QRazo, String confirma,String detalle) {

        pDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFB5F6"));
        pDialog.show();

        String url = Constantes.SERVER+"Android_Elecciones_Carta"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){

                        //Log.e("RESPONSE", response);

                        if(pDialog.isShowing()){
                            pDialog.dismissWithAnimation();
                        }

                        try {
                            JSONObject responde = new JSONObject(response);
                            switch (responde.getInt("success")){

                                case 0:
                                    Log.e("error 500",Constantes.id_act+" "+Constantes.token+" "+QRazo+" "+confirma+" "+detalle);
                                    try {
                                        Log.e("error 500 decrypt", cyfo.decrypt(confirma)+" "+ cyfo.decrypt(detalle));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
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
                                            consultarQR(qrre,confir,situacion);
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

                                    break;

                                case 2:
                                    SweetAlertDialog alertoaP = new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE);
                                    alertoaP.setTitleText(responde.getString("message"));
                                    alertoaP.show();
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
                                            consultarQR(qrre,confir,situacion);
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

                //Log.e("error 500",Constantes.id_act+" "+Constantes.token+" "+QRazo+" "+confirma+" "+detalle);
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
                        consultarQR(qrre,confir,situacion);
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
        multiPartRequestWithParams.addStringParam("ConfirmarEntrega", confirma);
        multiPartRequestWithParams.addStringParam("SituacionCarta", detalle);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });

    }
}