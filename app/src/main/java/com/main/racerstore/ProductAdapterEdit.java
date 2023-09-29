package com.main.racerstore;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.relex.photodraweeview.PhotoDraweeView;

public class ProductAdapterEdit extends RecyclerView.Adapter<ProductAdapterEdit.ProductViewHolder> {

    private List<Product> productList;
    private Context context;
    private SearchController searchController;
    private OnProductClickListener onProductClickListener;

    // Interfaz para el clic del producto
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    // Setter para el listener del clic del producto
    public void setOnProductClickListener(OnProductClickListener listener) {
        onProductClickListener = listener;
    }

    public ProductAdapterEdit(List<Product> productList, Context context, SearchController searchController) {
        this.productList = productList;
        this.context = context;
        this.searchController = searchController;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_edit, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        String ubi;
        if(!TextUtils.isEmpty(product.getUbicacion())){
            ubi = product.getUbicacion();
        }else{
            ubi = "No definida";
        }
        holder.tvCodigo.setText("Código: " + product.getCodigo());
        holder.tvCategoria.setText("Categoría: " + product.getCategoria());
        holder.tvNombre.setText("Nombre: " + product.getNombre());
        holder.tvDescripcion.setText("Descripción: " + product.getDescripcion());
        holder.tvUbicacion.setText("Ubicación: "+ ubi);
        holder.tvPrecio.setText("Precio: S/" + product.getPrecio());

        Uri link = Uri.parse(product.getImgrt());
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setUri(link)
                .setAutoPlayAnimations(true)
                .build();
        holder.ivProductImage.setController(controller);
        // Agregar el OnClickListener para el producto
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onProductClickListener != null) {
                    onProductClickListener.onProductClick(product);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private PhotoDraweeView ivProductImage;
        private TextView tvCodigo;
        private TextView tvCategoria;
        private TextView tvNombre;
        private TextView tvDescripcion;
        private TextView tvPrecio;
        private TextView tvUbicacion;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProductImage = itemView.findViewById(R.id.ivProductImage);
            tvCodigo = itemView.findViewById(R.id.tvCodigo);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvUbicacion = itemView.findViewById(R.id.tvUbicacion);
        }
    }
}
