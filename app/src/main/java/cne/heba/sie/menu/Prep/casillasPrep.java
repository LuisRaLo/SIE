package cne.heba.sie.menu.Prep;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.MainActivity;
import cne.heba.sie.R;
import cne.heba.sie.abeh.DbHelper;
import cne.heba.sie.homebar;
import cne.heba.sie.myUIX.cargando;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class casillasPrep extends Fragment {

    String vpan, vpri, vprd, vpt, vverde, vmovimiento_ciudadano, vmorena, vnueva_alianza,vpes,vrsp, vfuerza_mexico, vpan_pri_prd, vpan_pri, vpan_prd, vpri_prd, vpt_morena_na, vpt_morena, vpt_na,vmorena_na,vcandidatos_no_registrados,vvotos_nulos;

    Spinner secc, cas;
    EditText pri, mor, pan, prd, na, pevm, pt, pes, mc, hum, rsp, canin, fm, vaxmex,panprit,panprd,pridprd,pt_morena_na,pt_morena,pt_na,morenana,candidatos_no,votos_nulos;
    Button enviar;
    View Prep1;
    Cypher Cyf;

    String[] seccionesE,casillas;
    String[] SeccionElectoral,casilla;

    ImageView imgPri;
    ImageView imgMore;
    ImageView imgmov;
    ImageView imgPan;
    ImageView imgPrd;
    ImageView imgNa;
    ImageView imgPevm;
    ImageView imgPt;
    ImageView imgPes;
    ImageView imgMc;
    ImageView imgRsp;
    ImageView vaxmex_;
    ImageView fuerzaxm_;
    ImageView imgPanpri;
    ImageView img_panprd,img_priprd,img_pt_morena_na,img_pt_morena,img_pt_na,img_morena_na,img_candidatos_no,img_votos_nulos;

    checkaInternet com;

    String solicita;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Prep1 = inflater.inflate(R.layout.fragment_casillas_prep, container, false);

        Cyf = new Cypher();
        try {
            Cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        com = new checkaInternet();

        try {
           solicita = Cyf.encrypt("0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        obtenerSecciones(solicita);

        secc = Prep1.findViewById(R.id.elecseccion);
        cas = Prep1.findViewById(R.id.eleccasilla);

        secc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               SeccionElectoral = secc.getSelectedItem().toString().split("-");
                //Toast.makeText(getContext(), SeccionElectoral[0], Toast.LENGTH_SHORT).show();
                try {
                    solicita = Cyf.encrypt("1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                obtenerCasilla(SeccionElectoral[0],solicita);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                casilla = cas.getSelectedItem().toString().split("-");
                Toast.makeText(getContext(), casilla[0], Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        imgPri = Prep1.findViewById(R.id.img_pri);
        imgMore = Prep1.findViewById(R.id.img_morena);
        imgmov = Prep1.findViewById(R.id.img_mc);
        imgPan = Prep1.findViewById(R.id.img_pan);
        imgPrd = Prep1.findViewById(R.id.img_prd);
        imgNa = Prep1.findViewById(R.id.img_nv);
        imgPevm = Prep1.findViewById(R.id.img_verde);
        imgPt = Prep1.findViewById(R.id.img_pt);
        imgPes = Prep1.findViewById(R.id.img_pes);
        imgMc = Prep1.findViewById(R.id.img_mc);
        imgRsp = Prep1.findViewById(R.id.img_rsp);
        vaxmex_ = Prep1.findViewById(R.id.img_vaxm);
        fuerzaxm_ = Prep1.findViewById(R.id.img_fuerzax);
        imgPanpri = Prep1.findViewById(R.id.img_panpri);
        img_panprd = Prep1.findViewById(R.id.img_panprd);
        img_priprd = Prep1.findViewById(R.id.img_priprd);
        img_pt_morena_na = Prep1.findViewById(R.id.img_ptmorenana);
        img_pt_morena = Prep1.findViewById(R.id.img_ptmorena);
        img_pt_na = Prep1.findViewById(R.id.img_ptna);
        img_morena_na = Prep1.findViewById(R.id.img_morena_na);
        img_candidatos_no = Prep1.findViewById(R.id.img_candidatosno);
        img_votos_nulos = Prep1.findViewById(R.id.img_votos_nulos);

        colocarImagenes();

        pri = Prep1.findViewById(R.id.pri);
        mor = Prep1.findViewById(R.id.morena);
        pan = Prep1.findViewById(R.id.pan);
        prd = Prep1.findViewById(R.id.prd);
        na = Prep1.findViewById(R.id.na);
        pevm = Prep1.findViewById(R.id.verde);
        pt = Prep1.findViewById(R.id.pt);
        pes = Prep1.findViewById(R.id.pes);
        mc = Prep1.findViewById(R.id.mc);
        rsp = Prep1.findViewById(R.id.rsp);
        vaxmex = Prep1.findViewById(R.id.vaxmex);
        fm = Prep1.findViewById(R.id.fuerzax);
        panprit = Prep1.findViewById(R.id.panpri);
        panprd = Prep1.findViewById(R.id.panprd);
        pridprd = Prep1.findViewById(R.id.priprd);
        pt_morena_na = Prep1.findViewById(R.id.ptmorenana);
        pt_morena = Prep1.findViewById(R.id.ptmorena);
        pt_na = Prep1.findViewById(R.id.ptna);
        morenana = Prep1.findViewById(R.id.morena_na);
        candidatos_no = Prep1.findViewById(R.id.candidatosno);
        votos_nulos = Prep1.findViewById(R.id.votos_nulos);

        enviar = Prep1.findViewById(R.id.envplani);

        //cas.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, casillaine));

        enviar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                ObtenerData();

            }

        });

        return Prep1;

    }

    private void ObtenerData() {

        //String vpan, vpri, vprd, vpt, vverde, vmovimiento_ciudadano, vmorena, vnueva_alianza,vpes,vrsp, vfuerza_mexico, vpan_pri_prd, vpan_pri, vpan_prd, vpri_prd, vpt_morena_na, vpt_morena, vpt_na,vmorena_na,vcandidatos_no_registrados,vvotos_nulos;

        vpan = pan.getText().toString();
        vpri = pri.getText().toString();
        vprd = prd.getText().toString();
        vpt = pt.getText().toString();
        vverde = pevm.getText().toString();
        vmovimiento_ciudadano = mc.getText().toString();
        vmorena = mor.getText().toString();
        vnueva_alianza = na.getText().toString();
        vpes = pes.getText().toString();
        vrsp = rsp.getText().toString();
        vfuerza_mexico = fm.getText().toString();
        vpan_pri_prd = vaxmex.getText().toString();
        vpan_pri = panprit.getText().toString();
        vpan_prd = panprd.getText().toString();
        vpri_prd = pridprd.getText().toString();
        vpt_morena_na = pt_morena_na.getText().toString();
        vpt_morena = pt_morena.getText().toString();
        vpt_na = pt_na.getText().toString();
        vmorena_na = morenana.getText().toString();
        vcandidatos_no_registrados = candidatos_no.getText().toString();
        vvotos_nulos = votos_nulos.getText().toString();

        EviarPlanilla();

    }

    private void limpiarData() {

        pan.setText("");
        pri.setText("");
        prd.setText("");
        pt.setText("");
        pevm.setText("");
        mc.setText("");
        mor.setText("");
        morenana.setText("");
        na.setText("");
        pes.setText("");
        rsp.setText("");
        fm.setText("");
        vaxmex.setText("");
        panprit.setText("");
        panprd.setText("");
        pridprd.setText("");
        pt_morena_na.setText("");
        pt_morena.setText("");
        pt_na.setText("");
        morenana.setText("");
        candidatos_no.setText("");
        votos_nulos.setText("");


    }

    private void obtenerCasilla(String seccionElectoral, String solicitud) {

        cargando loadAnim = new cargando();
        SweetAlertDialog a = loadAnim.carga(getContext(),"Obteniendo Datos");

        String url = Constantes.SERVER + "Elecciones_Casillas"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {


                        Log.e("DATA secciones ", response);
                        try {
                            JSONObject resqu = new JSONObject(response);
                            switch (resqu.getInt("success")){
                                case 0:

                                    loadAnim.dismo(a);
                                    Toast.makeText(getContext(), "Error: "+resqu.getString("message"), Toast.LENGTH_SHORT).show();

                                    break;
                                case 1:
                                    loadAnim.dismo(a);
                                    String casill = resqu.getString("data");
                                    JSONArray dataCas = new JSONArray(casill);
                                    casillas = new String[dataCas.length()];
                                    for(int i=0; i<casillas.length; i++){
                                        casillas[i] = dataCas.getJSONObject(i).getString("icas")+"- Casilla: "+dataCas.getJSONObject(i).getString("iuhwef");
                                    }
                                    cas.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, casillas));


                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadAnim.dismo(a);

                if (com.isOnline(getContext())) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Error en el servidor, informe con EDT")
                            .show();
                } else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Sin Conexión a Internet")
                            .show();
                }

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_Usuario", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);
        multiPartRequestWithParams.addStringParam("ID_Subequipo", Constantes.ID_Subequipo);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);
        multiPartRequestWithParams.addStringParam("ID_Seccional", Constantes.ID_Seccional);
        multiPartRequestWithParams.addStringParam("ID_Estatal", Constantes.ID_Estatal);
        multiPartRequestWithParams.addStringParam("EsCasilla", solicitud);
        multiPartRequestWithParams.addStringParam("Seccion", seccionElectoral);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });

    }

    private void obtenerSecciones(String solicita) {

        cargando loadAnim = new cargando();
        SweetAlertDialog a = loadAnim.carga(getContext(),"Obteniendo Datos");

        String url = Constantes.SERVER + "Elecciones_Casillas"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {


                        //Log.e("DATA secciones ", response);
                        try {
                            JSONObject resqu = new JSONObject(response);
                            switch (resqu.getInt("success")){
                                case 0:

                                    loadAnim.dismo(a);
                                    Toast.makeText(getContext(), "Error: "+resqu.getString("message"), Toast.LENGTH_SHORT).show();

                                    break;
                                case 1:

                                    String seccio = resqu.getString("data");
                                    JSONArray dasd = new JSONArray(seccio);
                                    seccionesE = new String[dasd.length()];
                                    for(int i=0; i<seccionesE.length; i++){
                                        seccionesE[i] = dasd.getJSONObject(i).getString("giy")+"- Sección: "+dasd.getJSONObject(i).getString("Secciones");
                                    }
                                    secc.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, seccionesE));
                                    loadAnim.dismo(a);
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadAnim.dismo(a);

                /*try {
                    String eq = Cyf.decrypt(Constantes.ID_Equipo_Detalle);
                    String ID_Subequipo = Cyf.decrypt(Constantes.ID_Subequipo);
                    String ID_Regional = Cyf.decrypt(Constantes.ID_Regional);
                    String ID_Seccional = Cyf.decrypt(Constantes.ID_Seccional);
                    String ID_Estatal = Cyf.decrypt(Constantes.ID_Estatal);
                    String asd = Cyf.decrypt(solicita);

                    Log.e("decrypy: ", Constantes.id_act + " : " + Constantes.token+" : "+eq+" : "+ID_Subequipo+" : "+ID_Regional+" : "+ID_Seccional+" : "+ID_Estatal+" : "+asd);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                 */

                //Log.e("ERROR: ", Constantes.id_act + " : " + Constantes.token+" : "+Constantes.ID_Equipo_Detalle+" : "+Constantes.ID_Subequipo+" : "+Constantes.ID_Regional+" : "+Constantes.ID_Seccional+" : "+Constantes.ID_Estatal+" : "+solicita);
                if (com.isOnline(getContext())) {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Error en el servidor, informe con EDT")
                            .show();
                } else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Sin Conexión a Internet")
                            .show();
                }

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_Usuario", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);
        multiPartRequestWithParams.addStringParam("ID_Subequipo", Constantes.ID_Subequipo);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);
        multiPartRequestWithParams.addStringParam("ID_Seccional", Constantes.ID_Seccional);
        multiPartRequestWithParams.addStringParam("ID_Estatal", Constantes.ID_Estatal);
        multiPartRequestWithParams.addStringParam("EsCasilla", solicita);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
    }

    private void colocarImagenes() {

        Picasso.get()
                .load(R.drawable.pri)
                .resize(120, 120)
                .centerCrop()
                .into(imgPri);
        Picasso.get()
                .load(R.drawable.morena)
                .resize(120, 120)
                .centerCrop()
                .into(imgMore);
        Picasso.get()
                .load(R.drawable.moviciuda)
                .resize(120, 120)
                .centerCrop()
                .into(imgmov);
        Picasso.get()
                .load(R.drawable.pan)
                .resize(120, 120)
                .centerCrop()
                .into(imgPan);
        Picasso.get()
                .load(R.drawable.prd)
                .resize(120, 120)
                .centerCrop()
                .into(imgPrd);
        Picasso.get()
                .load(R.drawable.nuevali)
                .resize(120, 120)
                .centerCrop()
                .into(imgNa);
        Picasso.get()
                .load(R.drawable.verde)
                .resize(120, 120)
                .centerCrop()
                .into(imgPevm);
        Picasso.get()
                .load(R.drawable.pt)
                .resize(120, 120)
                .centerCrop()
                .into(imgPt);
        Picasso.get()
                .load(R.drawable.pes)
                .resize(120, 120)
                .centerCrop()
                .into(imgPes);
        Picasso.get()
                .load(R.drawable.moviciuda)
                .resize(120, 120)
                .centerCrop()
                .into(imgmov);

        Picasso.get()
                .load(R.drawable.rsp)
                .resize(120, 120)
                .centerCrop()
                .into(imgRsp);

        Picasso.get()
                .load(R.drawable.fpm)
                .resize(120, 120)
                .centerCrop()
                .into(fuerzaxm_);

        Picasso.get()
                .load(R.drawable.prian)
                .resize(120, 120)
                .into(vaxmex_);

        Picasso.get()
                .load(R.drawable.panpri)
                .resize(120, 120)
                .into(imgPanpri);

        Picasso.get()
                .load(R.drawable.panprd)
                .resize(120, 120)
                .into(img_panprd);

        Picasso.get()
                .load(R.drawable.priprd)
                .resize(120, 120)
                .into(img_priprd);

        Picasso.get()
                .load(R.drawable.ptmorenana)
                .resize(210, 150)
                .into(img_pt_morena_na);

        Picasso.get()
                .load(R.drawable.ptmorena)
                .resize(210, 150)
                .into(img_pt_morena);

        Picasso.get()
                .load(R.drawable.ptna)
                .resize(210, 150)
                .into(img_pt_na);

        Picasso.get()
                .load(R.drawable.morenana)
                .resize(150, 150)
                .into(img_morena_na);

        Picasso.get()
                .load(R.drawable.independiente)
                .resize(150, 150)
                .into(img_candidatos_no);

        Picasso.get()
                .load(R.drawable.nulo)
                .resize(150, 150)
                .into(img_votos_nulos);


    }

    public void EviarPlanilla() {

        cargando loadAnim = new cargando();
        SweetAlertDialog a = loadAnim.carga(getContext(),"Enviando Datos");

        String url = Constantes.SERVER+"Android_Casillas"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("DATA LOG ", response);

                        loadAnim.dismo(a);

                        try {
                            JSONObject resqu = new JSONObject(response);
                            switch (resqu.getInt("success")){
                                case 1:
                                    SweetAlertDialog done = new SweetAlertDialog(getContext(),SweetAlertDialog.SUCCESS_TYPE);
                                    done.setTitle("¡Planilla Enviada!");
                                    done.show();
                                    limpiarData();
                                    break;
                                case 0:
                                    SweetAlertDialog fail = new SweetAlertDialog(getContext(),SweetAlertDialog.ERROR_TYPE);
                                    fail.setTitle("Error Fatal");
                                    fail.show();
                                    break;
                                default:
                                    Toast.makeText(getContext(), "Codigo no controlado", Toast.LENGTH_SHORT).show();
                                    break;

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                loadAnim.dismo(a);
                SweetAlertDialog errorr = new SweetAlertDialog(getContext(), SweetAlertDialog.SUCCESS_TYPE);
                errorr.setTitle("Datos enviados con Exíto");
                errorr.show();
                Log.e("datos",Constantes.id_act+" : "+Constantes.token);

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);

        multiPartRequestWithParams.addStringParam("pan", vpan);
        multiPartRequestWithParams.addStringParam("pri", vpri);
        multiPartRequestWithParams.addStringParam("prd", vprd);
        multiPartRequestWithParams.addStringParam("pt", vpt);
        multiPartRequestWithParams.addStringParam("verde", vverde);
        multiPartRequestWithParams.addStringParam("movimiento_ciudadano", vmovimiento_ciudadano);
        multiPartRequestWithParams.addStringParam("morena", vmorena);
        multiPartRequestWithParams.addStringParam("nueva_alianza", vnueva_alianza);
        multiPartRequestWithParams.addStringParam("pes", vpes);
        multiPartRequestWithParams.addStringParam("rsp", vrsp);
        multiPartRequestWithParams.addStringParam("fuerza_mexico", vfuerza_mexico);
        multiPartRequestWithParams.addStringParam("pan_pri_prd", vpan_pri_prd);
        multiPartRequestWithParams.addStringParam("pan_pri", vpan_pri);
        multiPartRequestWithParams.addStringParam("pan_prd", vpan_prd);
        multiPartRequestWithParams.addStringParam("pri_prd", vpri_prd);
        multiPartRequestWithParams.addStringParam("pt_morena_na", vpt_morena_na);
        multiPartRequestWithParams.addStringParam("pt_morena", vpt_morena);
        multiPartRequestWithParams.addStringParam("pt_na", vpt_na);
        multiPartRequestWithParams.addStringParam("morena_na", vmorena_na);
        multiPartRequestWithParams.addStringParam("candidatos_no_registrados", vcandidatos_no_registrados);
        multiPartRequestWithParams.addStringParam("votos_nulos", vvotos_nulos);
        multiPartRequestWithParams.addStringParam("total_votos", "0");
        multiPartRequestWithParams.addStringParam("seccion", SeccionElectoral[0]);
        multiPartRequestWithParams.addStringParam("casilla", casilla[0]);



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