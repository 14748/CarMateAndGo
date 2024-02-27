package org.cuatrovientos.blablacar.adapters;

import static android.widget.Toast.makeText;

import static androidx.constraintlayout.widget.ConstraintLayoutStates.TAG;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.ORS.Route;
import org.cuatrovientos.blablacar.models.PlaceOpenStreetMap;

import java.util.List;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RutaViewHolder> {
    private List<PlaceOpenStreetMap> listaRutas;
    private OnItemClickListener onItemClickListener;

    public RouteAdapter(List<PlaceOpenStreetMap> listaRutas) {
        this.listaRutas = listaRutas;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    @NonNull
    @Override
    public RutaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_route, parent, false);
        return new RutaViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RutaViewHolder holder, int position) {
        PlaceOpenStreetMap ruta = listaRutas.get(position);
        holder.nombreTextView.setText(ruta.getDisplayName());

        // Manejar clic en el elemento
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = holder.getAbsoluteAdapterPosition();
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(adapterPosition);
                }
            }
        });
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return listaRutas.size();
    }

    public static class RutaViewHolder extends RecyclerView.ViewHolder {
        public TextView nombreTextView;
        public TextView latitudTextView;
        public TextView longitudTextView;

        public RutaViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.nombreTextView);
        }
    }
}
