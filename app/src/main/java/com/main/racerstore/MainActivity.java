package com.main.racerstore;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.material.textfield.TextInputLayout;

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
    String url = "http://circulinasperu.com/RacerStore/login.php";
    private EditText etUser;
    private EditText etPass;
    private Button btnLogin;
    private Animation buttonAnimation;
    public boolean valid = true;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_login);
        Fresco.initialize(this);
        etUser = findViewById(R.id.log);
        etPass = findViewById(R.id.pass);
        btnLogin = findViewById(R.id.login);
        buttonAnimation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.anim_login);
        TextInputLayout textInputLayout = findViewById(R.id.textInputLayout);
        TextInputLayout textInputLayout2 = findViewById(R.id.textInputLayout2);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etUser.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No necesitas implementar esto.
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // Aquí, cuando se cambia el texto en el EditText, verificamos si el texto es válido.
                        if (!TextUtils.isEmpty(charSequence.toString().trim())) {
                            // Si el texto no está vacío, eliminamos el mensaje de error.
                            textInputLayout.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        // No necesitas implementar esto.
                    }
                });
                etPass.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // No necesitas implementar esto.
                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        // Aquí, cuando se cambia el texto en el EditText, verificamos si el texto es válido.
                        if (!TextUtils.isEmpty(charSequence.toString().trim())) {
                            // Si el texto no está vacío, eliminamos el mensaje de error.
                            textInputLayout2.setError(null);
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        // No necesitas implementar esto.
                    }
                });
                textInputLayout.setErrorTextAppearance(R.style.ErrorText);
                textInputLayout2.setErrorTextAppearance(R.style.ErrorText);
                if (TextUtils.isEmpty(etUser.getText().toString().trim())) {
                    textInputLayout.setError("Introduzca su usuario");
                    valid=false; // Detener el proceso de inicio de sesión
                } else {
                    textInputLayout.setError(null); // Limpiar el mensaje de error
                }
                if (TextUtils.isEmpty(etPass.getText().toString().trim())) {
                    textInputLayout2.setError("Contraseña incorrecta o nula");
                    valid=false; // Detener el proceso de inicio de sesión
                } else {
                    textInputLayout2.setError(null); // Limpiar el mensaje de error
                }

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
                .url(url)  // Reemplaza con la URL de tu archivo PHP en el servidor
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
                    if (responseData.equals("entri")){
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


