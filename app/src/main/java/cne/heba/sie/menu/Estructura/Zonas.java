package cne.heba.sie.menu.Estructura;

import android.os.Bundle;
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


public class Zonas extends Fragment {


    //VARIABLES DE ALTA ZONA


    String Id_Regio,Id_estatal,Id_Sec,Id_Mun,id_resp,nombreZ;

    Spinner reg, esta, sec, mun, res;
    EditText nomz;
    Button Alta;

    Cypher cyf;

    String jsonData;

    String[] Regionales;
    String[] Estatales;
    String[] Seccionales;
    String[] Municipios;
    String[] Equipos;
    String[] Rutas;
    String[] Responsables;
    View VistaZonas;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        VistaZonas = inflater.inflate(R.layout.fragment_zonas, container, false);

        reg = VistaZonas.findViewById(R.id.RegZ);
        esta = VistaZonas.findViewById(R.id.EstaZ);
        sec = VistaZonas.findViewById(R.id.SeccZ);
        mun = VistaZonas.findViewById(R.id.MunZ);
        res = VistaZonas.findViewById(R.id.ResponZ);
        nomz = VistaZonas.findViewById(R.id.NomZ);
        Alta = VistaZonas.findViewById(R.id.AltaZ);



        cyf=new Cypher();

        try {
            cyf.AESCrypt();
        } catch (Exception e) {

            e.printStackTrace();

        }

        obtenerregional();
        obtenerEstatal(Constantes.ID_Regional);
        obtenerSeccional(Constantes.ID_Regional,Constantes.ID_Estatal);
        ObtenerMunicipios(Constantes.ID_Regional);
        obtenerRepsonsables(Constantes.ID_Regional,Constantes.ID_Estatal,Constantes.ID_Seccional);

        ObtenerEquipos();
        //ObtenerRutas();

        Alta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerdata();
            }
        });

        reg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = reg.getSelectedItem().toString().split("-");
                    Id_Regio = cyf.encrypt(cricko[0]);
                    obtenerEstatal(Id_Regio);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        esta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = esta.getSelectedItem().toString().split("-");
                    Id_estatal = cyf.encrypt(cricko[0]);
                    obtenerSeccional(Id_Regio,Id_estatal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sec.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = sec.getSelectedItem().toString().split("-");
                    Id_Sec = cyf.encrypt(cricko[0]);
                    obtenerRepsonsables(Id_Regio,Id_estatal,Id_Sec);
                    ObtenerMunicipios(Id_Sec);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mun.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = mun.getSelectedItem().toString().split("-");
                    Id_Mun = cyf.encrypt(cricko[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        res.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    String cricko[] = res.getSelectedItem().toString().split("-");
                    id_resp = cyf.encrypt(cricko[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    return VistaZonas;
    }

    private void generarJSONRespo(String RegionalB, String EstatalB, String SeccionalB) {

        JSONObject Data = new JSONObject();
        try {
            Data.put("ID_Regional", RegionalB);
            Data.put("ID_Estatal", EstatalB);
            Data.put("ID_Seccional", SeccionalB);
            Data.put("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);
            Data.put("ID_Subequipo", Constantes.ID_Subequipo);
            Data.put("ID_INE_ListaNominal", Constantes.ID_INE_ListaNominal);
            Data.put("ID_Elecciones_Contactos_Cy", Constantes.ID_Elecciones_Contactos_Cy);
            Data.put("ID_Sistema_Nivel_Crypt", Constantes.ID_Sistema_Nivel_Crypt);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        jsonData = Data.toString();


        System.out.println("Data_Cyf: " + jsonData);


        JSONObject Data_N = new JSONObject();
        try {
            try {
                Data_N.put("ID_Regional", cyf.decrypt(RegionalB));
                Data_N.put("ID_Estatal", cyf.decrypt(EstatalB));
                Data_N.put("ID_Seccional", cyf.decrypt(SeccionalB));
                Data_N.put("ID_Equipo_Detalle", cyf.decrypt(Constantes.ID_Equipo_Detalle));
                Data_N.put("ID_Subequipo", cyf.decrypt(Constantes.ID_Subequipo));
                Data_N.put("ID_INE_ListaNominal", cyf.decrypt(Constantes.ID_INE_ListaNominal));
                Data_N.put("ID_Elecciones_Contactos_Cy", cyf.decrypt(Constantes.ID_Elecciones_Contactos_Cy));
                Data_N.put("ID_Sistema_Nivel_Crypt", cyf.decrypt(Constantes.ID_Sistema_Nivel_Crypt));
            } catch (Exception e) {

                e.printStackTrace();

            }


           System.out.println("Decyf => "+Data_N.toString());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void obtenerregional() {

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

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }


    private void ObtenerMunicipios(String SeccB) {

        //Log.e("TRIPITA: ", Constantes.token+" "+Constantes.id_act+" "+SeccB);

        String url = Constantes.SERVER+"Android_Obtener_Municipios"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Log.e("salen dos de Mun", response);
                        try {
                            JSONArray regios = new JSONArray(response);
                            JSONObject[] regiosOject = new JSONObject[regios.length()];
                            Municipios = new String[regios.length()];
                            for(int i=0;i<regios.length(); i++){
                                //System.out.println(regios.getJSONObject(i));
                                regiosOject[i]=regios.getJSONObject(i);
                                try {
                                    Municipios[i]=cyf.decrypt(regiosOject[i].getString("id_mun"))+"- "+cyf.decrypt(regiosOject[i].getString("mun"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        mun.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,Municipios));

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
        multiPartRequestWithParams.addStringParam("Seccional", SeccB);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }

    private void obtenerEstatal(String RegionalB) {

        String url = Constantes.SERVER+"Obtener_Estatales"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Log.e("salen dos Estatales", response);
                        try {
                            JSONArray esta = new JSONArray(response);
                            JSONObject[] estataObject = new JSONObject[esta.length()];
                             Estatales = new String[esta.length()];
                            for(int i=0;i<esta.length(); i++){
                                //System.out.println(esta.getJSONObject(i));
                                estataObject[i]=esta.getJSONObject(i);
                                try {
                                    Estatales[i]=cyf.decrypt(estataObject[i].getString("id_est"))+"- "+cyf.decrypt(estataObject[i].getString("Est"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        esta.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,Estatales));

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
        multiPartRequestWithParams.addStringParam("ID_Regional", RegionalB);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }


    private void obtenerSeccional(String RegionalB, String EstatalB) {

        String url = Constantes.SERVER+"Obtener_Seccionales_Grandes"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("salen dos Seccionales", response);
                        try {
                            JSONArray esta = new JSONArray(response);
                            JSONObject[] seccionalObject = new JSONObject[esta.length()];
                            Seccionales = new String[esta.length()];
                            for(int i=0;i<esta.length(); i++){
                               // System.out.println(esta.getJSONObject(i));
                                seccionalObject[i]=esta.getJSONObject(i);
                                try {
                                    Seccionales[i]=cyf.decrypt(seccionalObject[i].getString("id_sec"))+"- "+cyf.decrypt(seccionalObject[i].getString("Sec"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        sec.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item,Seccionales));

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
        multiPartRequestWithParams.addStringParam("ID_Regional", RegionalB);
        multiPartRequestWithParams.addStringParam("ID_Estatal", EstatalB);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }

    //SON ZONAS
    private void ObtenerEquipos() {

        String url = Constantes.SERVER+"Android_Equipos_Admin"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("salen dos zonas", response);
                        try {
                            JSONArray regios = new JSONArray(response);
                            JSONObject[] regiosOject = new JSONObject[regios.length()];
                            Equipos = new String[regios.length()];
                            for(int i=0;i<regios.length(); i++){
                                //System.out.println(regios.getJSONObject(i));
                                regiosOject[i]=regios.getJSONObject(i);
                                try {
                                    Equipos[i]=cyf.decrypt(regiosOject[i].getString("id_eq"))+"- "+cyf.decrypt(regiosOject[i].getString("Eq"));

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
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(7000,
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

    //SON RUTAS
    private void ObtenerRutas() {

        String url = Constantes.SERVER+"Android_Rutas_Admin"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //Log.e("salen dos Rutas", response);
                        try {
                            JSONArray regios = new JSONArray(response);
                            JSONObject[] regiosOject = new JSONObject[regios.length()];
                            Rutas = new String[regios.length()];
                            for(int i=0;i<regios.length(); i++){
                               // System.out.println(regios.getJSONObject(i));
                                regiosOject[i]=regios.getJSONObject(i);
                                try {
                                    Rutas[i]=cyf.decrypt(regiosOject[i].getString("id_rut"))+"- "+cyf.decrypt(regiosOject[i].getString("rut"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(7000,
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

    private void obtenerRepsonsables(String RegionB, String EstalB, String SeccB){

        generarJSONRespo(RegionB,EstalB,SeccB);

        Log.e("ID_User_Cy", Constantes.id_act);
        Log.e("Token_Cy", Constantes.token);

        String url = Constantes.SERVER+"Android_Responsables_Promovidos"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Respuesta Responsables", response);

                        try {
                            JSONArray respos = new JSONArray(response);
                            JSONObject[] regiosOject = new JSONObject[respos.length()];
                            Responsables = new String[respos.length()];
                            for(int i=0;i<respos.length(); i++){
                                // System.out.println(regios.getJSONObject(i));
                                regiosOject[i]=respos.getJSONObject(i);
                                try {
                                    Responsables[i]=cyf.decrypt(regiosOject[i].getString("idrs"))+"- "+cyf.decrypt(regiosOject[i].getString("resp"));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (JSONException e) {
                            Toast.makeText(getContext(),"No Hay Responsables Para Este Filtro",Toast.LENGTH_LONG).show();
                        }

                        if(Responsables!=null) {
                            res.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, Responsables));
                        }else {
                            Toast.makeText(getContext(),"No Hay Responsables Para Este Filtro",Toast.LENGTH_LONG).show();
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {



            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(7000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);
        multiPartRequestWithParams.addStringParam("Data", jsonData);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);
    }


    private void darAltaZona(String NombreZon) {

        Log.e("DATOS: ", Constantes.id_act+" "+Constantes.token);
        String url = Constantes.SERVER+"Altas_Zonas_Android"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Responde Alta Zona", response);


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

    private void generarJSONAltaZ(String NombreZ) {

        JSONObject Data = new JSONObject();
        try {

            Data.put("Nombre_Zona", NombreZ);
            Data.put("ID_Regional", Id_Regio);
            Data.put("ID_Estatal", Id_estatal);
            Data.put("ID_Seccional", Id_Sec);
            Data.put("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);
            Data.put("ID_Subequipo", Constantes.ID_Subequipo);
            Data.put("ID_INE_ListaNominal", Constantes.ID_INE_ListaNominal);
            Data.put("ID_Elecciones_Contactos_Cy", Constantes.ID_Elecciones_Contactos_Cy);
            Data.put("ID_Sistema_Nivel_Crypt", Constantes.ID_Sistema_Nivel_Crypt);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonData = Data.toString();

        System.out.println("Data: "+jsonData);

    }


    private void obtenerdata() {

        String nombrezona = null;
        try {
            nombrezona = cyf.encrypt(nomz.getText().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        generarJSONAltaZ(nombrezona);


    }
}