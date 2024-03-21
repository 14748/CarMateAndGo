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
    private onLinkClickListener linkClickListener;

    private int selectedItemPosition = 0;

    public RecyclerRoutesAdapter(List<RouteSelectionInfo> listPalabras,
                                   onItemClickListener itemClickListener,
                                 onLinkClickListener linkClickListener) {
        this.listPalabras = listPalabras;
        this.itemClickListener = itemClickListener;
        this.linkClickListener = linkClickListener;
    }

    @NonNull
    @Override
    public RecyclerRoutesAdapter.RecyclerDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_available_routes, parent, false);
        return new RecyclerDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerRoutesAdapter.RecyclerDataHolder holder, int position) {
        holder.assignData(listPalabras.get(position), itemClickListener, linkClickListener);

        if(selectedItemPosition == holder.getBindingAdapterPosition()) {
            // This is the selected item
            holder.viewLink.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundResource(R.drawable.item_border_selected); // Use the border drawable for selected item
        } else {
            // This is not the selected item
            holder.viewLink.setVisibility(View.GONE);
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
        TextView viewLink;

        public RecyclerDataHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textViewTitle);
            kms = itemView.findViewById(R.id.textViewKilometers);
            time = itemView.findViewById(R.id.textViewTime);
            viewLink = itemView.findViewById(R.id.textViewLink);
        }

        public void assignData(RouteSelectionInfo palabra, onItemClickListener onItemClickListener, onLinkClickListener onLinkClickListener) {
            title.setText(palabra.getTitle());
            kms.setText(palabra.getKilometers());
            time.setText(palabra.getTime());

            itemView.setOnClickListener(view -> {
                if(itemClickListener != null) {
                    itemClickListener.onItemClickListener(palabra);
                }
            });

            // Set the click listener for the viewLink to handle link clicks
            viewLink.setOnClickListener(view -> {
                if(linkClickListener != null) {
                    // Use getAdapterPosition() to get the current position
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        linkClickListener.onLinkClickListener(position);
                    }
                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClickListener(RouteSelectionInfo palabra);
    }

    public interface onLinkClickListener {
        void onLinkClickListener(int position);
    }
}