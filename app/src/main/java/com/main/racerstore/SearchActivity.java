package com.main.racerstore;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import me.relex.photodraweeview.PhotoDraweeView;

public class SearchActivity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private Runnable mRunnable;
    private static final int DELAY_MILLISECONDS = 7000; // 5 segundos
    TextView txt1,txt2,txt3,txt4;
    ImageView img1,img2,img3,img4;
    private HorizontalScrollView horizontalScrollView2;
    private boolean isButtonEnabled = true;
    private LinearLayout ln1;
    private LinearLayout ln2;
    private EditText tbuscar;
    private SearchController searchController;
    private Animation buttonAnimation;
    private static final long TIEMPO_LIMITE_SALIDA = 2000; // 2 segundos
    private long tiempoUltimaPulsacion = 0;
    ImageView btn1,btn2,btn3,btn4,btn5,btn6;
    public boolean cargaPeriodicaHabilitada = true;
    public boolean isAutoScrollEnabled2 = true;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        cargarImagenesAleatorias();
        //ln2 = findViewById(R.id.ln2);
        btn1 = findViewById(R.id.btn1);
        btn2 = findViewById(R.id.btn2);
        btn3 = findViewById(R.id.btn3);
        btn4 = findViewById(R.id.btn4);
        btn5 = findViewById(R.id.btn5);
        btn6 = findViewById(R.id.btn6);
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
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = "Accesorios";
                Intent intent = new Intent(SearchActivity.this, Result.class);
                intent.putExtra("searchTerm", searchTerm);
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = "Emblemas";
                Intent intent = new Intent(SearchActivity.this, Result.class);
                intent.putExtra("searchTerm", searchTerm);
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = "Barra led";
                Intent intent = new Intent(SearchActivity.this, Result.class);
                intent.putExtra("searchTerm", searchTerm);
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = "Circulinas";
                Intent intent = new Intent(SearchActivity.this, Result.class);
                intent.putExtra("searchTerm", searchTerm);
                startActivity(intent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = "Electrico";
                Intent intent = new Intent(SearchActivity.this, Result.class);
                intent.putExtra("searchTerm", searchTerm);
                startActivity(intent);
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = "Grapas";
                Intent intent = new Intent(SearchActivity.this, Result.class);
                intent.putExtra("searchTerm", searchTerm);
                startActivity(intent);
            }
        });
        /*horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Consume todos los eventos táctiles para evitar la interacción
                return true;
            }
        });*/
        //horizontalScrollView2 = findViewById(R.id.horizontalScrollView3);
        //autoScroll2();
        iniciarCargaPeriodica();
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

    private void cargarImagenesAleatorias() {
        String url = "https://circulinasperu.com/RacerStore/randomi.php";

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Procesa la respuesta en segundo plano
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject item = response.getJSONObject(0);
                                        String productName = item.getString("nombre");
                                        String cat = item.getString("categoria");
                                        String desp = item.getString("descripcion");
                                        String pric = item.getString("precio");
                                        String imageUrl = item.getString("imgrt");
                                        JSONObject item2 = response.getJSONObject(1);
                                        String productName2 = item2.getString("nombre");
                                        String cat2 = item2.getString("categoria");
                                        String desp2 = item2.getString("descripcion");
                                        String pric2 = item2.getString("precio");
                                        String imageUrl2 = item2.getString("imgrt");
                                        JSONObject item3 = response.getJSONObject(2);
                                        String productName3 = item3.getString("nombre");
                                        String cat3 = item3.getString("categoria");
                                        String desp3 = item3.getString("descripcion");
                                        String pric3 = item3.getString("precio");
                                        String imageUrl3 = item3.getString("imgrt");
                                        JSONObject item4 = response.getJSONObject(3);
                                        String productName4 = item4.getString("nombre");
                                        String cat4 = item4.getString("categoria");
                                        String desp4 = item4.getString("descripcion");
                                        String pric4 = item4.getString("precio");
                                        String imageUrl4 = item4.getString("imgrt");
                                        // Comprueba si la URL es una imagen válida antes de cargarla
                                        if ((cargaPeriodicaHabilitada)) {
                                            // Crea la vista de la imagen y cárgala en el hilo principal
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Load(productName, productName2,productName3,productName4, imageUrl, imageUrl2,imageUrl3,imageUrl4,cat,cat2,cat3,cat4,desp,desp2,desp3,desp4,pric,pric2,pric3,pric4);
                                                }
                                            });
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
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

    /*private void cargarImagenesAleatorias2() {
        // URL del archivo PHP en tu servidor
        String url = "https://circulinasperu.com/RacerStore/ff.php";

        // Crea una solicitud de JSONArray utilizando Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Procesa la respuesta en segundo plano
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    for (int i = 0; i < response.length(); i++) {
                                        JSONObject item = response.getJSONObject(i);
                                        String productName = item.getString("nombre");
                                        String cat = item.getString("categoria");
                                        String desp = item.getString("descripcion");
                                        String pric = item.getString("precio");
                                        String imageUrl = item.getString("imgrt");

                                        // Comprueba si la URL es una imagen válida antes de cargarla
                                        if (isImageValid(imageUrl)) {
                                            // Crea la vista de la imagen y cárgala en el hilo principal
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    // Crea una nueva ProductCardView y pásale la referencia de SearchActivity
                                                    ProductCardView2 customCardView = new ProductCardView2(SearchActivity.this, imageUrl, productName,cat,desp,pric, SearchActivity.this);

                                                    // Agrega la ProductCardView al contenedor (LinearLayout en este caso)
                                                    ln2.addView(customCardView);
                                                }
                                            });
                                        } else {
                                            // No es una imagen válida, puedes manejarlo de acuerdo a tus necesidades
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
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
    }*/

    private boolean isImageValid(String imageUrl) {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(imageUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            String contentType = connection.getHeaderField("Content-Type");

            // Comprueba si el tipo de contenido es una imagen
            return contentType != null && contentType.startsWith("image/");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        // Si no se puede determinar si es una imagen válida, considera que no lo es
        return false;
    }

    private void autoScroll2() {
        int duration = 4000; // Duración de la animación en milisegundos
        int scrollDistance = 1065; // Distancia de desplazamiento en píxeles

        // Mueve el ScrollView horizontal a la derecha
        horizontalScrollView2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isAutoScrollEnabled2) {
                    horizontalScrollView2.smoothScrollBy(scrollDistance, 0);
                }
                horizontalScrollView2.postDelayed(this, duration);
            }
        }, duration);

        // Detecta el final del desplazamiento
        horizontalScrollView2.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int maxScrollX = ln2.getWidth() - horizontalScrollView2.getWidth();
                if (horizontalScrollView2.getScrollX() == maxScrollX) {
                    // Cuando llega al final, vuelve al principio sin animación
                    horizontalScrollView2.smoothScrollTo(0, 0);
                }
            }
        });
    }

    public void Load(String n1,String n2,String n3,String n4,String imgl1,String imgl2,String imgl3,String imgl4,String catego1,String catego2,String catego3,String catego4,String descript1,String descript2,String descript3,String descript4,String pricep1,String pricep2,String pricep3,String pricep4){
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        txt3 = findViewById(R.id.txt3);
        txt4 = findViewById(R.id.txt4);
        img1 = findViewById(R.id.imageView1);
        img2 = findViewById(R.id.imageView2);
        img3 = findViewById(R.id.imageView3);
        img4 = findViewById(R.id.imageView4);
        txt1.setText(n1);
        txt2.setText(n2);
        txt3.setText(n3);
        txt4.setText(n4);
        Picasso.get().load(imgl1).into(img1);
        Picasso.get().load(imgl2).into(img2);
        Picasso.get().load(imgl3).into(img3);
        Picasso.get().load(imgl4).into(img4);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaPeriodicaHabilitada=false;
                Dialog ppo = new Dialog(SearchActivity.this);
                ppo.setContentView(R.layout.dialogp);
                ImageView img = ppo.findViewById(R.id.imageProducto);
                TextView name = ppo.findViewById(R.id.tvNombre);
                TextView cate = ppo.findViewById(R.id.tvCategoria);
                TextView descrip = ppo.findViewById(R.id.tvDescripcion);
                TextView price = ppo.findViewById(R.id.tvPrecio);
                ImageView lista = ppo.findViewById(R.id.list);
                name.setText(n1);
                cate.setText(catego1);
                descrip.setText(descript1);
                price.setText("S/ " + pricep1);
                Picasso.get().load(imgl1).into(img);
                ppo.show();

                ppo.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        cargaPeriodicaHabilitada = true;
                    }
                });
                lista.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String searchTerm = catego1;
                        Intent intent = new Intent(SearchActivity.this, Result.class);
                        intent.putExtra("searchTerm", searchTerm);
                        startActivity(intent); // Usa el contexto de searchActivity para iniciar la nueva actividad

                        // Realiza la búsqueda del elemento en el adaptador y desplázate hasta él
                    }

                });
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaPeriodicaHabilitada=false;
                Dialog ppo = new Dialog(SearchActivity.this);
                ppo.setContentView(R.layout.dialogp);
                ImageView img = ppo.findViewById(R.id.imageProducto);
                TextView name = ppo.findViewById(R.id.tvNombre);
                TextView cate = ppo.findViewById(R.id.tvCategoria);
                TextView descrip = ppo.findViewById(R.id.tvDescripcion);
                TextView price = ppo.findViewById(R.id.tvPrecio);
                ImageView lista = ppo.findViewById(R.id.list);
                name.setText(n2);
                cate.setText(catego2);
                descrip.setText(descript2);
                price.setText("S/ " + pricep2);
                Picasso.get().load(imgl2).into(img);
                ppo.show();
                ppo.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        cargaPeriodicaHabilitada = true;
                    }
                });
                lista.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String searchTerm = catego2;
                        Intent intent = new Intent(SearchActivity.this, Result.class);
                        intent.putExtra("searchTerm", searchTerm);
                        startActivity(intent); // Usa el contexto de searchActivity para iniciar la nueva actividad

                        // Realiza la búsqueda del elemento en el adaptador y desplázate hasta él
                    }

                });
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaPeriodicaHabilitada=false;
                Dialog ppo = new Dialog(SearchActivity.this);
                ppo.setContentView(R.layout.dialogp);
                ImageView img = ppo.findViewById(R.id.imageProducto);
                TextView name = ppo.findViewById(R.id.tvNombre);
                TextView cate = ppo.findViewById(R.id.tvCategoria);
                TextView descrip = ppo.findViewById(R.id.tvDescripcion);
                TextView price = ppo.findViewById(R.id.tvPrecio);
                ImageView lista = ppo.findViewById(R.id.list);
                name.setText(n3);
                cate.setText(catego3);
                descrip.setText(descript3);
                price.setText("S/ " + pricep3);
                Picasso.get().load(imgl3).into(img);
                ppo.show();
                ppo.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        cargaPeriodicaHabilitada = true;
                    }
                });
                lista.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String searchTerm = catego3;
                        Intent intent = new Intent(SearchActivity.this, Result.class);
                        intent.putExtra("searchTerm", searchTerm);
                        startActivity(intent); // Usa el contexto de searchActivity para iniciar la nueva actividad

                        // Realiza la búsqueda del elemento en el adaptador y desplázate hasta él
                    }

                });
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cargaPeriodicaHabilitada=false;
                Dialog ppo = new Dialog(SearchActivity.this);
                ppo.setContentView(R.layout.dialogp);
                ImageView img = ppo.findViewById(R.id.imageProducto);
                TextView name = ppo.findViewById(R.id.tvNombre);
                TextView cate = ppo.findViewById(R.id.tvCategoria);
                TextView descrip = ppo.findViewById(R.id.tvDescripcion);
                TextView price = ppo.findViewById(R.id.tvPrecio);
                ImageView lista = ppo.findViewById(R.id.list);
                name.setText(n4);
                cate.setText(catego4);
                descrip.setText(descript4);
                price.setText("S/ " + pricep4);
                Picasso.get().load(imgl4).into(img);
                ppo.show();
                ppo.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        cargaPeriodicaHabilitada = true;
                    }
                });
                lista.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String searchTerm = catego4;
                        Intent intent = new Intent(SearchActivity.this, Result.class);
                        intent.putExtra("searchTerm", searchTerm);
                        startActivity(intent); // Usa el contexto de searchActivity para iniciar la nueva actividad

                        // Realiza la búsqueda del elemento en el adaptador y desplázate hasta él
                    }

                });
            }
        });

    }
    private void iniciarCargaPeriodica() {
        if(cargaPeriodicaHabilitada) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                    cargarImagenesAleatorias();
                    mHandler.postDelayed(this, DELAY_MILLISECONDS);
                }
            };

            mHandler.postDelayed(mRunnable, DELAY_MILLISECONDS);
        }
    }

    private void detenerCargaPeriodica() {
        mHandler.removeCallbacks(mRunnable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        detenerCargaPeriodica();
    }

}