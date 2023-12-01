package com.main.racerstore;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductCardView2 extends CardView {
    Uri imageUri;
    String nombre, descript, catego, pricep;
    private SearchActivity searchActivity;
    private  ProductAdapter productAdapter;

    public ProductCardView2(Context context, String imageUrl, String productName, String categoria, String descripcion, String precio, SearchActivity searchActivity) {
        super(context);
        this.searchActivity = searchActivity; // Inicializa la referencia

        // Configura el diseño y elevación de la CardView
        LayoutParams cardParams = new LayoutParams(
                400,
                400
        );
        cardParams.setMargins(16, 16, 16, 16);
        setLayoutParams(cardParams);
        setCardElevation(4);
        setRadius(8);
        setClickable(true);

        // Crea un nuevo RelativeLayout como contenedor
        RelativeLayout container = new RelativeLayout(context);

        // Crea un nuevo ImageView para mostrar la imagen
        ImageView image = new ImageView(context);

        // Configura la URI de la imagen (reemplaza "tu_url_de_imagen" con tu URL real)
        imageUri = Uri.parse(imageUrl);
        nombre = productName;
        catego = categoria;
        descript = descripcion;
        pricep = precio;

        // Configura el controlador de Fresco para cargar la imagen
        Picasso.get().load(imageUri).into(image);

        // Configura los parámetros de la ImageView para estar en la parte inferior
        RelativeLayout.LayoutParams imageParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        imageParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        image.setLayoutParams(imageParams);

        // Agrega la ImageView al RelativeLayout
        container.addView(image);

        // Agrega el RelativeLayout (contenedor) a la CardView
        addView(container);

        // Configura el OnClickListener para abrir el diálogo
        container.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí puedes abrir el diálogo o realizar la acción deseada
                dialogoproduc();
            }
        });
    }

    public void dialogoproduc() {
        Dialog ppo = new Dialog(searchActivity);
        ppo.setContentView(R.layout.dialogp);
        ImageView img = ppo.findViewById(R.id.imageProducto);
        TextView name = ppo.findViewById(R.id.tvNombre);
        TextView cate = ppo.findViewById(R.id.tvCategoria);
        TextView descrip = ppo.findViewById(R.id.tvDescripcion);
        TextView price = ppo.findViewById(R.id.tvPrecio);
        ImageView lista = ppo.findViewById(R.id.list);
        searchActivity.isAutoScrollEnabled2 = false;
        name.setText(nombre);
        cate.setText(catego);
        descrip.setText(descript);
        price.setText("S/ " + pricep);
        Picasso.get().load(imageUri).into(img);
        ppo.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                // Restablece el estado de isAutoScrollEnabled a true
                searchActivity.isAutoScrollEnabled2 = true;
            }
        });
        ppo.show();

        lista.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = catego;
                Intent intent = new Intent(searchActivity, Result.class);
                intent.putExtra("searchTerm", searchTerm);
                searchActivity.startActivity(intent); // Usa el contexto de searchActivity para iniciar la nueva actividad

                // Realiza la búsqueda del elemento en el adaptador y desplázate hasta él
            }

        });
    }
}
