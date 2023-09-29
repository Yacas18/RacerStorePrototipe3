package com.main.racerstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Timer;
import java.util.TimerTask;
import com.facebook.drawee.backends.pipeline.Fresco;


public class Splash extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.splash);
            Fresco.initialize(this);
            ImageView iconImageView = findViewById(R.id.imageView); // Reemplaza con el ID correcto de tu ImageView
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
            iconImageView.startAnimation(animation);
            ImageView iconImageView2 = findViewById(R.id.imageView2); // Reemplaza con el ID correcto de tu ImageView
            Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.ani_fade);
            iconImageView2.startAnimation(animation2);

            TimerTask tarea= new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            Timer tiempo = new Timer();
            tiempo.schedule(tarea,2000);

        }

}
