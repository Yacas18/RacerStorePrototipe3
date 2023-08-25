package com.main.racerstore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.util.List;
import com.airbnb.lottie.LottieAnimationView;
public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private SearchController searchController;
    public ProductAdapter(List<Product> productList, Context context, SearchController searchController) {
        this.productList = productList;
        this.context = context;
        this.searchController = searchController;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Obtener los datos del producto en la posición actual
        String actpa="";
        Product producto = productList.get(position);
        if(!TextUtils.isEmpty(producto.getUbicacion())){
            actpa = producto.getUbicacion();
            holder.tvUbicacion.setTextColor(ContextCompat.getColor(context,R.color.black));
        }else {
            actpa = "Ubicacion no disponible";
        }
        // Asignar los datos a las vistas del ViewHolder
        holder.tvCodigo.setText("Código: " + producto.getCodigo());
        holder.tvCategoria.setText("Categoría: " + producto.getCategoria());
        holder.tvNombre.setText("Nombre: " + producto.getNombre());
        holder.tvDescripcion.setText("Descripción: " + producto.getDescripcion());
        holder.tvPrecio.setText("Precio: S/" + producto.getPrecio());
        holder.tvUbicacion.setText("Ubicación: " + actpa);
        // Cargar la imagen con Picasso
        Picasso.get().load(producto.getGlobalVariable()+producto.getImgrt()).placeholder(R.drawable.imagen).into(holder.ivProductImage);
        // Asignar el OnClickListener para reproducir el video
        holder.ivProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CLICK EN IMAGEN METO((getImgrt()) == 1 || (getImgrt()) == null || (getImgrt()).equals(""))
                if(producto.getImgrt()==("1")){
                    Toast.makeText(context, "Producto sin imagen", Toast.LENGTH_SHORT).show();
                }else{
                    Dialog popupDialog = new Dialog(context);
                    popupDialog.setContentView(R.layout.item_image);
                    ImageView imagdd = popupDialog.findViewById(R.id.imageView);
                    searchController.imagepopup((producto.getGlobalVariable()+producto.getImgrt()), imagdd);
                    popupDialog.show();
                }
            }
        });
        holder.botonlocate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String UBI = producto.getUbicacion();
                if (!TextUtils.isEmpty(UBI)){
                    String plocation= producto.getUbicacion();
                    Dialog popupDialog = new Dialog(context);
                    popupDialog.setContentView(R.layout.popup_image);
                    RelativeLayout popup = popupDialog.findViewById(R.id.layu);
                    TextView textnamel = popupDialog.findViewById(R.id.productolocationm);
                    ImageView imagennn = popupDialog.findViewById(R.id.locateimagep);
                    TextView textname2 = popupDialog.findViewById(R.id.locate);
                    LottieAnimationView loti = popupDialog.findViewById(R.id.imageView);
                    PhotoView imagenmap = popupDialog.findViewById(R.id.ivImage);
                    searchController.mostrarLocate(textnamel,producto.getNombre(),textname2,producto.getUbicacion(),(producto.getGlobalVariable()+producto.getImgrt()),imagennn);

                        float zoomLevel;

                        switch (producto.getUbicacion()) {
                            case "ANAQUEL 1":
                                imagenmap.setScale(3, 500,30, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 2":
                                imagenmap.setScale(3, 500,0, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 3":
                                imagenmap.setScale(3, 1000,0, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 4":
                                imagenmap.setScale(3, 1400,0, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 5":
                                imagenmap.setScale(3, 1600,0, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 6":
                                imagenmap.setScale(3, 1400,100, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 7":
                                imagenmap.setScale(3, 1000,100, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 8":
                                imagenmap.setScale(3, 1000,100, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 9":
                                imagenmap.setScale(3, 1400,100, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 10":
                                imagenmap.setScale(3, 1400,180, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 11":
                                imagenmap.setScale(3, 1400,250, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 12":
                                imagenmap.setScale(3, 1000,250, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 13":
                                imagenmap.setScale(3, 0,250, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 14":
                                imagenmap.setScale(3, 100,300, true); // Con animación y centrado en (x, y)
                                break;
                            case "ANAQUEL 15":
                                imagenmap.setScale(3, 100,350, true); // Con animación y centrado en (x, y)
                                break;
                            case "COCINA":
                                imagenmap.setScale(3, 0,30, true); // Con animación y centrado en (x, y)
                                break;
                            case "ZONA OSCURA":
                                imagenmap.setScale(3, 0,350, true); // Con animación y centrado en (x, y)
                                break;
                            case "TRUPAN 1":
                                imagenmap.setScale(3, 400,400, true); // Con animación y centrado en (x, y)
                                break;
                            case "TRUPAN 2":
                                imagenmap.setScale(3, 400,250, true); // Con animación y centrado en (x, y)
                                break;
                            case "TRUPAN 3":
                                imagenmap.setScale(3, 1000,300, true); // Con animación y centrado en (x, y)
                                break;
                            case "TRUPAN 4":
                                imagenmap.setScale(3, 1100,700, true); // Con animación y centrado en (x, y)
                                break;
                            case "TRUPAN 5":
                                imagenmap.setScale(3, 400,700, true); // Con animación y centrado en (x, y)
                                break;
                            case "VITRINA 1":
                                imagenmap.setScale(3, 700,500, true); // Con animación y centrado en (x, y)
                                break;
                            case "VITRINA 2":
                                imagenmap.setScale(3, 100,700, true); // Con animación y centrado en (x, y)
                                break;
                            case "VITRINA 3":
                                imagenmap.setScale(3, 100,600, true); // Con animación y centrado en (x, y)
                                break;
                            case "VITRINA 4":
                                imagenmap.setScale(3, 0,600, true); // Con animación y centrado en (x, y)
                                break;
                            case "VITRINA 5":
                                imagenmap.setScale(3, 0,700, true); // Con animación y centrado en (x, y)
                                break;
                        }
                    popup.setScaleX(0);
                    popup.setScaleY(0);
                    popup.animate().scaleX(1).scaleY(1).setDuration(300).start();
                    popupDialog.show();
                }else{
                    Toast.makeText(context, "No definida", Toast.LENGTH_SHORT).show();
                }
            }
        });
    holder.playingvid.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Obtener el enlace del video desde el objeto Product
            String videoURL = producto.getVideoURL();
            if (!TextUtils.isEmpty(videoURL)) {
                // Mostrar el reproductor de video utilizando el SearchController
                searchController.showVideoDialog(videoURL, context);
            } else {
                Toast.makeText(context, "Video no disponible", Toast.LENGTH_SHORT).show();
            }
        }
    });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvCodigo;
        TextView tvCategoria;
        TextView tvNombre;
        TextView tvDescripcion;
        TextView tvPrecio;
        TextView tvUbicacion;
        ImageView ivProductImage;
        Button botonlocate;
        Button playingvid;
        ImageView imagdd;
        public ProductViewHolder(View itemView) {
            super(itemView);
            // Vincular las vistas con los elementos del layout
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            botonlocate = itemView.findViewById(R.id.locate);
            playingvid = itemView.findViewById(R.id.playingvid);
            imagdd = itemView.findViewById(R.id.imageView);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
        }
    }
}
