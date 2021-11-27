package cne.heba.sie.menu.Captura.Step;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.R;
import cne.heba.sie.abeh.DbHelper;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;
import cne.heba.sie.util.Estados;
import cne.heba.sie.util.NukeSSLCerts;


public class estructur extends Fragment {

    View vistaEstr;

    TextView tzon,trut,tsec,tcall;
    Spinner zon, rut, secc, call;

    String[] zonas, rutas, secciones;

    Cypher cyfo;

    DbHelper conn;

    SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vistaEstr = inflater.inflate(R.layout.fragment_estructur, container, false);

        new NukeSSLCerts().nuke();

        conn = new DbHelper(getContext());

        cyfo = new Cypher();

        try {
            cyfo.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObtenerEquipos(Constantes.ID_Regional,Constantes.ID_Estatal,Constantes.ID_Seccional);
        ObtenerRutas(Constantes.ID_Regional,Constantes.ID_Estatal,Constantes.ID_Seccional);
        obtenerSeccional();


        zon =  vistaEstr.findViewById(R.id.spinner2);
        rut =  vistaEstr.findViewById(R.id.spinner3);
        secc = vistaEstr.findViewById(R.id.spinner4);
        call = vistaEstr.findViewById(R.id.spinner5);

        tzon =  vistaEstr.findViewById(R.id.zonText);
        trut =  vistaEstr.findViewById(R.id.rutText);
        tsec =  vistaEstr.findViewById(R.id.seccText);
        tcall = vistaEstr.findViewById(R.id.calleTest);

        switch (Constantes.ID_USER_NIVEL){


            case 100:

                trut.setVisibility(View.VISIBLE);
                tsec.setVisibility(View.VISIBLE);
                tcall.setVisibility(View.VISIBLE);
                rut.setVisibility(View.VISIBLE);
                secc.setVisibility(View.VISIBLE);
                call.setVisibility(View.VISIBLE);

                break;

            case 101:

                tsec.setVisibility(View.VISIBLE);
                tcall.setVisibility(View.VISIBLE);
                secc.setVisibility(View.VISIBLE);
                call.setVisibility(View.VISIBLE);

                break;

            case 102:


                tsec.setVisibility(View.VISIBLE);
                secc.setVisibility(View.VISIBLE);

                break;

            case 103:


                break;

            case 104:

                break;

            case 105:

                break;

            case 200:

                break;

            default:

                zon.setVisibility(View.VISIBLE);
                rut.setVisibility(View.VISIBLE);
                secc.setVisibility(View.VISIBLE);
                call.setVisibility(View.VISIBLE);

                tzon.setVisibility(View.VISIBLE);
                trut.setVisibility(View.VISIBLE);
                tsec.setVisibility(View.VISIBLE);
                tcall.setVisibility(View.VISIBLE);

                break;

        }


        zon.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = zon.getSelectedItem().toString().split("-");
                    Constantes.ID_ZONA = cyfo.encrypt(cricko[0]);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),"El Elemento seleccionado es posición número:" +position + " El String es: " +zon.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        rut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = rut.getSelectedItem().toString().split("-");
                    Constantes.ID_RUTA = cyfo.encrypt(cricko[0]);
                    //obtenSecc(Constantes.zona, Constantes.ruta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Toast.makeText(getApplicationContext(),"El Elemento seleccionado es posición número:" +position + " El String es: " +rut.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        secc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = secc.getSelectedItem().toString().split("-");
                    Constantes.ID_SECCION = cyfo.encrypt(cricko[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   Toast.makeText(getApplicationContext(),"El Elemento seleccionado es posición número:" +position + " El String es: " +secc.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        call.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                   // Constantes.calle = cyfo.encrypt(call.getSelectedItem().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //   Toast.makeText(getApplicationContext(),"El Elemento seleccionado es posición número:" +position + " El String es: " +call.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return vistaEstr;

    }


    private void ObtenerRutas(String i_regio, String i_estado, String i_secc) {

        String url = Constantes.SERVER+"Android_Rutas_Admin"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("salen dos Detalles", response);
                        try {
                            JSONArray regios = new JSONArray(response);
                            JSONObject[] regiosOject = new JSONObject[regios.length()];
                            rutas = new String[regios.length()];
                            for(int i=0;i<regios.length(); i++){
                                System.out.println(regios.getJSONObject(i));
                                regiosOject[i]=regios.getJSONObject(i);
                                try {
                                    rutas[i]=cyfo.decrypt(regiosOject[i].getString("id_rut"))+"- "+cyfo.decrypt(regiosOject[i].getString("rut"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        rut.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, rutas));

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkaInternet chk = new checkaInternet();
                if(chk.isOnline(getContext())){
                   /* new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error 500...")
                            .setContentText("Por favor reporte este error a CNDT")
                            .show();

                    */
                }else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Su Conexión a Internet es inestable.")
                            .show();
                }

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Regional", i_regio);
        multiPartRequestWithParams.addStringParam("ID_Estatal", i_estado);
        multiPartRequestWithParams.addStringParam("ID_Seccional", i_secc);
        multiPartRequestWithParams.addStringParam("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }



    //SON ZONAS
    private void ObtenerEquipos(String i_regio, String i_estado, String i_secc) {

        String url = Constantes.SERVER+"Android_Equipos_Admin"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("salen dos rego", response);
                        try {
                            JSONArray regios = new JSONArray(response);
                            JSONObject[] regiosOject = new JSONObject[regios.length()];
                            zonas = new String[regios.length()];
                            for(int i=0;i<regios.length(); i++){
                                System.out.println(regios.getJSONObject(i));
                                regiosOject[i]=regios.getJSONObject(i);
                                try {
                                    zonas[i]=cyfo.decrypt(regiosOject[i].getString("id_eq"))+"- "+cyfo.decrypt(regiosOject[i].getString("Eq"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        zon.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, zonas));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkaInternet chk = new checkaInternet();
                if(chk.isOnline(getContext())){
                   /* new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error 500...")
                            .setContentText("Por favor reporte este error a CNDT")
                            .show();

                    */
                }else {
                    new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Oops...")
                            .setContentText("Su Conexión a Internet es inestable.")
                            .show();
                }

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Regional", i_regio);
        multiPartRequestWithParams.addStringParam("ID_Estatal", i_estado);
        multiPartRequestWithParams.addStringParam("ID_Seccional", i_secc);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }

    private void obtenerSeccional() {

        Log.e("a",Constantes.ID_Regional+" "+Constantes.ID_Estatal+" "+Constantes.ID_Seccional+" "+Constantes.ID_Equipo_Detalle+" "+Constantes.ID_Subequipo);

        String url = Constantes.SERVER+"Obtener_Seccionales"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("salen dos de asada", response);
                        try {
                            JSONArray esta = new JSONArray(response);
                            JSONObject[] seccionalObject = new JSONObject[esta.length()];
                            secciones = new String[esta.length()];
                            for(int i=0;i<esta.length(); i++){
                                System.out.println(esta.getJSONObject(i));
                                seccionalObject[i]=esta.getJSONObject(i);
                                try {


                                     db = conn.getWritableDatabase();

                                    ContentValues values = new ContentValues();

                                    values.put("id_secserver",seccionalObject[i].getString("id_sec"));
                                    values.put("seccion", seccionalObject[i].getString("sec"));


                                    long newRowId = db.insert("t_secciones", null, values);

                                    secciones[i]=cyfo.decrypt(seccionalObject[i].getString("id_sec"))+"- Sección: "+cyfo.decrypt(seccionalObject[i].getString("sec"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                            db.close();
                            conn.close();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        secc.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, secciones));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                checkaInternet chk = new checkaInternet();
                if(chk.isOnline(getContext())){
                   /* new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error 500...")
                            .setContentText("Por favor reporte este error a CNDT")
                            .show();

                    */
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


        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);
        multiPartRequestWithParams.addStringParam("ID_Estatal", Constantes.ID_Estatal);
        multiPartRequestWithParams.addStringParam("ID_Seccional", Constantes.ID_Seccional);
        multiPartRequestWithParams.addStringParam("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);
        multiPartRequestWithParams.addStringParam("ID_Subequipo", Constantes.ID_Subequipo); //ide_rutas

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }





}