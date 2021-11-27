package cne.heba.sie.menu.Prep;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ServiceWorkerWebSettings;
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

import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.MainActivity;
import cne.heba.sie.R;
import cne.heba.sie.homebar;
import cne.heba.sie.menu.Carteo.CarteoUbicacion;
import cne.heba.sie.myUIX.cargando;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class QRprep extends Fragment {

    Button btnQRPre;
    View QRprep;

    Cypher cyfo;

    cargando load;

    checkaInternet chk;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        QRprep = inflater.inflate(R.layout.fragment_q_rprep, container, false);

        btnQRPre = QRprep.findViewById(R.id.btnQRPrep);

        load = new cargando();
        chk = new checkaInternet();

        cyfo = new Cypher();
        try {
            cyfo.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //jeje un saludo xd


        btnQRPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator.forSupportFragment(QRprep.this)
                        .setPrompt("Por Favor pocisione el QR debajo de la cámara")
                        .setBarcodeImageEnabled(true)
                        //.setTimeout(10000)  Limite de tiempo Cámara encendida
                        .setTorchEnabled(true)
                        .setOrientationLocked(false)
                        .initiateScan();
            }
        });

        return QRprep;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(getContext(), "Cancelado", Toast.LENGTH_LONG).show();
            } else {
                try {
                    mandarQR(cyfo.encrypt(result.getContents()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void mandarQR(String QRadeon) {

        SweetAlertDialog cargo = load.carga(getContext(),"Leyendo...");

        String url = Constantes.SERVER+"Android_Capturar_Votante"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){

                        load.dismo(cargo);
                        try {
                            JSONObject resp = new JSONObject(response);
                            switch(resp.getInt("success")){

                                case 0:
                                    new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText(resp.getString("message"))
                                            .show();
                                    break;

                                case 1:
                                    new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText(resp.getString("message"))
                                            .show();
                                    break;
                                default:
                                    new SweetAlertDialog(getContext())
                                            .setTitleText(resp.getString("message"))
                                            .show();
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                load.dismo(cargo);

                if(chk.isOnline(getContext())){
                    SweetAlertDialog erro = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                    erro.setTitle("QR Registrado _");
                    erro.show();
                }else {
                    SweetAlertDialog erro = new SweetAlertDialog(getContext(),SweetAlertDialog.WARNING_TYPE);
                    erro.setTitle("SIN CONEXIÓN");
                    erro.show();
                }



            }
        });

        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("QRVoto", QRadeon);

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