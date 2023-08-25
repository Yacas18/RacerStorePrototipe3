package com.main.racerstore;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.URI;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.app.ActivityManager;
public class SearchController{
    private boolean isActionEnabled = true;
    private Context context;
    private SearchActivity searchActivity;
    private Result result;
    private Edit edit;
    private Add add;
    private MiniMenu miniMenu;
    private ProductAdapter productAdapter;
    String ip = Product.getGlobalip();
    public SearchController(SearchActivity searchActivity) {
        this.searchActivity = searchActivity;
    }
    public SearchController(Edit edit) {
        this.edit = edit;
    }
    public SearchController(Result result) {
        this.result = result;
    }
    public  SearchController(Add add){this.add = add;}
    public SearchController(ProductAdapter productAdapter){this.productAdapter = productAdapter;}
    public SearchController(MiniMenu miniMenu){this.miniMenu = miniMenu;}

    public void searchProducts(String searchTerm) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("searchTerm", searchTerm).build();
        Request request = new Request.Builder().url(ip+"/RacerStore/search.php").post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    // Verificar el contexto actual
                    if (searchActivity != null) {
                        searchActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray jsonArray = new JSONArray(responseData);
                                    result.showSearchResults(jsonArray);
                                } catch (JSONException e) {
                                    Toast.makeText(searchActivity, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        });
                    } else if (result != null) {
                        result.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    JSONArray jsonArray = new JSONArray(responseData);
                                    result.showSearchResults(jsonArray);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } else {
                    if (searchActivity != null) {
                        searchActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(searchActivity, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (result != null) {
                        result.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(result, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                if (searchActivity != null) {
                    searchActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(searchActivity, "Error de conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (result != null) {
                    result.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(result, "Error de conexión", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }
    public void searchProductsEdit() {
        // Obtén el término de búsqueda del SearchView en la clase Edit
        String searchTerm = edit.getSearchTerm(); // Reemplaza "edit" con la instancia de la clase Edit correspondiente
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder().add("searchTerm", searchTerm).build();
        Request request = new Request.Builder().url(ip+"/RacerStore/search.php").post(requestBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();

                    // Verificar el contexto actual (en este caso, la clase Edit)
                    edit.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(responseData);
                                edit.showSearchResults(jsonArray);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    edit.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(edit, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                edit.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(edit, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void enviarDatosAlServidorAdd(Context context, String URL,String codigo, String categoria, String nombre, String descripcion, String precio,String ubicacion) {
        // Crea una instancia de OkHttpClient (o la biblioteca que estés utilizando)
        OkHttpClient client = new OkHttpClient();

        // Crea un objeto FormBody.Builder para construir los parámetros de la solicitud
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("codigo", codigo)
                .add("categoria", categoria)
                .add("nombre", nombre)
                .add("descripcion", descripcion)
                .add("precio", precio)
                .add("ubicacion",ubicacion);
        // Crea una solicitud HTTP POST con la URL del archivo PHP y los parámetros
        Request request = new Request.Builder()
                .url(URL)
                .post(formBuilder.build())
                .build();

        // Envía la solicitud al servidor
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Maneja el error de conexión
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Maneja la respuesta del servidor
                final String respuesta = response.body().string();

                // Actualiza la interfaz de usuario en el hilo principal
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Muestra la respuesta del servidor en un Toast o realiza otras acciones
                        Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void enviarDatosAlServidorEdit(Context context, String URL, String idcodigo,String codigo, String categoria, String nombre, String descripcion, String precio,String ubicacion) {
        // Crea una instancia de OkHttpClient (o la biblioteca que estés utilizando)
        OkHttpClient client = new OkHttpClient();

        // Crea un objeto FormBody.Builder para construir los parámetros de la solicitud
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("idcodigo",idcodigo)
                .add("codigo", codigo)
                .add("categoria", categoria)
                .add("nombre", nombre)
                .add("descripcion", descripcion)
                .add("precio", precio)
                .add("ubicacion", ubicacion);
        // Crea una solicitud HTTP POST con la URL del archivo PHP y los parámetros
        Request request = new Request.Builder()
                .url(URL)
                .post(formBuilder.build())
                .build();

        // Envía la solicitud al servidor
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Maneja el error de conexión
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Maneja la respuesta del servidor
                final String respuesta = response.body().string();

                // Actualiza la interfaz de usuario en el hilo principal
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Muestra la respuesta del servidor en un Toast o realiza otras acciones
                        Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void eliminarProducto(Context context, String cod) {
        // Crea una instancia de OkHttpClient (o la biblioteca que estés utilizando)
        OkHttpClient client = new OkHttpClient();

        // Crea un objeto FormBody.Builder para construir los parámetros de la solicitud
        FormBody.Builder formBuilder = new FormBody.Builder()
                .add("codigo", cod);

        // Crea una solicitud HTTP POST con la URL del archivo PHP y los parámetros
        Request request = new Request.Builder()
                .url(ip+"/RacerStore/eliminar_producto.php")
                .post(formBuilder.build())
                .build();

        // Envía la solicitud al servidor
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Maneja el error de conexión
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Maneja la respuesta del servidor
                final String respuesta = response.body().string();

                // Actualiza la interfaz de usuario en el hilo principal
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        // Muestra la respuesta del servidor en un Toast o realiza otras acciones
                        Toast.makeText(context, respuesta, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
    public void showVideoDialog(String videoURL, Context context) {
        // Crear el AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.player_video, null);
        builder.setView(dialogView);

        // Configurar el reproductor de video y reproducir el video
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(context).build();
        PlayerView playerView = dialogView.findViewById(R.id.playerView);
        playerView.setPlayer(player);
        playerView.setUseController(true);

        // Configurar el MediaItem del video
        MediaItem mediaItem = MediaItem.fromUri(videoURL);
        player.setMediaItem(mediaItem);

        // Preparar y reproducir el video
        player.prepare();
        player.play();

        // Configurar el cierre del AlertDialog
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    // Detener la reproducción del video y liberar los recursos
                    player.stop();
                    player.release();
                    dialog.dismiss();
                    return true;
                }
                return false;
            }
        });

        // Mostrar el AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void mostrarLocate(TextView text, String nombre, TextView text2, String locate,String uri, ImageView imageloc){
        // Obtener la referencia a la ImageView del popup
        text.setText(nombre);
        text2.setText(locate);

        Picasso.get().load(uri).into(imageloc);
    }
    public void imagepopup(String uri,ImageView imgrr){
        Picasso.get().load(uri).into(imgrr);
    }

}

