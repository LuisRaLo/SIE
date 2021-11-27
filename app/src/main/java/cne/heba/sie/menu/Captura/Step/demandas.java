package cne.heba.sie.menu.Captura.Step;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
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

import java.util.Objects;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;


public class demandas extends Fragment {

    View VistaDem;

    String demandasF[];
    String[] dema={"Seleccione una Demanda","Pavimentación","Drenaje","Agua Potable","Seguridad"};
    AutoCompleteTextView dem,demI,demII;
    Button agregarDem,quitarDem,saverd;
    Cypher cyf;

    int demVI;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        VistaDem = inflater.inflate(R.layout.fragment_demandas, container, false);

        dem = VistaDem.findViewById(R.id.demandasS);
        demI = VistaDem.findViewById(R.id.demandasSI);
        demII = VistaDem.findViewById(R.id.demandasSII);

        obtenerDemandas();

        cyf = new Cypher();

        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        dem.setThreshold(0);
        demI.setThreshold(0);
        demII.setThreshold(0);

        agregarDem = VistaDem.findViewById(R.id.demAg);
        quitarDem = VistaDem.findViewById(R.id.demQu);
        saverd = VistaDem.findViewById(R.id.saverD);

        saverd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                dem.setEnabled(false);
                demI.setEnabled(false);
                demII.setEnabled(false);

                agregarDem.setVisibility(View.GONE);
                quitarDem.setVisibility(View.GONE);

                String deI,deII,deIII;

                    String[] dataI = dem.getText().toString().split("-");
                    if(Objects.equals(dataI[0],"")) {
                        deI="0";
                        dem.setVisibility(View.GONE);
                    }else {
                        deI = dataI[0];
                    }


                    String[] dataII = demI.getText().toString().split("-");
                    if(Objects.equals(dataII[0],"")) {
                        deII="0";
                        demI.setVisibility(View.GONE);
                    }else {
                        deII = dataII[0];
                    }


                    String[] dataIII = demII.getText().toString().split("-");
                    if(Objects.equals(dataIII[0],"")) {
                        deIII="0";
                        demII.setVisibility(View.GONE);
                    }else {
                        deIII = dataIII[0];
                    }

                    saverd.setVisibility(View.GONE);
                try {
                    Constantes.D1 = cyf.encrypt(deI);
                    Constantes.D2 = cyf.encrypt(deII);
                    Constantes.D3 = cyf.encrypt(deIII);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        agregarDem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(demVI==1){
                   demII.setVisibility(View.VISIBLE);
                   demVI=demVI+1;
               }else if(demVI==2) {
                   Toast.makeText(getContext(), "Solo se permite 3 demandas por contacto.", Toast.LENGTH_SHORT).show();
               }else {
                   quitarDem.setVisibility(View.VISIBLE);
                   demI.setVisibility(View.VISIBLE);
                   demVI=1;
               }


            }
        });

        quitarDem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(demVI==2){
                    demII.setVisibility(View.GONE);
                    demVI=demVI-1;
                }else if(demVI==1){
                    demI.setVisibility(View.GONE);
                    demVI=demVI-1;
                    quitarDem.setVisibility(View.GONE);
                }else{
                    Toast.makeText(getContext(), "No Hay nada que eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });




        return VistaDem;
    }



    private void obtenerDemandas() {

        String url = Constantes.SERVER+"Catalogo_Demandas"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray demandasTotal = new JSONArray(response);
                            JSONObject[] demandas = new JSONObject[demandasTotal.length()];
                            demandasF = new String[demandas.length];
                            for(int i=0; i<demandasTotal.length(); i++){
                                demandas[i] = demandasTotal.getJSONObject(i);
                                demandasF[i] = demandas[i].getString("ID_Catalogo_Demandas")+"- "+demandas[i].getString("Demandas");

                                dem.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, demandasF));
                                demI.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, demandasF));
                                demII.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.select_dialog_item, demandasF));



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
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);


        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

    }
}