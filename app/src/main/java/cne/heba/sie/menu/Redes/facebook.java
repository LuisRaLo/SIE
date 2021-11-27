package cne.heba.sie.menu.Redes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.SimpleMultiPartRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareHashtag;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cne.heba.sie.R;
import cne.heba.sie.util.Constantes;

import static com.facebook.FacebookSdk.getApplicationContext;

public class facebook extends Fragment {

    View vistaF;
    private CallbackManager callbackManager;
    LoginButton loginButton;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    ShareDialog shareDialog;
    RequestQueue queue;

    String APIREDES ="";

    ImageView foto;
    TextView nombre;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vistaF = inflater.inflate(R.layout.fragment_facebook, container, false);
        callbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) vistaF.findViewById(R.id.login_button);
        loginButton.setFragment(this);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        foto = (ImageView)vistaF.findViewById(R.id.fotoPerfil);
        nombre = (TextView)vistaF.findViewById(R.id.nombresF);

        obtenerActividades();
        crearBotones(1);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                final AccessToken accessToken = loginResult.getAccessToken();

                Profile profile = Profile.getCurrentProfile();
                Datos(profile , accessToken);
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

                    }
                };

                profileTracker=new ProfileTracker() {
                    @Override
                    protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                        Datos(currentProfile, accessToken);

                    }
                };

                accessTokenTracker.startTracking();
                profileTracker.startTracking();

                //Reference Facebook Login
                loginButton.setReadPermissions("user_friends");
                loginButton.setReadPermissions("public_profile");
                loginButton.setReadPermissions("email");

            }

            @Override
            public void onCancel() {

                Toast.makeText(getApplicationContext(),"No se inicio sesión",Toast.LENGTH_LONG).show();

            }

            @Override
            public void onError(FacebookException error) {



            }
        });



        return vistaF;
    }

    private void crearBotones(int arrow) {

        Toast.makeText(getContext(), "Obteniendo Actividades", Toast.LENGTH_SHORT).show();
        LinearLayout llBotonera = (LinearLayout)vistaF.findViewById(R.id.llBotonera);
        //Creamos las propiedades de layout que tendrán los botones.
        //Son LinearLayout.LayoutParams porque los botones van a estar en un LinearLayout.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );

        //Creamos los botones en bucle
        for (int i=0; i<arrow; i++){
            Button button = new Button(getContext());
            //Asignamos propiedades de layout al boton
            button.setLayoutParams(lp);
            //Asignamos estilo a el boton
            button.setBackgroundResource(R.drawable.bordero);
            button.setTextColor(Color.BLACK);
            //Asignamos Texto al botón
            button.setText("18-May-21 | Pagina Facebook | Dar Like ");
            //Asignamose el Listener
            button.setOnClickListener(new ButtonsOnClickListener(getContext(),i));
            //Añadimos el botón a la botonera
            llBotonera.addView(button);
        }
    }

    class ButtonsOnClickListener implements View.OnClickListener
    {
        Context context;
        int numButton;

        public ButtonsOnClickListener(Context context, int numButton) {
            this.context = context;
            this.numButton = numButton;
        }

        @Override
        public void onClick(View v)
        {
            comparte("https://facebook.com/LaCabraArisca");
        }

    }

    private void obtenerActividades() {

        String url = Constantes.SERVER+APIREDES; // Url de el servidor
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

    @Override
    public void onActivityResult(final int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        callbackManager.onActivityResult(requestCode,resultCode,data);
        try {


            shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                @Override
                public void onSuccess(Sharer.Result result) {
                    Toast.makeText(getContext(), "Articulo Compartido", Toast.LENGTH_LONG).show();
                    System.out.println("RESULTAD: " + result + " ID=" + result.getPostId());
                }

                @Override
                public void onCancel() {
                    Toast.makeText(getContext(), "Articulo No Compartido", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onError(FacebookException error) {
                    Toast.makeText(getContext(), "Error " + error.toString(), Toast.LENGTH_LONG).show();
                }
            });

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(),"Por favor reinicie...",Toast.LENGTH_LONG).show();
            Log.e("LogFace",e.toString());
        }
    }


    private  void Datos(Profile perfil, AccessToken tokenC){
        if(perfil!=null){

            GraphRequest request = GraphRequest.newMeRequest(
                    tokenC,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            Log.e("DATOS", response.toString());
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link");
            request.setParameters(parameters);
            request.executeAsync();


            String id = perfil.getId();
            String link = Profile.getCurrentProfile().getLinkUri().toString();
            String url = Profile.getCurrentProfile().getProfilePictureUri(200, 200).toString();
            String nom=perfil.getName();
            nombre.setText(nom);
            Picasso.get().load(url).into(foto);
            Log.e("Url Profile", link);


        }
    }
    @Override
    public void onResume() {
        super.onResume();

        Profile profile=Profile.getCurrentProfile();
        Datos(profile, null);
    }

    private void comparte(String LinkF){
        shareDialog = new ShareDialog(this);
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(LinkF))
                .setQuote("Compartir Link")
                .setContentDescription("Articulo de MACM")
                .setShareHashtag(new ShareHashtag.Builder()
                        .setHashtag("#SiganLaPagina")
                        .build())
                .build();
        if(shareDialog.canShow(ShareLinkContent.class))
        {
            shareDialog.show(linkContent);
        }


    }

}