package com.main.racerstore;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private EditText tbuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tbuscar = findViewById(R.id.tbuscar);

        Button btnSearch = findViewById(R.id.search);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = tbuscar.getText().toString().trim();

                // Realizar la solicitud HTTP al archivo PHP
                searchProducts(searchTerm);
            }
        });
    }

    private void searchProducts(String searchTerm) {
        OkHttpClient client = new OkHttpClient();

        // Construir el cuerpo de la solicitud con el parámetro de búsqueda
        RequestBody requestBody = new FormBody.Builder()
                .add("searchTerm", searchTerm)
                .build();

        // Construir la solicitud POST
        Request request = new Request.Builder()
                .url("http://192.168.1.45/RacerStore/search.php")  // Reemplaza con la URL de tu archivo PHP en el servidor
                .post(requestBody)
                .build();

        // Enviar la solicitud de forma asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Manejar la respuesta del servidor
                if (response.isSuccessful()) {
                    // La solicitud fue exitosa
                    final String responseData = response.body().string();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                JSONArray jsonArray = new JSONArray(responseData);

                                // Inflar el layout result.xml utilizando LayoutInflater
                                LayoutInflater inflater = LayoutInflater.from(SearchActivity.this);
                                View resultView = inflater.inflate(R.layout.result, null);

                                // Encontrar el LinearLayout llProductsContainer utilizando findViewById en resultView
                                LinearLayout llProductsContainer = resultView.findViewById(R.id.llProductsContainer);

                                // Limpiar el LinearLayout antes de agregar nuevos elementos
                                llProductsContainer.removeAllViews();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    // Obtener los datos de la fila
                                    String codigo = jsonObject.getString("codigo");
                                    String categoria = jsonObject.getString("categoria");
                                    String nombre = jsonObject.getString("nombre");
                                    String descripcion = jsonObject.getString("descripcion");
                                    String imgrt = jsonObject.getString("imgrt");

                                    // Crear un TextView para mostrar los datos del producto
                                    TextView tvProduct = new TextView(SearchActivity.this);
                                    tvProduct.setText("Código: " + codigo + "\n" +
                                            "Categoría: " + categoria + "\n" +
                                            "Nombre: " + nombre + "\n" +
                                            "Descripción: " + descripcion + "\n" +
                                            "Imágen: " + imgrt + "\n");
                                    tvProduct.setTextSize(16);



                                    // Agregar el LinearLayout al LinearLayout llProductsContainer
                                    llProductsContainer.addView(tvProduct);
                                }

                                // Establecer el contenido de la actividad como la vista inflada resultView
                                setContentView(resultView);
                            } catch (JSONException e) {
                                    Toast.makeText(SearchActivity.this, "No se encontraron resultados", Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    // La solicitud falló
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SearchActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                // Error de conexión
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(SearchActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}



