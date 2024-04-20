package org.cuatrovientos.blablacar.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.CustomLatLng;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.RouteSelectionInfo;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class RecyclerTripsAdapter extends RecyclerView.Adapter<RecyclerTripsAdapter.RecyclerDataHolder> {

    private List<DriverTrips> listPalabras;
    private onItemClickListener itemClickListener;

    private int selectedItemPosition = 0;

    public RecyclerTripsAdapter(List<DriverTrips> listPalabras,
                                onItemClickListener itemClickListener) {
        this.listPalabras = listPalabras;
        this.itemClickListener = itemClickListener;
    }

    public void updateData(List<DriverTrips> newTrips) {
        this.listPalabras = newTrips;
        notifyDataSetChanged();
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
        TextView time1;
        TextView timeDiff;
        TextView time2;
        TextView origin;
        TextView destination;
        TextView username;
        TextView userExtra;
        TextView rating;
        TextView tripCost;
        TextView userLogo;

        public RecyclerDataHolder(@NonNull View itemView) {
            super(itemView);
            time1 = itemView.findViewById(R.id.start_time);
            timeDiff = itemView.findViewById(R.id.duration);
            time2 = itemView.findViewById(R.id.arrival_time);
            origin = itemView.findViewById(R.id.origin_city);
            destination = itemView.findViewById(R.id.destination_city);
            tripCost = itemView.findViewById(R.id.price);
            userLogo = itemView.findViewById(R.id.driver_image);
            username = itemView.findViewById(R.id.driver_name);
            rating = itemView.findViewById(R.id.driver_rating);

        }

        public void assignData(DriverTrips palabra, onItemClickListener onItemClickListener) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            // Format the start time to a String
            String startTimeStr = timeFormat.format(palabra.getRoute().getDate());
            time1.setText(startTimeStr);

            // Get the duration as a String
            String durationStr = palabra.getRoute().getDuration();
            timeDiff.setText(durationStr);

            userLogo.setText(palabra.getUser().getName().charAt(0) + "" + palabra.getUser().getLastName().charAt(0));
            userLogo.getBackground().setColorFilter(Color.parseColor("#" + palabra.getUser().getColor()), PorterDuff.Mode.SRC);

            try {
                String[] parts = durationStr.split(":");
                int hours = 0;
                int minutes = 0;

                try {
                    hours = Integer.parseInt(parts[0]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid hours format, setting to 0");
                }

                try {
                    minutes = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid minutes format, setting to 0");
                }

                long durationMillis = (hours * 3600 + minutes * 60) * 1000;

                long startTimeMillis = palabra.getRoute().getDate().getTime();
                long endTimeMillis = startTimeMillis + durationMillis;

                Date endTime = new Date(endTimeMillis);

                SimpleDateFormat timeFormatEnd = new SimpleDateFormat("HH:mm");
                String endTimeStr = timeFormatEnd.format(endTime);
                time2.setText(endTimeStr);
            } catch (Exception e) {
                e.printStackTrace();
                time2.setText("Error");
            }


            origin.setText(palabra.getRoute().getOriginText());
            destination.setText(palabra.getRoute().getDestinationText());



            if (palabra.getRoute().isFull()){
                tripCost.setText("Completo");
                itemView.setBackgroundColor(Color.parseColor("#E0E0E0"));
            }else{
                tripCost.setText(String.valueOf(palabra.getRoute().getPrice()) + "€");
            }

            //uselogo
            username.setText(palabra.getUser().getName());
            rating.setText(String.valueOf(palabra.getUser().getRating()));
            itemView.setOnClickListener(view -> onItemClickListener.onItemClickListener(palabra));

        }
    }

    public interface onItemClickListener {
        void onItemClickListener(DriverTrips palabra);
    }


}