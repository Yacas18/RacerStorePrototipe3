package com.main.racerstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
public class Add extends AppCompatActivity {
    private boolean isButtonEnabled = true;
    private EditText codigoEditText;
    private EditText categoriaEditText;
    private EditText nombreEditText;
    private EditText descripcionEditText;
    private EditText precioEditText;
    ImageView iv;
    TextView et;
    ImageView cyt;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap bitmap;
    int PICK_IMAGE_REQUEST = 1;
    private RelativeLayout mainLayout;
    String KEY_IMAGE = "foto";
    String KEY_NOMBRE = "nombre";
    SearchController searchController;
    private LottieAnimationView animationView;
    private Uri imageUri;
    private Spinner spinner;
    public String video = "";
    String option[] = {"Ubicación del producto","ANAQUEL 1","ANAQUEL 2","ANAQUEL 3","ANAQUEL 4",
            "ANAQUEL 5", "ANAQUEL 6", "ANAQUEL 7","ANAQUEL 8","ANAQUEL 9","ANAQUEL 10","ANAQUEL 11","ANAQUEL 12"
    ,"ANAQUEL 13","ANAQUEL 14","ANAQUEL 15","COCINA","TRUPAN 1","TRUPAN 2","TRUPAN 3"
            ,"TRUPAN 4","TRUPAN 5","ZONA OSCURA", "VITRINA 1","VITRINA 2","VITRINA 3","VITRINA 4", "VITRINA 5"};
    public String opcs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addp);
        searchController= new SearchController(Add.this);
        animationView = findViewById(R.id.animationView);
        et = findViewById(R.id.editText);
        iv = findViewById(R.id.imageView);
        mainLayout =findViewById(com.airbnb.lottie.R.id.software);
        codigoEditText = findViewById(R.id.icodigo);
        categoriaEditText = findViewById(R.id.icategoria);
        nombreEditText = findViewById(R.id.inombre);
        descripcionEditText = findViewById(R.id.idescripcion);
        precioEditText = findViewById(R.id.iprecio);
        cyt = findViewById(R.id.clipyt);
        ViewSwitcher viewSwitcher = findViewById(R.id.viewSwitcher);
        Button ingresar = findViewById(R.id.intodate);
        spinner = findViewById(R.id.locate);
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,option);
        spinner.setAdapter(adapter);
        ingresar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (isButtonEnabled) {
                    isButtonEnabled = false; // Desactivar el botón temporalmente
                    handleIngresarButtonClick();
                    viewSwitcher.showNext();
                    animationView.playAnimation();
                    String texto = codigoEditText.getText().toString(); // Obtén el texto del EditText
                    et.setText(texto); // Asigna el texto al TextView
                    uploadImage();
                    // Restablecer el estado del botón después de un tiempo
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isButtonEnabled = true; // Volver a habilitar el botón
                        }
                    }, 2000); // Especifica el tiempo en milisegundos antes de restablecer el botón
                }
                int delayMillis = 2000; // 2000 milisegundos = 2 segundos
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                }, delayMillis);
            }
        });
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageSourceMenu();
            }
        });

        cyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog pp = new Dialog(Add.this);
                pp.setContentView(R.layout.inpurl);
                EditText link = pp.findViewById(R.id.youtube);
                Button savm =pp.findViewById(R.id.save);
                pp.show();
                savm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String linkdevideo = link.getText().toString();
                        String truth = searchController.isYouTubeUrl(linkdevideo,Add.this);
                        video = truth;
                        Log.i("LINK",video);
                        pp.dismiss();
                    }
                });
            }
        });
    }
    private void showImageSourceMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar imagen");
        builder.setItems(new CharSequence[]{"Tomar foto", "Escoger de la galería"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openCamera();
                                break;
                            case 1:
                                showFileChooser();
                                break;
                        }
                    }
                });
        builder.show();
    }
    private void handleIngresarButtonClick() {
        String codigo = codigoEditText.getText().toString();
        String categoria = categoriaEditText.getText().toString();
        String nombre = nombreEditText.getText().toString();
        String descripcion = descripcionEditText.getText().toString();
        String precio = precioEditText.getText().toString();
        String ubicacion = opcion();
        // Llama al método para enviar los datos al servidor
        searchController.enviarDatosAlServidorAdd(Add.this,("http://circulinasperu.com/RacerStore/insert.php"), codigo,categoria, nombre, descripcion, precio, video,ubicacion);
        Log.i("VARIABLE",video);
    }
// FUNCIONALIDAD DE INSERTR LINK DE VIDEO Y REPRODUCRI EL VIDEO 
    public String getStringImagen(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void uploadImage() {
        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo...", "Espere por favor");
        StringRequest stringRequest = new StringRequest(com.android.volley.Request.Method.POST, ("http://circulinasperu.com/RacerStore/uploaddg.php"),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Verificar si la actividad no está finalizada y la vista principal está adjunta al administrador de ventanas
                        if (!isFinishing() && mainLayout.getWindowToken() != null) {
                            loading.dismiss();
                            Toast.makeText(Add.this, response, Toast.LENGTH_LONG).show();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                loading.dismiss();
                Toast.makeText(Add.this, "ADVERTENCIA: No se adjunto imagen", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String imagen = getStringImagen(bitmap);
                String nombre = et.getText().toString().trim();
                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, imagen);
                params.put(KEY_NOMBRE, nombre);
                et.setText("");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen"), PICK_IMAGE_REQUEST);
    }
    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Crea un archivo donde se guardará la imagen
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Continúa si el archivo fue creado con éxito
            if (photoFile != null) {
                // Obtén la Uri del archivo y guarda la referencia en imageUri
                imageUri = FileProvider.getUriForFile(this,
                        "com.main.racerstore.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Crea un nombre único para el archivo basado en la fecha y hora actual
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        // Obtiene el directorio de almacenamiento externo (tarjeta SD)
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        // Crea el archivo donde se guardará la imagen
        File imageFile = File.createTempFile(
                imageFileName,  /* Nombre del archivo */
                ".jpg",         /* Extensión del archivo */
                storageDir      /* Directorio de almacenamiento */
        );

        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Imagen tomada con la cámara
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                iv.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String opcion(){
        opcs = "Ubicación del producto";
        if(opcs == (spinner.getSelectedItem().toString())){
            opcs=" ";
        }else{
            opcs=spinner.getSelectedItem().toString();
        }
        return opcs;
    }
}
