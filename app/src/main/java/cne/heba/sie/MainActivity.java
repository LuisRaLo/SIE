package cne.heba.sie;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.abeh.DbHelper;
import cne.heba.sie.redes.checkaInternet;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;
import cne.heba.sie.util.NukeSSLCerts;
import cne.heba.sie.myUIX.cargando;
import cne.heba.sie.util.genTokn;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class  MainActivity extends AppCompatActivity {
    //METODOS DE LOGIN Y PERMISOS DE APP

    private EditText mUsername, mPasword;
    private Button mLoginBtn,mLoginBtnOff;

    int passs = 1;

    Cypher cyf;

    DbHelper conn;

    checkaInternet com;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsername =  findViewById(R.id.userT);
        mPasword =  findViewById(R.id.contrasena);
        mLoginBtn = findViewById(R.id.btningresar);
        mLoginBtnOff = findViewById(R.id.btningresarOff);

        //FUNCIONES OFFLINE-ONLINE
        validaPermisos();
        com = new checkaInternet();
        if(com.isOnline(getApplicationContext())){
            Constantes.online=1;
        }else{
            Constantes.online=0;
        }
        cyf = new Cypher();

        try {
            cyf.AESCrypt();

        } catch (Exception e) {
            e.printStackTrace();
        }

        new NukeSSLCerts().nuke();

        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Constantes.user = cyf.encrypt(mUsername.getText().toString());
                    Constantes.pass = cyf.encrypt(mPasword.getText().toString());
                    Constantes.token = cyf.encrypt(genTokn.cadenaAleatoria(9));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                iniciaSesion();
            }
        });

        mLoginBtnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    Constantes.user = cyf.encrypt(mUsername.getText().toString());
                    Constantes.pass = cyf.encrypt(mPasword.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                iniciaSesionOff();
            }
        });

    }

    private void iniciaSesionOff() {
        String usua = null,pass = null;

        conn = new DbHelper(this);

        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor u = db.rawQuery("SELECT email,pass,beta,name,nivelU FROM t_usuarios",null);
        if (u.moveToFirst()) {  //si ha devuelto 1 fila, vamos al primero (que es el unico)
           usua = u.getString(0);
           pass = u.getString(1);
           Constantes.BETA_TESTER=u.getString(2);
            try {
                Constantes.name=cyf.decrypt(u.getString(3));
                Constantes.ID_Sistema_Nivel_Crypt = u.getString(4);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(usua.equals(Constantes.user)){
               if(pass.equals(Constantes.pass)){
                   Constantes.online=0;
                   Intent start = new Intent(this,homebar.class);
                   startActivity(start);

               }else {
                   mPasword.setError("Contraseña Incorrecta");
                   mPasword.requestFocus();
               }
           }else {
               mUsername.setError("Usuario No Encontrado");
               mUsername.requestFocus();
           }
        } else {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("¡Atención!")
                    .setContentText("Debe iniciar Sesión con conexión a internet primero")
                    .setConfirmText("Vale")
                    .show();
            u.close();
            conn.close();
        }

    }

    private void iniciaSesion() {

        cargando loadAnim = new cargando();
        SweetAlertDialog a = loadAnim.carga(this,"Iniciando...");

            String url = Constantes.SERVER+"requestuser"; // Url de el servidor
            SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                           // Log.e("DATA LOG ", response);
                            try {
                                JSONObject datos = new JSONObject(response);
                                if (datos.getInt("success") == 1) {


                                    String datoso = datos.getString("dta");
                                    JSONObject datosoI = new JSONObject(datoso);
                                    Intent i = new Intent(getApplicationContext(), homebar.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); //esta es la linea nueva con la que evito los duplicados.
                                    startActivity(i);
                                    Constantes.online=1;

                                    loadAnim.dismo(a);

                                    Constantes.name = datosoI.getString("Nombre");
                                    Constantes.ap = datosoI.getString("APaterno");
                                    Constantes.am = datosoI.getString("AMaterno");
                                    Constantes.id_act = datosoI.getString("ID_Usuario_Crypt");
                                    Constantes.ID_Regional = datosoI.getString("ID_Regional");
                                    Constantes.ID_Estatal = datosoI.getString("ID_Estatal");
                                    Constantes.ID_Seccional = datosoI.getString("ID_Seccional");
                                    Constantes.ID_Municipio = datosoI.getString("ID_Municipio");
                                    Constantes.ID_Equipo_Detalle = datosoI.getString("ID_Equipo_Detalle");
                                    Constantes.ID_Subequipo = datosoI.getString("ID_Subequipo");
                                    Constantes.ID_INE_ListaNominal = datosoI.getString("ID_INE_ListaNominal");
                                    Constantes.ID_Organismo = datosoI.getString("ID_Organismo");
                                    Constantes.ID_Calle = datosoI.getString("ID_Calle");
                                    Constantes.EsAntorchista = datosoI.getString("EsAntorchista");
                                    Constantes.ID_Antorchista = datosoI.getString("ID_Antorchista");
                                    Constantes.STATUS = datosoI.getString("Status");
                                    Constantes.ID_Elecciones_Contactos_Cy = datosoI.getString("ID_Elecciones_Contactos_Cy");
                                    Constantes.ID_Usuario_Crypt = datosoI.getString("ID_Usuario_Crypt");
                                    //ESTE CONTROLA LA MAYORIA DE VISTAS
                                    Constantes.ID_Sistema_Nivel_Crypt = datosoI.getString("ID_Sistema_Nivel_Crypt");
                                    Constantes.Usuario_Crypt = datosoI.getString("Usuario_Crypt");
                                    Constantes.UltimoIniciodeSesion2_Crypt = datosoI.getString("UltimoIniciodeSesion2_Crypt");
                                    Constantes.SIE_NewPass = datosoI.getString("SIE_NewPass");
                                    Constantes.BETA_TESTER = cyf.decrypt(datosoI.getString("BETA_Tester"));
                                    Constantes.foto = cyf.decrypt(datosoI.getString("preferences_ImgUrl"));

                                } else {

                                    loadAnim.dismo(a);

                                    new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Tus credenciales son incorrectas")
                                            .show();


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    loadAnim.dismo(a);

                    Log.e("ERROR: ", Constantes.user+" : "+Constantes.pass);
                    if(com.isOnline(getApplicationContext())){
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Error en el servidor, informe con EDT")
                                .show();
                    }else {
                        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Oops...")
                                .setContentText("Sin Conexión a Internet")
                                .show();
                    }



                }
            });
            multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(15000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            multiPartRequestWithParams.addStringParam("user", Constantes.user);
            multiPartRequestWithParams.addStringParam("pwd", Constantes.pass);
            multiPartRequestWithParams.addStringParam("tkn", Constantes.token);

            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(multiPartRequestWithParams);

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                queue.getCache().clear();
            }
        });
        }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean validaPermisos() {

       // Log.e("Versión", String.valueOf(Build.VERSION.SDK_INT) + " "+ Build.VERSION_CODES.LOLLIPOP);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                //todo when permission is granted
            } else {
                //request for the permission

                new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("¡Atención!")
                        .setContentText("Android 11 pide permisos más estrictos, por favor habilitalo y vuelve a abrir la app.")
                        .setConfirmText("Claro")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                intent.setData(uri);
                                startActivity(intent);
                            }
                        })
                        .show();


            }
        }

        if (Build.VERSION.SDK_INT<=22){
            return true;
        }


        if ((checkSelfPermission(CAMERA)== PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED)){
            return true;
        }

        if ((shouldShowRequestPermissionRationale(CAMERA)) ||
                (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION) ||
                        (shouldShowRequestPermissionRationale(ACCESS_COARSE_LOCATION)))){

            cargarDialogoRecomendacion();

        }else {
            requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
        }

        return false;
    }



    private void solicitarPermisosManual() {

        AlertDialog.Builder constructor = new AlertDialog.Builder(this);
        constructor.setTitle("Permisos");
        constructor.setMessage("¿Confirgurar Permisos de Manera Manual?");

        constructor.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                Intent intent=new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri=Uri.fromParts("package",getPackageName(),null);
                intent.setData(uri);
                startActivity(intent);

            }
        });
        constructor.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, "La APP No funcionara de manera Correcta", Toast.LENGTH_SHORT).show();
            }
        });

        AlertDialog solicitPerm = constructor.create();
        solicitPerm.show();

    }

    private void cargarDialogoRecomendacion() {


        AlertDialog.Builder construye = new AlertDialog.Builder(this);
        construye.setTitle("Permisos Descativados");
        construye.setMessage("Debe aceptar los permisos para que la app funcione de manera correcta");

        construye.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE,CAMERA,ACCESS_FINE_LOCATION,ACCESS_COARSE_LOCATION},100);
            }
        });

        AlertDialog perReco = construye.create();
        perReco.show();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==100){

            if(grantResults.length==4 && grantResults[0]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[1]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[2]==PackageManager.PERMISSION_GRANTED &&
                    grantResults[3]==PackageManager.PERMISSION_GRANTED){
                //cargar.setEnabled(true);
            }else {
                solicitarPermisosManual();
            }

        }

    }

}