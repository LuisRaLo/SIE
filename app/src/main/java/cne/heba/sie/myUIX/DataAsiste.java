package cne.heba.sie.myUIX;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;

public class DataAsiste extends AppCompatActivity {

    TextView name,regional,estatal,seccional,equipo,subequipo,seccioElec,curp,tel,nivel,respo;
    Button asiste;
    RequestQueue queue;
    Cypher cyfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_asiste);

        cyfo = new Cypher();

        try {
            cyfo.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        name = findViewById(R.id.nomb);
        regional = findViewById(R.id.regio);
        estatal = findViewById(R.id.estat);
        seccional = findViewById(R.id.seccio);
        equipo = findViewById(R.id.Equipo);
        subequipo = findViewById(R.id.subE);
        seccioElec = findViewById(R.id.seccioElec);
        curp = findViewById(R.id.curpo);
        tel = findViewById(R.id.tele);
        nivel = findViewById(R.id.Nivelo);
        respo = findViewById(R.id.respo);

        asiste = findViewById(R.id.confirmo);

        asiste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    QRVa(Constantes.DCodecito,Constantes.id_reu,Constantes.Observa,cyfo.encrypt("2"));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

        colocarDatos();

    }

    public void colocarDatos(){

        if(Constantes.DNombre.equals("0")){
            Constantes.DNombre="Sin Nombre";
        }
        if(Constantes.DRegio.equals("0")){
            Constantes.DRegio="Sin Region";
        }
        if(Constantes.Desta.equals("0")){
            Constantes.Desta="Sin Estatal";
        }
        if(Constantes.Dsec.equals("0")){
            Constantes.Dsec="Sin Sección";
        }
        if(Constantes.Dequipo.equals("0")){
            Constantes.Dequipo="Sin Equipo";
        }
        if(Constantes.DsubE.equals("0")){
            Constantes.DsubE="Sin SubEquipo";
        }
        if(Constantes.DseccioElec.equals("0")){
            Constantes.DseccioElec="Sin Sección Electoral";
        }
        if(Constantes.Dcurp.equals("0")){
            Constantes.Dcurp="Sin CURP";
        }
        if(Constantes.Dtel.equals("0")){
            Constantes.Dtel="Sin Teléfono";
        }
        if(Constantes.Dnivel.equals("0")){
            Constantes.Dnivel="Sin Nivel";
        }
        if(Constantes.Drespo.equals("0")){
            Constantes.Drespo="Sin Responsable";
        }

        name.setText(Constantes.DNombre);
        regional.setText(Constantes.DRegio);
        estatal.setText(Constantes.Desta);
        seccional.setText(Constantes.Dsec);
        equipo.setText(Constantes.Dequipo);
        subequipo.setText(Constantes.DsubE);
        seccioElec.setText(Constantes.DseccioElec);
        curp.setText(Constantes.Dcurp);
        tel.setText(Constantes.Dtel);
        nivel.setText(Constantes.Dnivel);
        respo.setText(Constantes.Drespo);

    }

    public void QRVa(String code, String reun, String observ, String conAss){

        String url = Constantes.SERVER+"elecciones_paseLista"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject resku = new JSONObject(response);
                            switch (resku.getInt("success")){

                                case 2:
                                    SweetAlertDialog alerto = new SweetAlertDialog(DataAsiste.this, SweetAlertDialog.SUCCESS_TYPE);
                                    alerto.setTitleText(resku.getString("message"));
                                    alerto.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    });
                                    alerto.show();
                                    break;
                                default:

                                    SweetAlertDialog alertoa = new SweetAlertDialog(DataAsiste.this, SweetAlertDialog.ERROR_TYPE);
                                    alertoa.setTitleText("¡Que que!");
                                    alertoa.show();

                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                //Log.e("ERROR", Constantes.id_act+" : "+Constantes.token+" : "+code+" : "+reun+" : "+ observ);

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

        queue = Volley.newRequestQueue(DataAsiste.this);
        if (queue == null)
            queue = Volley.newRequestQueue(DataAsiste.this);
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
    }

}