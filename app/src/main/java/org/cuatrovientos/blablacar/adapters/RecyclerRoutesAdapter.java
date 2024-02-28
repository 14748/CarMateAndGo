package org.cuatrovientos.blablacar.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.ORS.Route;
import org.cuatrovientos.blablacar.models.RouteSelectionInfo;

import java.util.List;


public class RecyclerRoutesAdapter extends RecyclerView.Adapter<RecyclerRoutesAdapter.RecyclerDataHolder> {

    private List<RouteSelectionInfo> listPalabras;
    private onItemClickListener itemClickListener;

    private int selectedItemPosition = 0;

    public RecyclerRoutesAdapter(List<RouteSelectionInfo> listPalabras,
                                   onItemClickListener itemClickListener) {
        this.listPalabras = listPalabras;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerRoutesAdapter.RecyclerDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_routes, parent, false);
        return new RecyclerDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerRoutesAdapter.RecyclerDataHolder holder, int position) {
        holder.assignData(listPalabras.get(position), itemClickListener);

        if(selectedItemPosition == holder.getBindingAdapterPosition()) {
            // This is the selected item
            holder.itemView.setBackgroundResource(R.drawable.item_border_selected); // Use the border drawable for selected item
        } else {
            // This is not the selected item
            holder.itemView.setBackgroundResource(R.drawable.item_border); // Remove background or set default
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int previousItem = selectedItemPosition;
                selectedItemPosition = holder.getBindingAdapterPosition();

                // Refresh UI for previously selected and newly selected items
                notifyItemChanged(previousItem);
                notifyItemChanged(selectedItemPosition);

                // Invoke the custom listener passed into the adapter
                if(itemClickListener != null) {
                    itemClickListener.onItemClickListener(listPalabras.get(selectedItemPosition));
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return listPalabras.size();
    }

    public class RecyclerDataHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView kms;
        TextView time;

        public RecyclerDataHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            kms = itemView.findViewById(R.id.textViewKilometers);
            time = itemView.findViewById(R.id.textViewTime);
        }

        public void assignData(RouteSelectionInfo palabra, onItemClickListener onItemClickListener) {
            title.setText(palabra.getTitle());
            kms.setText(palabra.getKilometers());
            time.setText(palabra.getTime());

            itemView.setOnClickListener(view -> onItemClickListener.onItemClickListener(palabra));
        }
    }

    public interface onItemClickListener {
        void onItemClickListener(RouteSelectionInfo palabra);
    }
}