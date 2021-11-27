package cne.heba.sie.offline;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

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
import cne.heba.sie.abeh.DbHelper;
import cne.heba.sie.adaptadores.ListAdapter;
import cne.heba.sie.menu.Captura.Step.controlCap;
import cne.heba.sie.menu.Captura.Step.firmaInit;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;

public class EditarActivity extends AppCompatActivity {

    private ListAdapter listAdapter;

    private EditText txtCURPE,txtClvE,txtNombreE,txtAPaternoE,txtAMaternoE,txtOCRE,txtCICE,txtSEE,txtNumEE,txtnVigE,txtfacE,txtTewittE,txtNumeroTE,CallE,NumEE,NumIE,ManzE,nLotE,cPostalE;
    private CheckBox checkF1,checkT1;
    private Button btnGdar;

    String TipoCaptura;

    DbHelper dbC;
    Cypher cyf;
    SweetAlertDialog pDialog,parti;

    String APICURP = "Android_Comprobacion_Ants_CURP";
    String APICLAVE = "Android_Comprobacion_Ants_ClaveElec";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar);

        String id_usuario = getIntent().getStringExtra("id_us");

        cyf = new Cypher();
        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        txtCURPE = findViewById(R.id.editaCUR);
        txtClvE = findViewById(R.id.editaClave);
        txtNombreE = findViewById(R.id.editaNomb);
        txtAPaternoE = findViewById(R.id.editaApatE);
        txtAMaternoE = findViewById(R.id.editaAmaterno);
        txtOCRE = findViewById(R.id.editaOCR);
        txtCICE = findViewById(R.id.editaCIC);
        txtSEE = findViewById(R.id.editaSE);
        txtNumEE = findViewById(R.id.editaEmis);
        txtnVigE = findViewById(R.id.editaVigencia);
        txtfacE = findViewById(R.id.editaFace);
        txtTewittE = findViewById(R.id.editaTwitter);
        txtNumeroTE = findViewById(R.id.editaTel);
        CallE = findViewById(R.id.editaCall);
        NumEE = findViewById(R.id.editaNumExt);
        NumIE = findViewById(R.id.editaNumInt);
        cPostalE = findViewById(R.id.editaCod);

        btnGdar = findViewById(R.id.btnEdGuard);

        llenarDatos(id_usuario);

        btnGdar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultaCRP(Constantes.CURP);
            }
        });

    }

    private void llenarDatos(String id) {

        dbC = new DbHelper(this);
        SQLiteDatabase db = dbC.getReadableDatabase();
        Cursor u = db.rawQuery("SELECT * FROM t_registros WHERE id_registro = "+id,null);

        if (u.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
            try {

                txtCURPE.setText(cyf.decrypt(u.getString(1)));
                txtClvE.setText(cyf.decrypt(u.getString(2)));
                txtNombreE.setText(cyf.decrypt(u.getString(3)));
                txtAPaternoE.setText(cyf.decrypt(u.getString(4)));
                txtAMaternoE.setText(cyf.decrypt(u.getString(5)));
                txtOCRE.setText(cyf.decrypt(u.getString(6)));
                txtCICE.setText(cyf.decrypt(u.getString(7)));
                txtSEE.setText(cyf.decrypt(u.getString(8)));
                txtNumEE.setText(cyf.decrypt(u.getString(9)));
                txtnVigE.setText(cyf.decrypt(u.getString(10)));
                txtfacE.setText(cyf.decrypt(u.getString(11)));
                txtTewittE.setText(cyf.decrypt(u.getString(12)));
                txtNumeroTE.setText(cyf.decrypt(u.getString(13)));
                CallE.setText(cyf.decrypt(u.getString(14)));
                NumEE.setText(cyf.decrypt(u.getString(15)));
                NumIE.setText(cyf.decrypt(u.getString(16)));
                cPostalE.setText(cyf.decrypt(u.getString(17)));

                Constantes.CURP = u.getString(1);
                Constantes.CLAVEL = u.getString(2);
                Constantes.NOMBRE = u.getString(3);
                Constantes.AP = u.getString(4);
                Constantes.AM = u.getString(5);
                Constantes.OCR = u.getString(6);
                Constantes.CIC = u.getString(7);
                Constantes.SECCION = u.getString(8);
                Constantes.EMIS = u.getString(9);
                Constantes.VIGENCIA = u.getString(10);
                Constantes.FACE = u.getString(11);
                Constantes.TWTT = u.getString(12);
                Constantes.TEL = u.getString(13);
                Constantes.NOMBREVIAL = u.getString(14);
                Constantes.EMIS = u.getString(15);
                Constantes.SECCION = u.getString(16);
                Constantes.CP = u.getString(17);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        u.close();
        db.close();

    }

    private void comprobarINE(String clavelo, String curpo) {

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
                                    Toast.makeText(EditarActivity.this, dat.getString("message"), Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    try {

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
                                            parti = new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.WARNING_TYPE);

                                            parti.setTitleText("¡ATENCIÓN!");
                                            parti.setContentText("EL CONTACTO MILITA EN EL: " + Constantes.partidoMilit);
                                            parti.setCancelable(false);
                                            parti.setConfirmText("Entendido");
                                            parti.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                    parti.dismissWithAnimation();
                                                    //continua();
                                                }
                                            });
                                            parti.show();
                                        }else {
                                           // continua();
                                        }

                                    }catch (Exception milit){

                                    }
                                    break;
                                case 2:

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
                                        parti = new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.WARNING_TYPE);

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
                                    Toast.makeText(EditarActivity.this,dat.getString("message") , Toast.LENGTH_SHORT).show();
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
                if(chk.isOnline(EditarActivity.this)){
                    new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Quite el ultimo dígito de su Clave INE y coloquelo de nuevo por favor.")
                            .show();
                }else {
                    new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Su Conexión a Internet es inestable.")
                            .show();
                }


            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("CURP", curpo);
        multiPartRequestWithParams.addStringParam("ClaveElector", clavelo);
        multiPartRequestWithParams.addStringParam("iu", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);


        RequestQueue queue = Volley.newRequestQueue(EditarActivity.this);
        queue.add(multiPartRequestWithParams);

    }

    private void darAlta(String firmaPath) {


        String url = "https://189.240.232.89/api/Android/APIS/AltaContacto"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Log.e("Resp FIRMA",response);
                        try {
                            JSONObject r = new JSONObject(response);
                            if(r.getInt("success")==1){

                                String altNombre = null;
                                try {
                                    altNombre = cyf.decrypt(Constantes.NOMBRE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("¡Exito!")
                                        .setContentText("Se dio de alta a "+ altNombre)
                                        .setConfirmText("Ok ¡Gracias!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{

                                Toast.makeText(EditarActivity.this, r.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("¡Oops!")
                        .setContentText("Tuvimos un pequeño problema, vuelve a intentarlo por favor :3.")
                        .setConfirmText("Vale :(")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();
            }
        });

        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);
        multiPartRequestWithParams.addStringParam("ClaveElector", Constantes.CLAVEL);
        multiPartRequestWithParams.addStringParam("CURP", Constantes.CURP);
        multiPartRequestWithParams.addStringParam("APaterno", Constantes.AP);
        multiPartRequestWithParams.addStringParam("AMaterno", Constantes.AM);
        multiPartRequestWithParams.addStringParam("Nombre", Constantes.NOMBRE);

        multiPartRequestWithParams.addStringParam("SeccionElectoral", Constantes.SECCION);
        multiPartRequestWithParams.addStringParam("Emision", Constantes.EMIS);
        multiPartRequestWithParams.addStringParam("Vigencia", Constantes.VIGENCIA);
        multiPartRequestWithParams.addStringParam("CIC", Constantes.CIC);
        multiPartRequestWithParams.addStringParam("OCR", Constantes.OCR);
        multiPartRequestWithParams.addStringParam("Longitud", Constantes.Clongitude);
        multiPartRequestWithParams.addStringParam("Latitud", Constantes.Clatitude);
        multiPartRequestWithParams.addStringParam("Telefono", Constantes.TEL);
        multiPartRequestWithParams.addStringParam("StatusF", Constantes.PFACE);
        multiPartRequestWithParams.addStringParam("StatusT", Constantes.PTWIT);
        multiPartRequestWithParams.addStringParam("nameFace", Constantes.FACE);
        multiPartRequestWithParams.addStringParam("nameTwit", Constantes.TWTT);
        multiPartRequestWithParams.addStringParam("militancia_code", Constantes.codigoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Estado", Constantes.estadoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Partido", Constantes.partidoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Afiliacion", Constantes.fechMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Messages", Constantes.mensajeMilit);

        //ESTRUCTURA

        multiPartRequestWithParams.addStringParam("TipoCaptura", TipoCaptura);
        multiPartRequestWithParams.addStringParam("ID_CatalogoNiveles_Activismo", Constantes.Control_Nivel_ID);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);
        multiPartRequestWithParams.addStringParam("ID_Estatal", Constantes.ID_Estatal);
        multiPartRequestWithParams.addStringParam("ID_Seccional", Constantes.ID_Seccional);
        multiPartRequestWithParams.addStringParam("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);  //DEPENDIENDO DE LO QUE SE SELECCIONE
        multiPartRequestWithParams.addStringParam("ID_Subequipo", Constantes.ID_Subequipo);
        multiPartRequestWithParams.addStringParam("ID_INE_ListaNominal", Constantes.ID_INE_ListaNominal);
        multiPartRequestWithParams.addStringParam("ID_Organismo", Constantes.ID_Organismo);
        multiPartRequestWithParams.addStringParam("ID_Calle", Constantes.ID_Calle);
        multiPartRequestWithParams.addStringParam("ID_Responsable", Constantes.id_act); //PENDIENTE
        multiPartRequestWithParams.addStringParam("D1", Constantes.D1);
        multiPartRequestWithParams.addStringParam("D2", Constantes.D2);
        multiPartRequestWithParams.addStringParam("D3", Constantes.D2);

        if(firmaPath!=null){

            multiPartRequestWithParams.addFile("FirmaConset", firmaPath);
           // Log.e("FIRMA","SI AÑADO LA FIRMA :c");

        }


        if(Constantes.RutaAbsolutaFrente!=null)
            multiPartRequestWithParams.addFile("Ine_Anv", Constantes.RutaAbsolutaFrente);

        if(Constantes.RutaAbsolutaAtras!=null)
            multiPartRequestWithParams.addFile("Ine_Rev", Constantes.RutaAbsolutaAtras);


        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(multiPartRequestWithParams);


    }

    private void consultaCRP(String curpo) {

        SweetAlertDialog load = new SweetAlertDialog(EditarActivity.this,SweetAlertDialog.PROGRESS_TYPE);
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
                                    Toast.makeText(EditarActivity.this, resCRP.getString("message"), Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    JSONObject find = resCRP.getJSONObject("message");
                                    Constantes.AP = find.getString("APATERNO");
                                    Constantes.AM = find.getString("AMATERNO");
                                    Constantes.NOMBRE = find.getString("NOMBRE");
                                    Constantes.eventoSalida=1;
                                    darAlta("");
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

                                        new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                .setTitleText("¡Atención!")
                                                .setContentText(resCRP.getString("message"))
                                                .setConfirmText("Entendido")
                                                .show();
                                    }
                                    break;
                                default:

                                    Toast.makeText(EditarActivity.this, resCRP.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkaInternet chk = new checkaInternet();
                if(chk.isOnline(EditarActivity.this)){
                    new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Vuelve a iniciar sesión por favor")
                            .show();
                }else {
                    new SweetAlertDialog(EditarActivity.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("No Hay Conexión a Internet")
                            .show();
                }
            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("CURP", curpo);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);
        multiPartRequestWithParams.addStringParam("iu", Constantes.id_act);


        RequestQueue queue = Volley.newRequestQueue(EditarActivity.this);
        queue.add(multiPartRequestWithParams);

    }

    private void eventoAnt() {
        if (Constantes.encontrado == 1) {
            Constantes.INCUR = 1;
        } else {
            Constantes.INCUR = 1;
            Constantes.encontrado = 1;
            finish();
            Constantes.eventoSalida = 2;
            Intent edicio = new Intent(this, controlCap.class);
            startActivity(edicio);
        }
    }
}