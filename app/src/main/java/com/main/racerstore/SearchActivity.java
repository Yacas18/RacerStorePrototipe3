package com.main.racerstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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
                // Crear un Intent para abrir la actividad de resultados
                Intent intent = new Intent(SearchActivity.this, Result.class);
                startActivity(intent);
            }
        });
    }

    private void searchProducts(String tbuscar) {
        // Aquí puedes realizar la búsqueda de productos utilizando PHP como puente de conexión
        // Recuerda manejar adecuadamente las respuestas y procesar los resultados obtenidos
    }
}

