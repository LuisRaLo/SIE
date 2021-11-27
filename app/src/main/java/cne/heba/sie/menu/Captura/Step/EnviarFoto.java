package cne.heba.sie.menu.Captura.Step;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.File;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;


public class EnviarFoto extends Fragment {
    View vistaEnv;
    public int b, a;

    private String CARPETA_PRINCIPAL = "/Captura/Firma/";
    private String CARPETA_IN = "Maincra/";
    private String path;
    File fileImagen;
    Bitmap bitmapf,bitmapa;
    private String DIRECTORIO_IMAGEN = CARPETA_PRINCIPAL+CARPETA_IN;


    private static final int COD_FOTO=10;


    ImageView imgsendf, imgsendb;
    Button btnBuscarF, btnbuscarB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        vistaEnv = inflater.inflate(R.layout.fragment_enviar_foto, container, false);
        btnBuscarF = vistaEnv.findViewById(R.id.buscarf);
        //btnSubirF = vistaEnv.findViewById(R.id.subirF);
        btnbuscarB = vistaEnv.findViewById(R.id.buscarb);
        imgsendf = vistaEnv.findViewById(R.id.imagesendf);
        imgsendb = vistaEnv.findViewById(R.id.imgsendb);


        btnBuscarF.setOnClickListener(new View.OnClickListener() {
            @Override //buscarFront
            public void onClick(View view) {


                tomarFotoF();

                b = 1;


            }
        });

        btnbuscarB.setOnClickListener(new View.OnClickListener() {
            @Override //buscarBack
            public void onClick(View view) {

                tomarFotoR();
                b = 2;


            }
        });



        return vistaEnv;
    }

    private void tomarFotoR() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        File miFile  =  new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }
        if(isCreada==true){

            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString()+"bck"+".sie";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+File.separator+nombre;
            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));

            startActivityForResult(intent,COD_FOTO);



        }

    }

    private void tomarFotoF() {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        File miFile  =  new File(Environment.getExternalStorageDirectory(),DIRECTORIO_IMAGEN);
        boolean isCreada = miFile.exists();

        if(isCreada==false){
            isCreada=miFile.mkdirs();
        }
        if(isCreada==true){

            Long consecutivo = System.currentTimeMillis()/1000;
            String nombre = consecutivo.toString()+"frt"+".sie";

            path=Environment.getExternalStorageDirectory()+File.separator+DIRECTORIO_IMAGEN+File.separator+nombre;
            fileImagen = new File(path);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(fileImagen));

            startActivityForResult(intent,COD_FOTO);



        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case COD_FOTO:
                MediaScannerConnection.scanFile(getContext(), new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                            if(b==1){
                                Constantes.RutaAbsolutaFrente = getPath(getContext(), uri);
                                System.out.println(Constantes.RutaAbsolutaFrente);
                            }else if(b==2){
                                Constantes.RutaAbsolutaAtras = getPath(getContext(), uri);
                                System.out.println(Constantes.RutaAbsolutaAtras);
                            }

                    }
                });

                if(b==1){
                    bitmapf=BitmapFactory.decodeFile(path);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), bitmapf);
                    imgsendf.setBackground(ob);
                }else if(b==2){
                    bitmapa=BitmapFactory.decodeFile(path);
                    BitmapDrawable ob = new BitmapDrawable(getResources(), bitmapa);
                    imgsendb.setBackground(ob);
                }



                break;
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