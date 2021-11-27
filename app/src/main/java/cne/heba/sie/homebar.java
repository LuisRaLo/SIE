package cne.heba.sie;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.abeh.DbHelper;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;
import cne.heba.sie.util.GPSTracker;
import de.hdodenhof.circleimageview.CircleImageView;

public class homebar extends AppCompatActivity {

    Timer timer = new Timer();

    DbHelper conn;

    private AppBarConfiguration mAppBarConfiguration;
    Cypher cyf;
    String APIUBICA="Android_GPS";

    String mNombre, MAp,MAm;

    GPSTracker gps;

    RequestQueue queue;

    String fota = "";

    checkaInternet compr;

    AsyncTask mytask;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homebar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        compr = new checkaInternet();

        if(mytask!=null){
            mytask.cancel(true);
        }

        cyf = new Cypher();
        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //FUNCIONES OFFLINE-ONLINE

        if(Constantes.online==1) {
            //Log.e("beta"," aa "+ Constantes.BETA_TESTER);
            try {
                mNombre = cyf.decrypt(Constantes.name);
                MAp = cyf.decrypt(Constantes.ap);
                MAm = cyf.decrypt(Constantes.am);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (Integer.parseInt(Constantes.BETA_TESTER) == 1) {
                MAm = MAm + " (beta)";
            }
        }else if(Constantes.online==0) {
            try{
                mNombre = cyf.decrypt(Constantes.name);
            }catch (Exception exception){
                mNombre = Constantes.name;
            }
            MAp = "Sin Conexión";
            try {
                if (Integer.parseInt(Constantes.BETA_TESTER) == 1) {
                    MAm ="(beta)";
                }else {
                    MAm="";
                }
            }catch (Exception e){

            }

        }

        conn = new DbHelper(this);

        SQLiteDatabase db = conn.getReadableDatabase();
        if(db!=null){
            Cursor u = db.rawQuery("SELECT * FROM t_usuarios",null);
            if(u.getCount()>0){
                u.close();
                conn.close();
            }else{
                avisoProvacidad();
            }
        }else {
            Toast.makeText(getApplicationContext(),"Error 23", Toast.LENGTH_SHORT).show();
        }

        gps = new GPSTracker(getApplicationContext());

        if(Constantes.online==1){
            obtenerUbica();
            comprobarPASS();
            comprobarUpdate();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.mainCapter)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View headerView = navigationView.getHeaderView(0);
        headerView.animate();

        CircleImageView perfil = (CircleImageView) headerView.findViewById(R.id.profiles);
        TextView navNombre = (TextView) headerView.findViewById(R.id.titleName);
        navNombre.setText(mNombre+" "+MAp+" "+MAm);
        TextView navEstado = (TextView) headerView.findViewById(R.id.subtitleEstado);
        navEstado.setText("México");

        if(Constantes.online==1){
            fota = "https://cnencuestas.io/"+Constantes.foto;
        }else {
            fota = String.valueOf(R.drawable.logofincircle);
        }
        Picasso.get()
                .load(fota)
                .error(R.drawable.logofincircle)
                .resize(120,120)
                .centerCrop()
                .into(perfil);


        if(Constantes.online==1){
            try {
                Constantes.ID_USER_NIVEL = Integer.parseInt(cyf.decrypt(Constantes.ID_Sistema_Nivel_Crypt));
                // Log.e("AJA",cyf.decrypt(Constantes.ID_Sistema_Nivel_Crypt));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        Menu nav_Menu = navigationView.getMenu();

        try{
            switch (Integer.parseInt(Constantes.BETA_TESTER)){
                case 1:
                    if(Constantes.online==0){
                        nav_Menu.findItem(R.id.off).setVisible(true);//CAPTURA OFFLINE
                        nav_Menu.findItem(R.id.mainCapter).setVisible(false);//CAPTURA ONLINE
                    }else if(Constantes.online==1){
                        nav_Menu.findItem(R.id.off).setVisible(false); //CAPTURA OFFLINE
                        nav_Menu.findItem(R.id.asiste).setVisible(true); //ASISTENCIA QR
                        nav_Menu.findItem(R.id.redes).setVisible(true); //REDES SOCIALES
                        nav_Menu.findItem(R.id.Carteo).setVisible(true); //CARTEO
                        nav_Menu.findItem(R.id.prep).setVisible(true); //PREP
                    }
                    break;

                case 0:
                    if(Constantes.online==0){

                    }else if(Constantes.online==1){
                        nav_Menu.findItem(R.id.asiste).setVisible(true);
                        nav_Menu.findItem(R.id.mainCapter).setVisible(true);
                        nav_Menu.findItem(R.id.Carteo).setVisible(true); //CARTEO
                        nav_Menu.findItem(R.id.prep).setVisible(true); //PREP
                        nav_Menu.findItem(R.id.redes).setVisible(true); //REDES SOCIALES
                    }
                default:
                    break;
            }
        }catch (Exception e){

        }

        nav_Menu.findItem(R.id.nav_home).setChecked(true);

        //CONTROL USUARIOS
        /*switch (Constantes.ID_USER_NIVEL){

            case 100:
                nav_Menu.findItem(R.id.nav_rutas).setVisible(true);
                nav_Menu.findItem(R.id.nav_secciones).setVisible(true);
                nav_Menu.findItem(R.id.nav_calles).setVisible(true);
                break;

            case 101:
                nav_Menu.findItem(R.id.nav_secciones).setVisible(true);
                nav_Menu.findItem(R.id.nav_calles).setVisible(true);
                break;

            case 102:
                nav_Menu.findItem(R.id.nav_calles).setVisible(true);
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

                nav_Menu.findItem(R.id.menu_zonas).setVisible(true);
                nav_Menu.findItem(R.id.nav_rutas).setVisible(true);
                nav_Menu.findItem(R.id.nav_secciones).setVisible(true);
                nav_Menu.findItem(R.id.nav_calles).setVisible(true);

                break;

        }

         */
    }

    private void avisoProvacidad() {

        AlertDialog.Builder alrtas = new AlertDialog.Builder(this);
        alrtas.setTitle("AVISO DE  PRIVACIDAD");
        alrtas.setMessage("Al aceptar los términos de privacidad da acceso a la ubicación, a la camara, a los archivos y almacenamiento de datos, así mismo la recopilación de información tomada por medio de la captura OCR, de la misma forma se acepta no tener acceso a la información recopilada por método de captura, al aceptar la ubicación recopila tan solo el lugar de captura y el recorrido realizado por medio de la captura, no se infringe con las privacidades por Google(SMS, intromisión a apps ajenas al SIE), así mismo no se recopila información con fines de mercado (compras, recomendaciones, etc.).\n" +
                "Con respecto a la información personal retomada de la persona se tiene que leer con total atención el mensaje de privacidad de datos, tales como no usarlos con fines, comerciales, económicos, o distribución de los mismos, se recopilan sin fin de lucro bajo el consentimiento puntual de la persona, por lo tanto la firma es obligatoria para realizar la captura.\n");
        alrtas.setNegativeButton("Denegar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();

            }
        });
        alrtas.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                SQLiteDatabase db = conn.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put("name", Constantes.name);
                values.put("email", Constantes.user);
                values.put("pass", Constantes.pass);
                values.put("beta", Constantes.BETA_TESTER);
                values.put("nivelU", Constantes.ID_USER_NIVEL);
                long newRowId = db.insert("t_usuarios", null, values);
                db.close();
                bienvenida();
            }
        });

        AlertDialog alertota = alrtas.create();
        alertota.show();

    }

    private void bienvenida() {

        new Flashbar.Builder(this)
                .gravity(Flashbar.Gravity.BOTTOM)
                .title("¡Hola!")
                .message("Gracias por usar el SIE "+ mNombre + " Desliza para descartar")
                .enterAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(750)
                        .alpha()
                        .overshoot())
                .exitAnimation(FlashAnim.with(this)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .showIcon()
                .enableSwipeToDismiss()
                .build()
                .show();

    }

    private void comprobarPASS() {

        try {
            String psas = cyf.decrypt(Constantes.SIE_NewPass);
            int pus = Integer.parseInt(psas);
            if(pus==1){

                mostrarCambio();

            }else if(pus==0){

            }else {
                Toast.makeText(getApplicationContext(), "Codigo Sin Control "+psas, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void mostrarCambio() {



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.homebar, menu);
        if(Constantes.online==1){
            menu.findItem(R.id.action_sync).setVisible(true);
        }else {
            menu.findItem(R.id.action_sync).setVisible(false);
            menu.findItem(R.id.action_pass).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_user:
                bienvenida();
                break;
            case R.id.action_exit:
                finishAffinity();
                break;
            case R.id.action_pass:
                Toast.makeText(getApplicationContext(), "Cambie Su contraseña en la pagina WEB", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_sync:
                finish();
                Constantes.online=0;
                Intent off = new Intent(this,homebar.class);
                startActivity(off);
                break;
            default:

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void obtenerUbica() {

        if(gps.canGetLocation()){
            gps.getLocation();
            Constantes.Clatitude = Double.toString(gps.getLatitude());
            Constantes.Clongitude = Double.toString(gps.getLongitude());
            ejecutarHilo();
        }else {
            AlertDialog.Builder contac = new AlertDialog.Builder(this);
            contac.setMessage("ACTIVE LA UBICACIÓN Y VUELVA A INICIAR")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent);
                            finish();
                        }
                    });
            contac.setCancelable(false);
            contac.create();
            contac.show();
        }
    }

    private void ejecutarHilo() {

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mytask = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] objects) {

                        new Handler (Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {

                                gps.getLocation();
                                try {
                                    Constantes.Clatitude = cyf.encrypt(Double.toString(gps.getLatitude()));
                                    Constantes.Clongitude = cyf.encrypt(Double.toString(gps.getLongitude()));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mandarUbica(Constantes.Clongitude,  Constantes.Clatitude);
                            }
                        });
                        return null;
                    }
                };
                mytask.execute();
            }
        };
        timer.schedule(task,0,3000);
}

    private void mandarUbica(String Lon, String Lati) {

        String fecha = null;
        String hora = null;
        try {
            fecha = cyf.encrypt(obtenerFechaConFormato("yyyy-MM-dd"));
            hora = cyf.encrypt(obtenerFechaConFormato("HH:mm:ss"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = Constantes.SERVER+APIUBICA; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

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
        multiPartRequestWithParams.addStringParam("Lat_Cy", Lati);
        multiPartRequestWithParams.addStringParam("Long_Cy", Lon);
        multiPartRequestWithParams.addStringParam("Dia", fecha);
        multiPartRequestWithParams.addStringParam("Hora", hora);

        queue = Volley.newRequestQueue(getApplicationContext());
        if (queue == null)
            queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });

    }

    @SuppressLint("SimpleDateFormat")
    public static String obtenerFechaConFormato(String formato) {
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        SimpleDateFormat sdf;
        sdf = new SimpleDateFormat(formato);
        sdf.setTimeZone(TimeZone.getTimeZone("America/Mexico_City"));
        return sdf.format(date);
    }

    private void comprobarUpdate() {

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

                                SweetAlertDialog upa = new SweetAlertDialog(homebar.this, SweetAlertDialog.WARNING_TYPE);
                                upa.setTitleText("¡ACTUALIZACIÓN!");
                                upa.setCancelable(false);
                                upa.setContentText("HAY UNA VERSIÓN DISPONIBLE, POR FAVOR ACTUALIZE");
                                upa.setConfirmText("Actualizar Ahora");
                                upa.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {

                                                sDialog.dismissWithAnimation();

                                                Uri uri = Uri.parse("https://cnencuestas.io/"+Url);
                                                Intent i = new Intent(Intent.ACTION_VIEW, uri);
                                                startActivity(i);

                                                finishAffinity();

                                            }
                                        });
                                upa.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            JSONObject dataV = new JSONObject(response);;
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


        if (queue == null)
            queue = Volley.newRequestQueue(getApplicationContext());

        queue.add(multiPartRequestWithParams);

    }

}