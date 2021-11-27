package cne.heba.sie.menu.Captura.Step;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.MarkerCanva.Lienzo;
import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;
import cne.heba.sie.util.Cypher;
import cne.heba.sie.util.NukeSSLCerts;

import static cne.heba.sie.util.Constantes.firmaPath;


public class firmaInit extends AppCompatActivity {
    private Context TheThis;
    private String NombreFolder = "/Captura/Firma/";
    private String NombreFirm = null, NombreFirmFinal=null;
    String subcarp;
    Button saver,lim;
    TextView titulo5, subtit5;
    String TipoCaptura;


    //CONSTANTES IDES

    /*
      multiPartRequestWithParams.addStringParam("ID_Estatal", "15");
        multiPartRequestWithParams.addStringParam("ID_Regional", "1");
        multiPartRequestWithParams.addStringParam("ID_Seccional", "11353");
     */

    private static Lienzo lienzo;

    SweetAlertDialog pDialog;

    String APIUP = "api/Android/APIS/AltaContacto";

    Cypher cyf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_firma_init);

        lienzo = (Lienzo)findViewById(R.id.lienzo);

        saver=findViewById(R.id.sav);
        lim=findViewById(R.id.limpis);
        titulo5= findViewById(R.id.titlePriva);
        subtit5= findViewById(R.id.subtit6);

//Log.e("Constante SALIDA", String.valueOf(Constantes.eventoSalida));

        new NukeSSLCerts().nuke();

        cyf = new Cypher();

        try {
            cyf.AESCrypt();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Constantes.Control_Nivel_ID=cyf.encrypt("14");
        } catch (Exception e) {
            e.printStackTrace();
        }


        lim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               lienzo.NuevoDibujo();
            }
        });

        if(Constantes.eventoSalida==2){
            try {
                TipoCaptura = cyf.encrypt("10");
                Constantes.CLAVEL=cyf.encrypt(Constantes.CLAVEL);
                Constantes.CURP=cyf.encrypt(Constantes.CURP);
                Constantes.AP=cyf.encrypt(Constantes.AP);
                Constantes.AM=cyf.encrypt(Constantes.AM);
                Constantes.NOMBRE=cyf.encrypt(Constantes.NOMBRE);
                Constantes.codigoMilit=cyf.encrypt(Constantes.codigoMilit);
                Constantes.estadoMilit=cyf.encrypt(Constantes.estadoMilit);
                Constantes.partidoMilit=cyf.encrypt(Constantes.partidoMilit);
                Constantes.fechMilit=cyf.encrypt(Constantes.fechMilit);
                Constantes.mensajeMilit=cyf.encrypt(Constantes.mensajeMilit);
                Constantes.IF_ID_ACT=cyf.encrypt(Constantes.IF_ID_ACT);
                Constantes.EsAntorchista=cyf.encrypt("1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(Constantes.eventoSalida==1){
            try {
                TipoCaptura = cyf.encrypt("10");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(Constantes.eventoSalida==0){
            try {
                TipoCaptura = cyf.encrypt("20");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        saver.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if(Objects.equals(Constantes.D1,null)){
                    try {
                        Constantes.D1=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(Objects.equals(Constantes.D2,null)){
                    try {
                        Constantes.D2=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(Objects.equals(Constantes.D3,null)){
                    try {
                        Constantes.D3=cyf.encrypt("0");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(Constantes.eventoSalida==2){

                    int numero = (int) (Math.random() * 421) + 1;
                    NombreFirmFinal="FiAn"+numero;
                    guardarFirma(NombreFirmFinal);
                    apiEdicion();

                    pDialog = new SweetAlertDialog(firmaInit.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Registrando...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                }else {

                    int numero = (int) (Math.random() * 241) + 1;
                    NombreFirmFinal="Fi"+numero;
                    guardarFirma(NombreFirmFinal);
                    darAlta(firmaPath);

                    pDialog = new SweetAlertDialog(firmaInit.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    pDialog.setTitleText("Registrando...");
                    pDialog.setCancelable(false);
                    pDialog.show();

                }

            }
        });


    }


    private void apiEdicion() {

        //muestraData();

        String url = "https://189.240.232.89/api/Android/APIS/Editar_Contacto"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Log.e("Resp FIRMA",response);
                        try {
                            JSONObject r = new JSONObject(response);
                            if(r.getInt("success")==1){

                                String altNombre = null;
                                try {
                                    altNombre = cyf.decrypt(Constantes.NOMBRE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                pDialog.dismissWithAnimation();
                                new SweetAlertDialog(firmaInit.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("¡Exito!")
                                        .setContentText("Se dio de alta a "+ altNombre)
                                        .setConfirmText("Ok ¡Gracias!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .show();

                            }else{

                                Toast.makeText(TheThis, r.getString("message"), Toast.LENGTH_SHORT).show();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismissWithAnimation();
                new SweetAlertDialog(firmaInit.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("¡Oops!")
                        .setContentText("Tuvimos un pequeño problema, vuelve a intentarlo por favor :3.")
                        .setConfirmText("Vale :(")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();
            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);
        multiPartRequestWithParams.addStringParam("ClaveElector", Constantes.CLAVEL);
        multiPartRequestWithParams.addStringParam("CURP", Constantes.CURP);
        multiPartRequestWithParams.addStringParam("APaterno", Constantes.AP);
        multiPartRequestWithParams.addStringParam("AMaterno", Constantes.AM);
        multiPartRequestWithParams.addStringParam("Nombre", Constantes.NOMBRE);
        multiPartRequestWithParams.addStringParam("TipoVialidad", Constantes.TIPOVIAL);
        multiPartRequestWithParams.addStringParam("NombreVialidad", Constantes.NOMBREVIAL);
        multiPartRequestWithParams.addStringParam("MzoNe", Constantes.MZ);
        multiPartRequestWithParams.addStringParam("LtoNi", Constantes.LTE);
        multiPartRequestWithParams.addStringParam("Colonia", Constantes.COLONIA);
        multiPartRequestWithParams.addStringParam("CP", Constantes.CP);
        multiPartRequestWithParams.addStringParam("Municipio_Domicilio", Constantes.MUNICDOM);
        multiPartRequestWithParams.addStringParam("Entidad_Domicilio", Constantes.ENTIDOM);
        multiPartRequestWithParams.addStringParam("SeccionElectoral", Constantes.SECCION);
        multiPartRequestWithParams.addStringParam("Emision", Constantes.EMIS);
        multiPartRequestWithParams.addStringParam("Vigencia", Constantes.VIGENCIA);
        multiPartRequestWithParams.addStringParam("CIC", Constantes.CIC);
        multiPartRequestWithParams.addStringParam("OCR", Constantes.OCR);
        multiPartRequestWithParams.addStringParam("Longitud", Constantes.Clongitude);
        multiPartRequestWithParams.addStringParam("Latitud", Constantes.Clatitude);
        multiPartRequestWithParams.addStringParam("Telefono", Constantes.TEL);
        multiPartRequestWithParams.addStringParam("StatusF", Constantes.PFACE);
        multiPartRequestWithParams.addStringParam("StatusT", Constantes.PTWIT);
        multiPartRequestWithParams.addStringParam("nameFace", Constantes.FACE);
        multiPartRequestWithParams.addStringParam("nameTwit", Constantes.TWTT);
        multiPartRequestWithParams.addStringParam("militancia_code", Constantes.codigoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Estado", Constantes.estadoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Partido", Constantes.partidoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Afiliacion", Constantes.fechMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Messages", Constantes.mensajeMilit);

        //ESTRUCTURA

        multiPartRequestWithParams.addStringParam("TipoCaptura", TipoCaptura);
        multiPartRequestWithParams.addStringParam("ID_CatalogoNiveles_Activismo", Constantes.Control_Nivel_ID);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);
        multiPartRequestWithParams.addStringParam("ID_Estatal", Constantes.ID_Estatal);
        multiPartRequestWithParams.addStringParam("ID_Seccional", Constantes.ID_Seccional);
        multiPartRequestWithParams.addStringParam("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);  //DEPENDIENDO DE LO QUE SE SELECCIONE
        multiPartRequestWithParams.addStringParam("ID_Subequipo", Constantes.ID_Subequipo);
        multiPartRequestWithParams.addStringParam("ID_INE_ListaNominal", Constantes.ID_INE_ListaNominal);
        multiPartRequestWithParams.addStringParam("ID_Calle", Constantes.ID_Calle);
        multiPartRequestWithParams.addStringParam("ID_Responsable", Constantes.id_act); //PENDIENTE
        multiPartRequestWithParams.addStringParam("D1", Constantes.D1);
        multiPartRequestWithParams.addStringParam("D2", Constantes.D2);
        multiPartRequestWithParams.addStringParam("D3", Constantes.D2);

        multiPartRequestWithParams.addStringParam("EsAntorchista", Constantes.EsAntorchista);
        multiPartRequestWithParams.addStringParam("ID_Antorchista", Constantes.IF_ID_ACT);

        multiPartRequestWithParams.addFile("FirmaConset", firmaPath);

        if(Constantes.RutaAbsolutaFrente!=null)
        multiPartRequestWithParams.addFile("Ine_Anv", Constantes.RutaAbsolutaFrente);

        if(Constantes.RutaAbsolutaFrente!=null)
        multiPartRequestWithParams.addFile("Ine_Rev", Constantes.RutaAbsolutaAtras);


        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(multiPartRequestWithParams);


    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void guardarFirma(String nombre) {

        //Salvar dibujo
        lienzo.setDrawingCacheEnabled(true);
        SaveImage(getApplicationContext(), lienzo.getDrawingCache(), nombre);
        lienzo.destroyDrawingCache();
        firmaPath = getPath(getApplicationContext(), Constantes.URIMG);

    }

    public void SaveImage(Context context, Bitmap ImageToSave, String nombre) {

        TheThis = context;

        String file_path;

        file_path = Environment.getExternalStorageDirectory().getAbsolutePath() + NombreFolder;

        //String CurrentDateAndTime = getCurrentDateAndTime();
        File dir = new File(file_path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, nombre + ".sie");

        try {
            FileOutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
                Constantes.URIMG = Uri.fromFile(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            ImageToSave.compress(Bitmap.CompressFormat.PNG, 85, fOut);
            fOut.flush();
            fOut.close();
            MakeSureFileWasCreatedThenMakeAvabile(file);
            AbleToSave();
        }

        catch(FileNotFoundException e) {
            UnableToSave();
        }
        catch(IOException e) {
            UnableToSave();
        }

    }

    private void MakeSureFileWasCreatedThenMakeAvabile(File file){
        MediaScannerConnection.scanFile(TheThis,
                new String[] { file.toString() } , null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
    }


    private void UnableToSave() {
        Toast.makeText(TheThis, "Ocurrio Un Error, Intente de Nuevo", Toast.LENGTH_SHORT).show();

    }

    private void AbleToSave() {
        Toast.makeText(TheThis, "Registrando Espere Por Favor", Toast.LENGTH_SHORT).show();
    }



    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };
//COMENTARIO DE UPDATE
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }


    private void darAlta(String firmaPath) {

        //muestraData();

        String url = "https://189.240.232.89/api/Android/APIS/AltaContacto"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       // Log.e("Resp FIRMA",response);
                        try {
                            JSONObject r = new JSONObject(response);
                            if(r.getInt("success")==1){

                                String altNombre = null;
                                try {
                                    altNombre = cyf.decrypt(Constantes.NOMBRE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                pDialog.dismissWithAnimation();
                                new SweetAlertDialog(firmaInit.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("¡Exito!")
                                        .setContentText("Se dio de alta a "+ altNombre)
                                        .setConfirmText("Ok ¡Gracias!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .show();
                            }else{

                                Toast.makeText(TheThis, r.getString("message"), Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                pDialog.dismissWithAnimation();
                new SweetAlertDialog(firmaInit.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("¡Oops!")
                        .setContentText("Tuvimos un pequeño problema, vuelve a intentarlo por favor :3.")
                        .setConfirmText("Vale :(")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                finish();
                            }
                        })
                        .show();

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);
        multiPartRequestWithParams.addStringParam("ClaveElector", Constantes.CLAVEL);
        multiPartRequestWithParams.addStringParam("CURP", Constantes.CURP);
        multiPartRequestWithParams.addStringParam("APaterno", Constantes.AP);
        multiPartRequestWithParams.addStringParam("AMaterno", Constantes.AM);
        multiPartRequestWithParams.addStringParam("Nombre", Constantes.NOMBRE);

        multiPartRequestWithParams.addStringParam("SeccionElectoral", Constantes.SECCION);
        multiPartRequestWithParams.addStringParam("Emision", Constantes.EMIS);
        multiPartRequestWithParams.addStringParam("Vigencia", Constantes.VIGENCIA);
        multiPartRequestWithParams.addStringParam("CIC", Constantes.CIC);
        multiPartRequestWithParams.addStringParam("OCR", Constantes.OCR);
        multiPartRequestWithParams.addStringParam("Longitud", Constantes.Clongitude);
        multiPartRequestWithParams.addStringParam("Latitud", Constantes.Clatitude);
        multiPartRequestWithParams.addStringParam("Telefono", Constantes.TEL);
        multiPartRequestWithParams.addStringParam("StatusF", Constantes.PFACE);
        multiPartRequestWithParams.addStringParam("StatusT", Constantes.PTWIT);
        multiPartRequestWithParams.addStringParam("nameFace", Constantes.FACE);
        multiPartRequestWithParams.addStringParam("nameTwit", Constantes.TWTT);
        multiPartRequestWithParams.addStringParam("militancia_code", Constantes.codigoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Estado", Constantes.estadoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Partido", Constantes.partidoMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Afiliacion", Constantes.fechMilit);
        multiPartRequestWithParams.addStringParam("Militancia_Messages", Constantes.mensajeMilit);

        //ESTRUCTURA

        multiPartRequestWithParams.addStringParam("TipoCaptura", TipoCaptura);
        multiPartRequestWithParams.addStringParam("ID_CatalogoNiveles_Activismo", Constantes.Control_Nivel_ID);
        multiPartRequestWithParams.addStringParam("ID_Regional", Constantes.ID_Regional);
        multiPartRequestWithParams.addStringParam("ID_Estatal", Constantes.ID_Estatal);
        multiPartRequestWithParams.addStringParam("ID_Seccional", Constantes.ID_Seccional);
        multiPartRequestWithParams.addStringParam("ID_Equipo_Detalle", Constantes.ID_Equipo_Detalle);  //DEPENDIENDO DE LO QUE SE SELECCIONE
        multiPartRequestWithParams.addStringParam("ID_Subequipo", Constantes.ID_Subequipo);
        multiPartRequestWithParams.addStringParam("ID_INE_ListaNominal", Constantes.ID_INE_ListaNominal);
        multiPartRequestWithParams.addStringParam("ID_Organismo", Constantes.ID_Organismo);
        multiPartRequestWithParams.addStringParam("ID_Calle", Constantes.ID_Calle);
        multiPartRequestWithParams.addStringParam("ID_Responsable", Constantes.id_act); //PENDIENTE
        multiPartRequestWithParams.addStringParam("D1", Constantes.D1);
        multiPartRequestWithParams.addStringParam("D2", Constantes.D2);
        multiPartRequestWithParams.addStringParam("D3", Constantes.D2);

        if(firmaPath!=null){

            multiPartRequestWithParams.addFile("FirmaConset", firmaPath);
            Log.e("FIRMA","SI AÑADO LA FIRMA :c");

        }


        if(Constantes.RutaAbsolutaFrente!=null)
        multiPartRequestWithParams.addFile("Ine_Anv", Constantes.RutaAbsolutaFrente);

        if(Constantes.RutaAbsolutaAtras!=null)
        multiPartRequestWithParams.addFile("Ine_Rev", Constantes.RutaAbsolutaAtras);


        RequestQueue queue = Volley.newRequestQueue(firmaInit.this);
        queue.add(multiPartRequestWithParams);


    }



    private void muestraData() {

        try {

            /*
            //LISTO SIEMPRE DATO
            System.out.println("id_user: " + Constantes.id_act);
            //LISTO SIEMPRE DATO
            System.out.println("Token: " + Constantes.token);
            //OPCIONAL
            System.out.println("ClaveElector: " + Constantes.CLAVEL);
            //LISTO SIEMPRE DATO
            System.out.println("CURP: " + Constantes.CURP);
            //LISTO SIEMPRE DATO
            System.out.println("APaterno: " + Constantes.AP);
            //LISTO SIEMPRE DATO
            System.out.println("AMaterno: " + Constantes.AM);
            //LISTO SIEMPRE DATO
            System.out.println("Nombre: " + Constantes.NOMBRE);
            //DATOS OFFILINE
            System.out.println("TipoVialidad: " + Constantes.TIPOVIAL);
            System.out.println("NombreVialidad: " + Constantes.NOMBREVIAL);
            System.out.println("MzoNe: " + Constantes.MZ);
            System.out.println("LtoNi: " + Constantes.LTE);
            System.out.println("Colonia: " + Constantes.COLONIA);
            System.out.println("CP: " + Constantes.CP);
            System.out.println("Municipio_Domicilio: " + Constantes.MUNICDOM);
            System.out.println("Entidad_Domicilio: " + Constantes.ENTIDOM);

            //LISTO SIEMPRE DATO
            System.out.println("SeccionElectoral: " + Constantes.SECCION);
            //OPCIONAL
            System.out.println("Emision: " + Constantes.EMIS);
            //OPCIONAL
            System.out.println("Vigencia: " + Constantes.VIGENCIA);
            //OPCIONAL
            System.out.println("CIC: " + Constantes.CIC);
            //OPCIONAL
            System.out.println("OCR: " + Constantes.OCR);
            //LISTO SIEMPRE DATO
            System.out.println("Longitud: " + Constantes.Clongitude);
            //LISTO SIEMPRE DATO
            System.out.println("Latitud: " + Constantes.Clatitude);
            //LISTO SIEMPRE DATO
            System.out.println("Telefono: " + Constantes.TEL);
            //OPCIONAL
            System.out.println("StatusF: " + Constantes.PFACE);
            //OPCIONAL
            System.out.println("StatusT: " + Constantes.PTWIT);
            //OPCIONAL
            System.out.println("nameFace: " + Constantes.FACE);
            //OPCIONAL
            System.out.println("nameTwit: " + Constantes.TWTT);
            //LISTO SIEMPRE DATO
            System.out.println("militancia_code: " + Constantes.codigoMilit);
            //OPCIONAL
            System.out.println("Militancia_Estado: " + Constantes.estadoMilit);
            //OPCIONAL
            System.out.println("Militancia_Partido: " + Constantes.partidoMilit);
            //OPCIONAL
            System.out.println("Militancia_Afiliacion: " + Constantes.fechMilit);
            //LISTO SIEMPRE DATO
            System.out.println("Militancia_Messages: " + Constantes.mensajeMilit);


            System.out.println("ID_Estatal: "+ Constantes.ID_Estatal);
            System.out.println("ID_Regional: "+ Constantes.ID_Regional);
            System.out.println("ID_Seccional: "+ Constantes.ID_Seccional);

            //LISTO SIEMPRE DATO
            System.out.println("FirmaConset: " + firmaPath);
            //OPCIONAL
            System.out.println("Ine_Anv: " + Constantes.RutaAbsolutaFrente);
            //OPCIONAL
            System.out.println("Ine_Rev: " + Constantes.RutaAbsolutaAtras);

             */

            System.out.println( " ID_User " + Constantes.id_act);
            System.out.println( " Token " + Constantes.token);
            System.out.println( " ClaveElector " + Constantes.CLAVEL);
            System.out.println( " CURP " + Constantes.CURP);
            System.out.println( " APaterno " + Constantes.AP);
            System.out.println( " AMaterno " + Constantes.AM);
            System.out.println( " Nombre " + Constantes.NOMBRE);
            /*
            System.out.println( " TipoVialidad " + Constantes.TIPOVIAL);
            System.out.println( " NombreVialidad " + Constantes.NOMBREVIAL);
            System.out.println( " MzoNe " + Constantes.MZ);
            System.out.println( " LtoNi " + Constantes.LTE);
            System.out.println( " Colonia " + Constantes.COLONIA);
            System.out.println( " CP " + Constantes.CP);
            System.out.println( " Municipio_Domicilio " + Constantes.MUNICDOM);
            System.out.println( " Entidad_Domicilio " + Constantes.ENTIDOM);
             */
            System.out.println( " SeccionElectoral " + Constantes.SECCION);
            System.out.println( " Emision " + Constantes.EMIS);
            System.out.println( " Vigencia " + Constantes.VIGENCIA);
            System.out.println( " CIC " + Constantes.CIC);
            System.out.println( " OCR " + Constantes.OCR);
            System.out.println( " Longitud " + Constantes.Clongitude);
            System.out.println( " Latitud " + Constantes.Clatitude);
            System.out.println( " Telefono " + Constantes.TEL);
            System.out.println( " StatusF " + Constantes.PFACE);
            System.out.println( " StatusT " + Constantes.PTWIT);
            System.out.println( " nameFace " + Constantes.FACE);
            System.out.println( " nameTwit " + Constantes.TWTT);
            System.out.println( " militancia_code " + Constantes.codigoMilit);
            System.out.println( " Militancia_Estado " + Constantes.estadoMilit);
            System.out.println( " Militancia_Partido " + Constantes.partidoMilit);
            System.out.println( " Militancia_Afiliacion " + Constantes.fechMilit);
            System.out.println( " Militancia_Messages " + Constantes.mensajeMilit);

            //ESTRUCTURA

            System.out.println( " TipoCaptura " + TipoCaptura);
            System.out.println( " ID_CatalogoNiveles_Activismo " + Constantes.Control_Nivel_ID);
            System.out.println( " ID_Regional " + Constantes.ID_Regional);
            System.out.println( " ID_Estatal " + Constantes.ID_Estatal);
            System.out.println( " ID_Seccional " + Constantes.ID_Seccional);
            System.out.println( " ID_Equipo_Detalle " + Constantes.ID_Equipo_Detalle);//DEPENDIENDO DE LO QUE SE SELECCIONE
            System.out.println( " ID_Subequipo " + Constantes.ID_Subequipo);
            System.out.println( " ID_INE_ListaNominal " + Constantes.ID_INE_ListaNominal);
            System.out.println( " ID_Organismo " + Constantes.ID_Organismo);
            System.out.println( " ID_Calle " + Constantes.ID_Calle);
            System.out.println( " ID_Responsable " + Constantes.id_act); //PENDIENTE
            System.out.println( " D1 " + Constantes.D1);
            System.out.println( " D2 " + Constantes.D2);
            System.out.println( " D3 " + Constantes.D3);

            System.out.println( " EsAntorchista " + Constantes.EsAntorchista);
           System.out.println( " ID_Antorchista " + Constantes.IF_ID_ACT);

            //AÑADIR IMAGES
            System.out.println( " FirmaConset " + firmaPath);

                System.out.println( " Ine_Anv " + Constantes.RutaAbsolutaFrente);

                System.out.println( " Ine_Rev " + Constantes.RutaAbsolutaAtras);

        }catch (Exception a){

        }

    }

}