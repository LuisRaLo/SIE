package cne.heba.sie.menu.Inicio;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.R;
import cne.heba.sie.abeh.DbHelper;
import cne.heba.sie.adaptadores.ListAdapter;
import cne.heba.sie.adaptadores.ListElement;
import cne.heba.sie.homebar;
import cne.heba.sie.offline.EditarActivity;
import cne.heba.sie.offline.offlineControlCap;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;

public class inicio extends Fragment {

    View vistaInit;

    Cypher cyf;
    RecyclerView listado;

    SweetAlertDialog pDialog;
    checkaInternet chk;
    String typeErro;

    DbHelper dbC;

    List<ListElement> registros;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //ADAPTADOR DE TRECIENTOS

        vistaInit = inflater.inflate(R.layout.fragment_inicio, container, false);

        listado = vistaInit.findViewById(R.id.simbionte);

        registros = new ArrayList<>();

        listado.setHasFixedSize(true);
        listado.setLayoutManager(new LinearLayoutManager(getContext()));

        chk = new checkaInternet();

        cyf = new Cypher();
        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(Constantes.online==1){
            obtenerPromovidos();
        }else if(Constantes.online==0) {

            obtenerPromovidosOff();

        }


        return vistaInit;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void obtenerPromovidosOff() {

        dbC = new DbHelper(getContext());
        SQLiteDatabase db = dbC.getReadableDatabase();
        Cursor u = db.rawQuery("SELECT * FROM t_registros",null);
        if (u.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)

            do{
                if(Objects.equals(u.getString(1),null)){
                    Log.e("Data","La linea 1 No tiene registro");
                }else
                {
                    try {
                        registros.add(new ListElement(u.getString(0),"#FFFFFF",cyf.decrypt(u.getString(3)),cyf.decrypt(u.getString(1)),":"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            }while (u.moveToNext());

            ListAdapter listAdapter = new ListAdapter(registros, getContext(), new ListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(ListElement item) {
                    moveToDescription(item);
                }
            });
            listado.setAdapter(listAdapter);


        } else {
            new SweetAlertDialog(getContext(), SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("¡Atención!")
                    .setContentText("No Hay Ningún Registro Offline")
                    .setConfirmText("Ok")
                    .show();
            u.close();
            db.close();
        }


    }

    private void obtenerPromovidos() {

        pDialog = new SweetAlertDialog(getContext(),SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setCancelable(false);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FFB5F6"));
        pDialog.show();
        String url = Constantes.SERVER+"Android_Lista_Promovidos"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(pDialog.isShowing()){
                            pDialog.dismissWithAnimation();
                        }

                        try {
                            JSONArray respueArre = new JSONArray(response);
                            JSONObject[] objetos = new JSONObject[respueArre.length()];
                            for (int i=0; i<respueArre.length(); i++){
                                objetos[i] = respueArre.getJSONObject(i);
                                try {

                                    registros.add(new ListElement("","#FFFFFF",cyf.decrypt(objetos[i].getString("resp")),cyf.decrypt(objetos[i].getString("curp")),":"));
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ListAdapter listAdapter = new ListAdapter(registros, getContext(), new ListAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(ListElement item) {
                                moveToDescription(item);
                            }
                        });
                        listado.setAdapter(listAdapter);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(chk.isOnline(getContext())){
                    typeErro = "Error 500";
                }else {
                    typeErro = "Sin Conexión a Internet";
                }

                pDialog.dismissWithAnimation();
                SweetAlertDialog alerto = new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE);
                alerto.setTitleText(typeErro);
                alerto.setContentText("¿Volver a intentarlo?");
                alerto.setCancelText("No");
                alerto.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                alerto.dismissWithAnimation();
                            }
                        });
                alerto.setConfirmText("Sí");
                alerto.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                alerto.dismissWithAnimation();
                                obtenerPromovidos();
                            }
                        });
                alerto.show();

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User_Cy", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token_Cy", Constantes.token);

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
    }

    private void moveToDescription(ListElement item) {

        switch (Constantes.online){
            case 0:

                Intent edita = new Intent(getContext(), EditarActivity.class);
                edita.putExtra("id_us",item.getId_pe());
                startActivity(edita);
                Toast.makeText(getContext(),item.getId_pe(),Toast.LENGTH_SHORT).show();

                break;
            case 1:
                Toast.makeText(getContext(),item.getNameC(),Toast.LENGTH_SHORT).show();
                break;
        }


    }

}