package com.main.racerstore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SearchActivity extends AppCompatActivity {
    private EditText tbuscar;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tbuscar = findViewById(R.id.tbuscar);
        search = findViewById(R.id.search);

        Button btnSearch = findViewById(R.id.search);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Crear un Intent para abrir la actividad de inicio de sesión
                Intent intent = new Intent(SearchActivity.this, Result.class);
                startActivity(intent);
            }
        });
    }

    private void searchProducts(String tbuscar) {
        // Aquí puedes agregar la lógica para buscar productos en la base de datos o realizar cualquier otra operación necesaria
        // Como se eliminó la parte de conexión y consultas SQL, esta función debe ser implementada según tus necesidades
        // Puedes utilizar métodos de acceso a datos como SQLite, Room, o utilizar servicios web para obtener los datos de productos.
        // Recuerda adaptar el código según el método que elijas para interactuar con la base de datos o fuente de datos.
    }
}
