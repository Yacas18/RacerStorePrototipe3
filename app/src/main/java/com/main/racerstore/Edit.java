package com.main.racerstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;

import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.relex.photodraweeview.PhotoDraweeView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Edit extends AppCompatActivity implements ProductAdapterEdit.OnProductClickListener {
    public Context context;
    private boolean isPlaying = false;
    private boolean isButtonEnabled = true;
    private SearchController searchController;
    private static final String TAG = "Edit";
    private ProductAdapterEdit productAdapter;
    private List<Product> productList = new ArrayList<>();
    private String cod = "";
    public String linkdeyutu = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editp);
        this.context=context;
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        productAdapter = new ProductAdapterEdit(productList, this, searchController);
        productAdapter.setOnProductClickListener(this);
        recyclerView.setAdapter(productAdapter);
        // ...

        // Obtén la instancia de la clase SearchController
        searchController = new SearchController(this);
        // ...
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // No es necesario implementar esta parte en la clase Edit
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Realizar la búsqueda en tiempo real
                searchController.searchProductsEdit();
                return true;
            }
        });
    }

    public String getSearchTerm() {
        SearchView searchView = findViewById(R.id.searchView); // Reemplaza "searchView" con el ID correcto de tu SearchView
        return searchView.getQuery().toString();
    }

    public void showSearchResults(JSONArray jsonArray) {
        RelativeLayout anim = findViewById(R.id.rela1);
        anim.setVisibility(View.GONE);
        try {
            productList.clear();

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
                Product product = new Product(codigo, categoria, nombre, descripcion, precio, imgrt, videoURL,ubicacion);
                productList.add(product);
                cod = codigo;
            }

            productAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Implementación del método onProductClick para editar el producto
    @Override
    public void onProductClick(Product product) {
        // Crear un Dialog personalizado
        Dialog popupDialog = new Dialog(this);
        popupDialog.setContentView(R.layout.popud_addp);
        String [] option = {product.getUbicacion(),"ANAQUEL 1","ANAQUEL 2","ANAQUEL 3","ANAQUEL 4",
                "ANAQUEL 5", "ANAQUEL 6", "ANAQUEL 7","ANAQUEL 8","ANAQUEL 9","ANAQUEL 10","ANAQUEL 11","ANAQUEL 12"
                ,"ANAQUEL 13","ANAQUEL 14","ANAQUEL 15","COCINA","TRUPAN 1","TRUPAN 2","TRUPAN 3"
                ,"TRUPAN 4","TRUPAN 5","ZONA OSCURA", "VITRINA 1","VITRINA 2","VITRINA 3","VITRINA 4", "VITRINA 5"};
        // Obtén las referencias a los elementos de la ventana emergente
        ViewSwitcher viewSwitcher = popupDialog.findViewById(R.id.viewSwitchered);
        LottieAnimationView animationView = popupDialog.findViewById(R.id.animationView);
        ViewSwitcher viewSwitcherdel = popupDialog.findViewById(R.id.viewSwitcherdel);
        LottieAnimationView animationView2 = popupDialog.findViewById(R.id.animationView2);
        Button EditB = popupDialog.findViewById(R.id.intodate);
        EditText codigoEditText = popupDialog.findViewById(R.id.icodigo);
        EditText categoriaEditText = popupDialog.findViewById(R.id.icategoria);
        EditText nombreEditText = popupDialog.findViewById(R.id.inombre);
        EditText descripcionEditText = popupDialog.findViewById(R.id.idescripcion);
        EditText precioEditText = popupDialog.findViewById(R.id.iprecio);
        TextView idcodigo = popupDialog.findViewById(R.id.codigoact);
        Button deletep = popupDialog.findViewById(R.id.delete);
        Button videoo = popupDialog.findViewById(R.id.lik);
        PhotoDraweeView imageView = popupDialog.findViewById(R.id.imagelog);
        Spinner ubi = popupDialog.findViewById(R.id.spinn);
        // Establecer los datos del producto en los elementos correspondientes de la ventana emergente
        idcodigo.setText(product.getCodigo());
        codigoEditText.setText(product.getCodigo());
        categoriaEditText.setText(product.getCategoria());
        nombreEditText.setText(product.getNombre());
        descripcionEditText.setText(product.getDescripcion());
        precioEditText.setText(product.getPrecio());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,option);
        ubi.setAdapter(adapter);
        Uri link = Uri.parse(product.getImgrt());
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(link)
                .setAutoPlayAnimations(true)
                .build();
        imageView.setController(controller);

        videoo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog editl = new Dialog(Edit.this);
                editl.setContentView(R.layout.inpurledt);
                EditText youtubeedt = editl.findViewById(R.id.youtubedt);
                Button saveedt = editl.findViewById(R.id.saveedt);
                Button ver = editl.findViewById(R.id.view);
                String videoproducto = "https://www.youtube.com/watch?v="+(product.getVideoURL());
                if ((product.getVideoURL())==""){
                    youtubeedt.setText("");
                    ver.setVisibility(View.GONE);
                }else {
                    youtubeedt.setText(videoproducto);
                }
                saveedt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        linkdeyutu = searchController.isYouTubeUrl(youtubeedt.getText().toString(),Edit.this);
                        Toast.makeText(Edit.this, "Se registro link", Toast.LENGTH_SHORT).show();
                        Log.i("ID DE VIDEO",linkdeyutu);
                        editl.dismiss();
                    }
                });
                ver.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Edit.this,"Cargando video",Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder builder = new AlertDialog.Builder(Edit.this);
                        LayoutInflater inflater = LayoutInflater.from(Edit.this);
                        View dialogView = inflater.inflate(R.layout.player_video, null);
                        builder.setView(dialogView);

                        WebView webView = dialogView.findViewById(R.id.playerView);
                        String codvid = product.getVideoURL();
                        String video = "<iframe width=\"350sp\" height=\"250sp\" src=\"https://www.youtube.com/embed/"+codvid+"?autoplay=1&controls=0&showinfo=0&rel=0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture\" allowfullscreen></iframe>";
                        webView.loadData(video,"text/html","utf-8");
                        webView.getSettings().setJavaScriptEnabled(true);
                        webView.setWebChromeClient(new WebChromeClient());
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
                editl.show();
            }
        });
        EditB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchController searchController = new SearchController(Edit.this);
                String id = idcodigo.getText().toString();
                String codigo = codigoEditText.getText().toString();
                String categoria = categoriaEditText.getText().toString();
                String nombre = nombreEditText.getText().toString();
                String descripcion = descripcionEditText.getText().toString();
                String precio = precioEditText.getText().toString();
                String videoURL = linkdeyutu;
                String ubicacion = ubi.getSelectedItem().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(Edit.this);
                builder.setTitle("Confirmación");
                builder.setMessage("Estas modificando la información del producto ¿Estas seguro?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acciones a realizar si se confirma (Sí)
                        searchController.enviarDatosAlServidorEdit(Edit.this,("http://circulinasperu.com/RacerStore/editp.php"),id, codigo,categoria, nombre, descripcion, precio, videoURL, ubicacion);
                        viewSwitcher.showNext();
                        animationView.addAnimatorListener(new Animator.AnimatorListener(){
                            @Override
                            public void onAnimationStart(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // La animación ha terminado, aquí puedes realizar las acciones adicionales
                                // como cerrar el layout, mostrar el popupDialog y reiniciar la actividad
                                popupDialog.dismiss();
                                Intent intent = getIntent(); // Obtener el intento actual
                                finish(); // Finalizar la actividad actual
                                startActivity(intent); // Iniciar la actividad nuevamente
                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {

                            }
                        });
                        animationView.playAnimation();
                    }
                });

                // Configura el botón negativo (No)
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acciones a realizar si se cancela (No)
                        dialog.dismiss(); // Cierra el diálogo
                    }
                });

                // Muestra el diálogo
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });

        deletep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Edit.this);
                builder.setTitle("Confirmación");
                builder.setMessage("Estas eliminando el producto de la faz de la tierra¿Estas seguro?");
                builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Acciones a realizar si se confirma (Sí)
                        SearchController searchController = new SearchController(Edit.this);
                        searchController.eliminarProducto(Edit.this,cod);
                        viewSwitcherdel.showNext();
                        animationView2.addAnimatorListener(new Animator.AnimatorListener(){
                            @Override
                            public void onAnimationStart(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                // La animación ha terminado, aquí puedes realizar las acciones adicionales
                                // como cerrar el layout, mostrar el popupDialog y reiniciar la actividad
                                popupDialog.dismiss();
                                Intent intent = getIntent(); // Obtener el intento actual
                                finish(); // Finalizar la actividad actual
                                startActivity(intent); // Iniciar la actividad nuevamente
                            }

                            @Override
                            public void onAnimationCancel(@NonNull Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(@NonNull Animator animation) {

                            }
                        });
                        animationView2.playAnimation();
                    }
                });

                // Configura el botón negativo (No)
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acciones a realizar si se cancela (No)
                        dialog.dismiss(); // Cierra el diálogo
                    }
                });

                // Muestra el diálogo
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        popupDialog.show();
    }

}
