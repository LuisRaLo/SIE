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
import android.widget.Spinner;

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


public class Rutas extends Fragment {

    AutoCompleteTextView  est, sec, zon, res;
    Spinner reg;
    EditText nomR;
    Button AltaR;
    String[] Regionales;
    String[] Estatales;
    String [] Seccionales;
    String [] Zona;

    String jsonReg;

    Cypher cyf;

    View VistasRutas;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        VistasRutas= inflater.inflate(R.layout.fragment_rutas, container, false);

        reg = VistasRutas.findViewById(R.id.RegR);
        est = VistasRutas.findViewById(R.id.EstaR);
        sec = VistasRutas.findViewById(R.id.SeccR);
        zon = VistasRutas.findViewById(R.id.ZonR);
        nomR = VistasRutas.findViewById(R.id.NomR);
        res = VistasRutas.findViewById(R.id.ResponR);
        AltaR = VistasRutas.findViewById(R.id.AltaR);

        cyf=new Cypher();

        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObtenerRegional();

        /*obtenerregional();
        obtenerEstatal();
        obtenerSeccional();
        obtenerZon();
         */

        AltaR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerdatas();
            }
        });


        return VistasRutas;

    }

    private void ObtenerRegional() {

        Log.e("ACHIS", Constantes.token +" "+Constantes.id_act);

        generarDataRegional();

        String url = Constantes.SERVER+"Obtener_Regionales"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("salen dos regios", response);
                        try {
                            JSONArray regios = new JSONArray(response);
                            JSONObject[] regiosOject = new JSONObject[regios.length()];
                            Regionales = new String[regios.length()];
                            for(int i=0;i<regios.length(); i++){
                                //System.out.println(regios.getJSONObject(i));
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

                        reg.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,Regionales));


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
        multiPartRequestWithParams.addStringParam("Data", jsonReg);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }

    private void generarDataRegional() {


        JSONObject Data = new JSONObject();
        try {
            Data.put("ID_Regional", Constantes.ID_Regional);
            Data.put("ID_Estatal", Constantes.ID_Estatal);
            Data.put("ID_Seccional", Constantes.ID_Seccional);
            Data.put("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);
            Data.put("ID_Subequipo", Constantes.ID_Subequipo);
            Data.put("ID_INE_ListaNominal", Constantes.ID_INE_ListaNominal);
            Data.put("ID_Elecciones_Contactos_Cy", Constantes.ID_Elecciones_Contactos_Cy);
            Data.put("ID_Sistema_Nivel_Crypt", Constantes.ID_Sistema_Nivel_Crypt);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        jsonReg = Data.toString();


        System.out.println("Data_Cyf: " + jsonReg);

    }


    private void obtenerdatas() {

        //String regional =reg.getText().toString();
        String estatl =est.getText().toString();
        String seccional =sec.getText().toString();
        String zona =zon.getText().toString();
        String nombrerut =nomR.getText().toString();
        String respon =res.getText().toString();

    }

}



/*


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
                            Seccionales = new String[esta.length()];
                            for(int i=0;i<esta.length(); i++){
                                System.out.println(esta.getJSONObject(i));
                                seccionalObject[i]=esta.getJSONObject(i);
                                try {
                                    Seccionales[i]=cyf.decrypt(seccionalObject[i].getString("ID_Seccional"))+"- "+cyf.decrypt(seccionalObject[i].getString("Seccionnal"));

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

                        Log.e("salen dos de asada", response);
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



 */