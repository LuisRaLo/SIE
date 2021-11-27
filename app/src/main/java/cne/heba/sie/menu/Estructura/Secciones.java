package cne.heba.sie.menu.Estructura;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

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

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class Secciones extends Fragment {

   AutoCompleteTextView reg,est, sec, zon, rut, contac, contardc, contacrda, dtof, dtol, listnom, res;
   EditText secc;
   Button AltaS;

   String[] Regionales;
   String[] Estatales;
   String[] Seccionales;
   String[] Zona;
   String[] Ruta;

   Cypher cyf;
   View VistasSeccion;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        VistasSeccion = inflater.inflate(R.layout.fragment_secciones, container, false);

        reg = VistasSeccion.findViewById(R.id.RegS);
        est = VistasSeccion.findViewById(R.id.EstaS);
        sec = VistasSeccion.findViewById(R.id.SeccS);
        zon = VistasSeccion.findViewById(R.id.ZonS);
        rut = VistasSeccion.findViewById(R.id.RutS);
        contac = VistasSeccion.findViewById(R.id.ContactProg);
        contardc = VistasSeccion.findViewById(R.id.ContactRDC);
        contacrda = VistasSeccion.findViewById(R.id.ContactRDA);
        dtof = VistasSeccion.findViewById(R.id.DtoFs);
        dtol = VistasSeccion.findViewById(R.id.DtoLs);
        listnom = VistasSeccion.findViewById(R.id.listNom);
        res = VistasSeccion.findViewById(R.id.ResSec);
        secc = VistasSeccion.findViewById(R.id.NomSecc);
        AltaS = VistasSeccion.findViewById(R.id.AltaS);


        cyf=new Cypher();

        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        obtenerregional();
        obtenerEstatal();
        obtenerSeccional();
        obtenerZon();
        obtenerRut();

        AltaS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerdatazo();
            }
        });

        return VistasSeccion;
    }

    private void obtenerregional() {

        String url = Constantes.SERVER+"Obtener_Regionales"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("salen dos reg", response);
                        try {
                            JSONArray regios = new JSONArray(response);
                            JSONObject[] regiosOject = new JSONObject[regios.length()];
                            Regionales = new String[regios.length()];
                            for(int i=0;i<regios.length(); i++){
                                System.out.println(regios.getJSONObject(i));
                                regiosOject[i]=regios.getJSONObject(i);
                                try {
                                    Regionales[i]=cyf.decrypt(regiosOject[i].getString("id"))+"- "+cyf.decrypt(regiosOject[i].getString("nameregio"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        reg.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, Regionales));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }

    private void obtenerEstatal() {

        String url = Constantes.SERVER+"Obtener_Estatales"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("salen dos de pechuga", response);
                        try {
                            JSONArray esta = new JSONArray(response);
                            JSONObject[] estataObject = new JSONObject[esta.length()];
                            Estatales = new String[esta.length()];
                            for(int i=0;i<esta.length(); i++){
                                System.out.println(esta.getJSONObject(i));
                                estataObject[i]=esta.getJSONObject(i);
                                try {
                                    Estatales[i]=cyf.decrypt(estataObject[i].getString("ID_Regional"))+"- "+cyf.decrypt(estataObject[i].getString("NombreEstatal"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        est.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, Estatales));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }


    private void obtenerSeccional() {

        String url = Constantes.SERVER+"Obtener_Seccionales"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("salen dos de asada", response);
                        try {
                            JSONArray esta = new JSONArray(response);
                            JSONObject[] seccionalObject = new JSONObject[esta.length()];
                            Estatales = new String[esta.length()];
                            for(int i=0;i<esta.length(); i++){
                                System.out.println(esta.getJSONObject(i));
                                seccionalObject[i]=esta.getJSONObject(i);
                                try {
                                    Estatales[i]=cyf.decrypt(seccionalObject[i].getString("ID_Seccional"))+"- "+cyf.decrypt(seccionalObject[i].getString("Seccionnal"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sec.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, Seccionales));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);
        multiPartRequestWithParams.addStringParam("ID_Estatal", Constantes.ID_Estatal);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }

    private void obtenerZon() {

        String url = Constantes.SERVER+"Android_Equipos_Admin"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("salen dos de telacomes", response);
                        try {
                            JSONArray esta = new JSONArray(response);
                            JSONObject[] zonObject = new JSONObject[esta.length()];
                            Zona = new String[esta.length()];
                            for(int i=0;i<esta.length(); i++){
                                System.out.println(esta.getJSONObject(i));
                                zonObject[i]=esta.getJSONObject(i);
                                try {
                                    Zona[i]=cyf.decrypt(zonObject[i].getString("ID_Equipo"))+"- "+cyf.decrypt(zonObject[i].getString("Equipo"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        zon.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, Zona));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }

    private void obtenerRut() {

        String url = Constantes.SERVER+"Android_Rutas_Admin"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("salen dos de telaentierro", response);
                        try {
                            JSONArray esta = new JSONArray(response);
                            JSONObject[] rutObject = new JSONObject[esta.length()];
                            Ruta = new String[esta.length()];
                            for(int i=0;i<esta.length(); i++){
                                System.out.println(esta.getJSONObject(i));
                                rutObject[i]=esta.getJSONObject(i);
                                try {
                                    Ruta[i]=cyf.decrypt(rutObject[i].getString("ID_Subequipo"))+"- "+cyf.decrypt(rutObject[i].getString("Subequipo"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        zon.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, Zona));


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }


    private void obtenerdatazo() {

       String regional = reg.getText().toString();
       String estatal = est.getText().toString();
       String seccional = sec.getText().toString();
       String zona = zon.getText().toString();
       String ruta = rut.getText().toString();
       String contactacion = contac.getText().toString();
       String contactacionrdc = contardc.getText().toString();
       String contactacionrda = contacrda.getText().toString();
       String dtofederal = dtof.getText().toString();
       String dtolocal = dtol.getText().toString();
       String listanom = listnom.getText().toString();
       String responsable = res.getText().toString();
       String nomseccion = secc.getText().toString();

    }
}