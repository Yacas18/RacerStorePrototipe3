package com.main.racerstore;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ejecutar la tarea asincrónica para establecer la conexión a la base de datos
        ConnectToDatabaseTask task = new ConnectToDatabaseTask();
        task.execute();
    }

    private class ConnectToDatabaseTask extends AsyncTask<Void, Void, Connection> {

        private static final String CONNECTION_URL = "jdbc:sqlserver://localhost\\MSSQLSERVER01;databaseName=master;integratedSecurity=true;";

        @Override
        protected Connection doInBackground(Void... voids) {
            Connection connection = null;

            try {
                // Establecer la conexión a la base de datos
                connection = DriverManager.getConnection(CONNECTION_URL);

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return connection;
        }

        @Override
        protected void onPostExecute(Connection connection) {
            super.onPostExecute(connection);

            if (connection != null) {
                // La conexión se estableció correctamente
                Log.d(TAG, "Conexión exitosa a la base de datos");

                // Aquí puedes realizar operaciones con la conexión, como ejecutar consultas SQL

                // Cerrar la conexión cuando hayas terminado
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            } else {
                // Hubo un error al establecer la conexión
                Log.e(TAG, "Error al conectar a la base de datos");
            }
        }
    }
}

