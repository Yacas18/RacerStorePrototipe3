package com.main.racerstore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;

import me.relex.photodraweeview.PhotoDraweeView;

public class SearchActivity extends AppCompatActivity {
    private HorizontalScrollView horizontalScrollView;
    private HorizontalScrollView horizontalScrollView2;
    private int scrollX = 0;
    private int scrollX2 = 0;
    private final Handler handler = new Handler();
    private final Handler handler2 = new Handler();
    private final int scrollDelay = 5; // Tiempo en milisegundos entre desplazamientos
    private final int scrollDelay2 = 4;
    private final int scrollSpeed = 2; // Velocidad del desplazamiento
    private final int scrollSpeed2 = 3;
    private boolean isButtonEnabled = true;
    private LinearLayout ln1;
    private EditText tbuscar;
    private SearchController searchController;
    private Animation buttonAnimation;
    private static final long TIEMPO_LIMITE_SALIDA = 2000; // 2 segundos
    private long tiempoUltimaPulsacion = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cargarImagenesAleatorias();
        ln1 = findViewById(R.id.ln1);
        tbuscar = findViewById(R.id.tbuscar);
        ImageView btnSearch = findViewById(R.id.search);
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
        horizontalScrollView = findViewById(R.id.horizontalScrollView2);
        horizontalScrollView2 = findViewById(R.id.horizontalScrollView3);
        // Agregar un escuchador para detectar cambios en el desplazamiento
        horizontalScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollX = horizontalScrollView.getScrollX();
            }
        });
        horizontalScrollView2.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                scrollX2 = horizontalScrollView2.getScrollX();
            }
        });

        // Iniciar el desplazamiento automático
        startAutoScroll();
        startAutoScroll2();
    }

    private void performSearch() {
        if(TextUtils.isEmpty(tbuscar.getText().toString().trim())){
            String searchTerm = "ACC";
            Intent intent = new Intent(SearchActivity.this, Result.class);
            intent.putExtra("searchTerm", searchTerm);
            startActivity(intent);
        }else {
            String searchTerm = tbuscar.getText().toString().trim();
            Intent intent = new Intent(SearchActivity.this, Result.class);
            intent.putExtra("searchTerm", searchTerm);
            startActivity(intent);
        }
    }


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
    private void startAutoScroll() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollX += scrollSpeed; // Aumentar la posición de desplazamiento
                if (scrollX >= horizontalScrollView.getChildAt(0).getMeasuredWidth() - horizontalScrollView.getMeasuredWidth()) {
                    // Has llegado al final, así que puedes volver al principio sin pausa
                    scrollX = 0;
                }
                horizontalScrollView.scrollTo(scrollX, 0); // Realizar el desplazamiento
                handler.postDelayed(this, scrollDelay); // Programar el próximo desplazamiento
            }
        }, scrollDelay);
    }

    private void startAutoScroll2() {
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollX2 += scrollSpeed2; // Aumentar la posición de desplazamiento
                if (scrollX2 >= horizontalScrollView2.getChildAt(0).getMeasuredWidth() - horizontalScrollView2.getMeasuredWidth()) {
                    // Has llegado al final, así que puedes volver al principio sin pausa
                    scrollX2 = 0;
                }
                horizontalScrollView2.scrollTo(scrollX2, 0); // Realizar el desplazamiento
                handler2.postDelayed(this, scrollDelay2); // Programar el próximo desplazamiento
            }
        }, scrollDelay2);
    }

    private void cargarImagenesAleatorias() {
        // URL del archivo PHP en tu servidor
        String url = "https://circulinasperu.com/RacerStore/randomi.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                String imageUrl = response.getString(i);
                                // Aquí puedes usar la URL de la imagen como desees (por ejemplo, cargarla en una vista)
                                Log.d("URL de imagen", imageUrl);
                                // URL de la imagen

                                PhotoDraweeView photoDraweeView = new PhotoDraweeView(SearchActivity.this);
                                photoDraweeView.setBackgroundResource(R.drawable.rounded_border2);
                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        500, // Ancho en píxeles
                                        500  // Alto en píxeles
                                );
                                params.setMargins(100, 0, 100, 0); // Ajusta los márgenes según tus necesidades
                                photoDraweeView.setLayoutParams(params);

                                Uri imageUri = Uri.parse(imageUrl);
                                DraweeController controller = Fresco.newDraweeControllerBuilder()
                                        .setUri(imageUri)
                                        .setAutoPlayAnimations(true)
                                        .build();
                                photoDraweeView.setController(controller);

                                ln1.addView(photoDraweeView);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores
                        Log.e("Error de Volley", error.toString());
                    }
                });

        // Agrega la solicitud a la cola de solicitudes de Volley
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }
}