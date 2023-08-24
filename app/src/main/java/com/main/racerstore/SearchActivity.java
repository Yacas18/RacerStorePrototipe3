package com.main.racerstore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    private boolean isButtonEnabled = true;
    private EditText tbuscar;
    private SearchController searchController;
    private Animation buttonAnimation;
    private static final long TIEMPO_LIMITE_SALIDA = 2000; // 2 segundos
    private long tiempoUltimaPulsacion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tbuscar = findViewById(R.id.tbuscar);
        Button btnSearch = findViewById(R.id.search);
        ImageView racerLogo = findViewById(R.id.racerLogo);
        searchController = new SearchController(this);
        buttonAnimation = AnimationUtils.loadAnimation(SearchActivity.this, R.anim.anim_login);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnSearch.startAnimation(buttonAnimation);
                if (isButtonEnabled) {
                    isButtonEnabled = false; // Desactivar el botón temporalmente

                    performSearch();

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
        racerLogo.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Acción a realizar al mantener presionada la imagen
                // Redireccionar a MiniMenu.java con el layout mmenu
                Intent intent = new Intent(SearchActivity.this, MiniMenu.class);
                startActivity(intent);

                return true;
            }
        });
    }

    private void performSearch() {
        String searchTerm = tbuscar.getText().toString().trim();
        Intent intent = new Intent(SearchActivity.this, Result.class);
        intent.putExtra("searchTerm", searchTerm);
        startActivity(intent);
    }


    // Resto del código...

    @Override
    protected void onRestart() {
        super.onRestart();
        tbuscar.setText("");
    }

    @Override
    public void onBackPressed() {
        long tiempoActual = System.currentTimeMillis();

        if (tiempoActual - tiempoUltimaPulsacion < TIEMPO_LIMITE_SALIDA) {
            super.onBackPressed();
        } else {
            tiempoUltimaPulsacion = tiempoActual;
            Toast.makeText(this, "Presiona nuevamente para salir", Toast.LENGTH_SHORT).show();
        }
    }
}