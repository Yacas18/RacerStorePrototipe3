package com.main.racerstore;

import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SearchActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private Connection connection;
    private EditText tbuscar;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        dbHelper = new DBHelper();
        connection = dbHelper.getConnection();

        tbuscar = findViewById(R.id.tbuscar);
        search = findViewById(R.id.search);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.closeConnection();
    }

    private void searchProducts(String tbuscar) {
        // Aquí puedes realizar la búsqueda de productos utilizando la conexión y el texto de búsqueda
        // Recuerda manejar adecuadamente las excepciones y cerrar los recursos apropiadamente
    }
}
