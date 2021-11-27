package cne.heba.sie;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.EventLog;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SlapshActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN = 2000;

    Animation topAnim, bottonAnim;
    ImageView image;
    TextView logo, slogan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_slapsh);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottonAnim = AnimationUtils.loadAnimation(this, R.anim.bootton_animation);

        image = findViewById(R.id.imageView);
        logo = findViewById(R.id.textView);
        slogan = findViewById(R.id.textView2);


        image.setAnimation(topAnim);
        logo.setAnimation(bottonAnim);
        slogan.setAnimation(bottonAnim);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {

            Intent intent = new Intent(SlapshActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(SlapshActivity.this, MainActivity.class);

                    Pair[] pairs = new Pair[2];
                    pairs[0] = new Pair<View, String>(image, "logo_image");
                    pairs[1] = new Pair<View, String>(logo, "logo_text");

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP && android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SlapshActivity.this, pairs);
                        startActivity(intent, options.toBundle());
                        finish();
                    } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
                        startActivity(intent);
                        finish();
                    }

                }
            }, SPLASH_SCREEN);


        }
    }
}