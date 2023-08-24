package com.main.racerstore;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.squareup.picasso.Picasso;

public class Result extends AppCompatActivity {
    // Variables para el reproductor de video
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private ViewGroup mainLayout;
    private String thisname = "";
    private String location = "";
    private String urimage="";
    private boolean isPlaying = false;
    private boolean isButtonEnabled = true;
    private SearchController searchController;
    private SearchView txtBuscar;
    private String searchTerm;
    private static final String TAG = "Result";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result);
        Button btncroquis = findViewById(R.id.croquis);
        searchController = new SearchController(Result.this);
        txtBuscar = findViewById(R.id.txtbuscar);
        Intent intent = getIntent();
        if (intent != null) {
            searchTerm = intent.getStringExtra("searchTerm");
            txtBuscar.setQuery(searchTerm, false);
            searchProducts(searchTerm);
        }

        // Configurar listener de búsqueda
        txtBuscar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchTerm = query.trim();
                searchProducts(searchTerm);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                searchTerm = newText.trim();
                searchProducts(searchTerm);
                return true;
            }
        });

        // Inicializar variables del reproductor de video
        playerView = new PlayerView(this);
        mainLayout = findViewById(R.id.mainLayout); // Cambia el ID al ID del layout principal en tu clase

        btncroquis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog popupDialog = new Dialog(Result.this);
                popupDialog.setContentView(R.layout.mapracer);
                // Obtener la referencia a la ImageView del popup
                PhotoView imgv = popupDialog.findViewById(R.id.imageView);
                RelativeLayout popup = popupDialog.findViewById(R.id.layu2);
                // Mostrar el popup con un efecto de crecimiento
                popup.setScaleX(0);
                popup.setScaleY(0);
                popup.animate().scaleX(1).scaleY(1).setDuration(300).start();
                //Mostrar el popup dialog del mapa
                popupDialog.show();
            }
        });
    }

    private void searchProducts(String searchTerm) {
        searchController.searchProducts(searchTerm);
    }

    public void showSearchResults(JSONArray jsonArray) {
        try {
            List<Product> productList = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String codigo = jsonObject.getString("codigo");
                String categoria = jsonObject.getString("categoria");
                String nombre = jsonObject.getString("nombre");
                String descripcion = jsonObject.getString("descripcion");
                String precio = jsonObject.getString("precio");
                String imgrt = jsonObject.getString("imgrt");
                String videoURL = jsonObject.getString("videoURL");
                String ubicacion = jsonObject.getString("ubicacion");
                Product product = new Product(codigo, categoria, nombre, descripcion, precio, imgrt,videoURL,ubicacion);
                productList.add(product);

            }

            RecyclerView recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ProductAdapter productAdapter = new ProductAdapter(productList, this,searchController);
            recyclerView.setAdapter(productAdapter);
        } catch (JSONException e) {
            Toast.makeText(this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        // Verificar si el reproductor de video está activo
        if (isPlaying) {
            // Detener la reproducción del video
            player.stop();
            player.release();
            isPlaying = false;

            // Eliminar el reproductor de video del diseño principal
            mainLayout.removeView(playerView);

            // Volver a cargar y mostrar los resultados del layout principal
            searchProducts(searchTerm);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Liberar recursos del reproductor de video
        if (player != null) {
            player.stop();
            player.release();
        }
    }
}



