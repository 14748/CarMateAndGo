package org.cuatrovientos.blablacar.adapters;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.RouteSelectionInfo;
import org.cuatrovientos.blablacar.models.User;

import java.util.List;


public class RecyclerTripsAdapter extends RecyclerView.Adapter<RecyclerTripsAdapter.RecyclerDataHolder> {

    private List<DriverTrips> listPalabras;
    private onItemClickListener itemClickListener;

    private int selectedItemPosition = 0;

    public RecyclerTripsAdapter(List<DriverTrips> listPalabras,
                                onItemClickListener itemClickListener) {
        this.listPalabras = listPalabras;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public RecyclerTripsAdapter.RecyclerDataHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trayecto, parent, false);
        return new RecyclerDataHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerTripsAdapter.RecyclerDataHolder holder, int position) {
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

        TextView user;
        TextView kms;
        TextView time;
        ImageView image;

        public RecyclerDataHolder(@NonNull View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.usernameTextView);
            kms = itemView.findViewById(R.id.kmTextView);
            time = itemView.findViewById(R.id.timeTextView);
            image = itemView.findViewById(R.id.avatarImageView);
        }

        public void assignData(DriverTrips palabra, onItemClickListener onItemClickListener) {
            user.setText(palabra.getUser().getName());
            kms.setText("to implement");
            time.setText("to implement");

            itemView.setOnClickListener(view -> onItemClickListener.onItemClickListener(palabra));
        }
    }

    public interface onItemClickListener {
        void onItemClickListener(DriverTrips palabra);
    }
}