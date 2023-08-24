package com.main.racerstore;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private boolean isButtonEnabled = true;
    private EditText etUser;
    private EditText etPass;
    private Button btnLogin;
    private Animation buttonAnimation;
    String ip = Product.getGlobalip();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.log);
        etPass = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.login);
        buttonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogin.startAnimation(buttonAnimation);
                if (isButtonEnabled) {
                    isButtonEnabled = false; // Desactivar el botón temporalmente
                    String user = etUser.getText().toString().trim();
                    String pass = etPass.getText().toString().trim();
                    // Realizar la solicitud HTTP al archivo PHP
                    login(user, pass);

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
    }


    private void login(String user, String pass) {
        OkHttpClient client = new OkHttpClient();

        // Construir el cuerpo de la solicitud con los parámetros de usuario y contraseña
        RequestBody requestBody = new FormBody.Builder()
                .add("user", user)
                .add("pass", pass)
                .build();

        // Construir la solicitud POST
        Request request = new Request.Builder()
                .url(ip+"/RacerStore/login.php")  // Reemplaza con la URL de tu archivo PHP en el servidor
                .post(requestBody)
                .build();

        // Enviar la solicitud de forma asíncrona
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Manejar la respuesta del servidor
                if (response.isSuccessful()) {
                    // La solicitud fue exitosa
                    String responseData = response.body().string();
                    // Aquí puedes manejar la respuestas del servidor según lo que devuelva tu archivo PHP
                    if (responseData.equals("Inicio de sesion exitosa")) {
                        // El inicio de sesión fue exitoso
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Inicio de sesion exitosa", Toast.LENGTH_SHORT).show();
                                // Aquí puedes redirigir a otra actividad o realizar alguna acción adicional
                                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else {
                        // Las credenciales son inválidas
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(MainActivity.this, "Credenciales invalidas", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else {
                    // La solicitud falló
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(MainActivity.this, "Error en la solicitud", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(MainActivity.this, "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}


