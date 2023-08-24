package com.main.racerstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import com.airbnb.lottie.LottieAnimationView;

public class MiniMenu extends AppCompatActivity {
    private boolean isButtonEnabled = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mmenu);
        ImageView addP = findViewById(R.id.addP);
        ImageView editP = findViewById(R.id.editP);
        SearchController searchController = new SearchController(this); // o con el contexto apropiado
        addP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isButtonEnabled) {
                    isButtonEnabled = false; // Desactivar el botón temporalmente

                    Intent intent = new Intent(MiniMenu.this,Add.class);
                    startActivity(intent);

                    // Restablecer el estado del botón después de un tiempo
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isButtonEnabled = true; // Volver a habilitar el botón
                        }
                    }, 2000); // Especifica el tiempo en milisegundos antes de restablecer el botón
                }
            }
        });

        editP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isButtonEnabled) {
                    isButtonEnabled = false; // Desactivar el botón temporalmente

                    Intent intent = new  Intent(MiniMenu.this,Edit.class);
                    startActivity(intent);

                    // Restablecer el estado del botón después de un tiempo
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isButtonEnabled = true; // Volver a habilitar el botón
                        }
                    }, 2000); // Especifica el tiempo en milisegundos antes de restablecer el botón
                }
            }
        });
    }


}