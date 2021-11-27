package cne.heba.sie.menu.AcercaDE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

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
import cne.heba.sie.util.Constantes;


public class acercade extends Fragment {

   Button acerca;
   View acercade;

   JSONObject dataV;

   TextView vers;

    private GestureDetector gestureDetector;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        acercade = inflater.inflate(R.layout.fragment_acercade, container, false);

        acerca = acercade.findViewById(R.id.actualiza);
        vers = acercade.findViewById(R.id.titleVersion);

        obtenUpdate();

        vers.setText("Versión Actual: "+Constantes.VERSION);

        acerca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                obtenUpdate();

            }
        });

        return acercade;
    }

    private void obtenUpdate() {

       // Log.e("SALEN DOS DE MACIZA",Constantes.id_act+" TKN:"+ Constantes.token);
        String url = Constantes.SERVER+"Actualizacion"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String Url;

                        //Log.e("Update", response);
                        try {
                            JSONObject updateJSON =  new JSONObject(response);
                            String latestVersion = updateJSON.getString("latestVersion");
                            Url = updateJSON.getString("url");
                            if(latestVersion.equals(Constantes.VERSION)){

                            }else {

                                new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("¡ACTUALIZACIÓN!")
                                        .setContentText("HAY UNA VERSIÓN DISPONIBLE, POR FAVOR ACTUALIZE")
                                        .setConfirmText("Actualizar Ahora")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                sDialog.dismissWithAnimation();

                                                Uri uri = Uri.parse("https://cnencuestas.io/"+Url);
                                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(i);

                                                getActivity().finishAffinity();


                                            }
                                        })
                                        .show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            dataV =new JSONObject(response) ;
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