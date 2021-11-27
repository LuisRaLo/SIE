package cne.heba.sie.menu.Captura.Ocr;

import android.Manifest;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.pedant.SweetAlert.SweetAlertDialog;
import cne.heba.sie.R;
import cne.heba.sie.menu.Captura.Step.controlCap;
import cne.heba.sie.util.Constantes;


public class ocrUp extends AppCompatActivity {

    ImageButton frenteBtn;
    Button subida;
    private Uri photoURI1;
    private String mCurrentPhotoPath;
    public int a;
    String upF;

    String APICURP = "Android_Comprobacion_Ants_CURP";
    String APICLAVE = "Android_Comprobacion_Ants_ClaveElec";

    //cargandoDialogo load = new cargandoDialogo(ocrUp.this);

    SweetAlertDialog cargandoD,errorD;

    String textoOCR,message,ocr,clv,crp,seccelec,vigencia,emision,nombre,ap,am;

    public static final int REQUEST_CODE_TAKE_PHOTO = 0 /*1*/;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr_up);

        frenteBtn = findViewById(R.id.frontImg);

        frenteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFotoF();
            }
        });


    }

    private void subirImg() {

        cargandoD = new SweetAlertDialog(ocrUp.this, SweetAlertDialog.PROGRESS_TYPE);

        errorD = new SweetAlertDialog(ocrUp.this, SweetAlertDialog.ERROR_TYPE);

        //load.startLoadingDialog();

        errorD.setTitleText("Error");
        errorD.setContentText("No pudimos obtener los datos, intentalo de nuevo, si el problema persiste use Captura Express");

        cargandoD.getProgressHelper().setBarColor(Color.parseColor("#ec407a"));
        cargandoD.setCancelable(false);
        cargandoD.setTitleText("Procesando");
        cargandoD.setContentText("Tardara dependiendo tu conexión");
        cargandoD.show();

        /*

        new SweetAlertDialog(getApplicationContext(), SweetAlertDialog.PROGRESS_TYPE)
                .setTitleText("Procesando")
                .setContentText("El proceso tardara dependiendo su conexión")
                .show();

         */

        Log.e("up", "subiendo..."+ " TK: "+ Constantes.token+ " ID: "+ Constantes.id_act);

        String url = "https://189.240.232.89/api/Android/APIS/Lector_OCR"; // Url de el servidor
        SimpleMultiPartRequest multiPartRequestWithParams = new SimpleMultiPartRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("Resp OCR",response);
                        try {
                            JSONObject data = new JSONObject(response);
                            if(data.getInt("success")==1){
                                message = data.getString("message");
                                textoOCR = data.getString("textoocr");

                                cargandoD.dismissWithAnimation();

                                //load.dismissDialog();

                                //MENU DESPLEGABLE EN DESARROLLO
                                /*menuDesplegable menu = new menuDesplegable();
                                menu.show(getSupportFragmentManager(),"TAG");

                                 */

                                JSONObject dataR = data.getJSONObject("BusquedadeTexto");
                                Constantes.OCR = dataR.getString("OCR");
                                Constantes.SECCION = dataR.getString("SeccionElectoral");
                                Constantes.VIGENCIA = dataR.getString("Vigencia");
                                Constantes.EMIS = dataR.getString("Emision");
                                Constantes.CLAVEL = dataR.getString("ClaveElector");
                                JSONObject dataCURP = dataR.getJSONObject("Sit_CURP");
                                if(dataCURP.getBoolean("success")){
                                    Constantes.INCUR=1;
                                    Constantes.NOAPMA=1;
                                    Constantes.CURP = dataCURP.getString("CURP");
                                    Constantes.AP = dataCURP.getString("APATERNO");
                                    Constantes.AM = dataCURP.getString("AMATERNO");
                                    Constantes.NOMBRE = dataCURP.getString("NOMBRE");
                                }

                                JSONObject dataINE = dataR.getJSONObject("Sit_ClaveElector");

                                Constantes.mensajeMilit=dataINE.getString("mensaje");
                                Constantes.codigoMilit=dataINE.getString("codigo");
                                Constantes.nombreMilit=dataINE.getString("nombre");
                                Constantes.apMilit = dataINE.getString("apellidoPaterno");
                                Constantes.amMilit = dataINE.getString("apellidoMaterno");
                                Constantes.estadoMilit = dataINE.getString("nombreEstado");
                                Constantes.partidoMilit = dataINE.getString("nombrePartido");
                                Constantes.fechMilit = dataINE.getString("fechaAfiliacion");

                                Constantes.eventoSalida=1;

                                Intent lanzaCAP = new Intent(ocrUp.this, controlCap.class);
                                startActivity(lanzaCAP);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                cargandoD.dismissWithAnimation();

                errorD.show();

            }
        });
        multiPartRequestWithParams.setRetryPolicy(new DefaultRetryPolicy(Constantes.MY_DEFAULT_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        multiPartRequestWithParams.addStringParam("ID_User", Constantes.id_act);
        multiPartRequestWithParams.addStringParam("Token", Constantes.token);

        //AÑADIR IMAGES
        multiPartRequestWithParams.addFile("Ruta1", upF);

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(multiPartRequestWithParams);


    }


    private void tomarFotoF() {

        if (ContextCompat.checkSelfPermission(ocrUp.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(ocrUp.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(ocrUp.this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                ActivityCompat.requestPermissions(ocrUp.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        225);
            }


            if (ActivityCompat.shouldShowRequestPermissionRationale(ocrUp.this,
                    Manifest.permission.CAMERA)) {

            } else {
                ActivityCompat.requestPermissions(ocrUp.this,
                        new String[]{Manifest.permission.CAMERA},
                        226);
            }
        } else {
            dispatchTakePictureIntent();
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(ocrUp.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.TITLE, "MyPicture");
                values.put(MediaStore.Images.Media.DESCRIPTION, "Photo taken on " + System.currentTimeMillis());
                photoURI1 = ocrUp.this.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                //Uri photoURI = FileProvider.getUriForFile(AddActivity.this, "com.example.android.fileprovider", photoFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI1);
                startActivityForResult(takePictureIntent, REQUEST_CODE_TAKE_PHOTO);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = ocrUp.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".sie",         /* suffix */
                storageDir      /* directory */
        );


        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(ocrUp.this.getContentResolver(), photoURI1);

                    frenteBtn.setBackground(null);
                    //frenteBtn.setImageBitmap(bitmap);
                    upF = getPath(ocrUp.this,photoURI1);

                    subirImg();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /*if (requestCode == REQUEST_CODE_TAKE_PHOTO && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras(); // Aquí es null
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                mPhotoImageView.setImageBitmap(imageBitmap);
            }*/

        }
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


}