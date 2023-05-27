package com.main.racerstore;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

public class SearchActivity extends AppCompatActivity {
    private static final String URL = "jdbc:sqlserver://localhost:PUERTO;databaseName=RacerStore";
    private static final String USER = "TU_USUARIO";
    private static final String PASSWORD = "TU_CONTRASEÑA";
    private EditText tbuscar;
    private Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tbuscar = findViewById(R.id.tbuscar);
        search = findViewById(R.id.search);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textoBusqueda = tbuscar.getText().toString().trim();
                // Realizar búsqueda en la tabla de productos con el texto de búsqueda
                searchProducts(textoBusqueda);
            }
        });
    }
    private void searchProducts(String tbuscar) {

        try {
            // Establecer la conexión con la base de datos
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // Definir la consulta SQL para buscar coincidencias
            String query = "SELECT * FROM productos WHERE codigo LIKE ? OR " +
                    "categoria LIKE ? OR " +
                    "nombre LIKE ? OR " +
                    "descripcion LIKE ?";

            // Crear una sentencia preparada con los parámetros de búsqueda
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + search + "%");
            stmt.setString(2, "%" + search + "%");
            stmt.setString(3, "%" + search + "%");
            stmt.setString(4, "%" + search + "%");

            // Ejecutar la consulta y obtener los resultados
            ResultSet rs = stmt.executeQuery();

            // Recorrer los resultados y mostrar cada fila
            while (rs.next()) {
                int codigo = rs.getInt("codigo");
                String categoria = rs.getString("categoria");
                String nombre = rs.getString("nombre");
                String descripcion = rs.getString("descripcion");

                // Mostrar los datos de la fila de la coincidencia
                Log.d("Producto", "Código: " + codigo);
                Log.d("Producto", "Categoría: " + categoria);
                Log.d("Producto", "Nombre: " + nombre);
                Log.d("Producto", "Descripción: " + descripcion);
            }

            // Cerrar los recursos
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
