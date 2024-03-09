package org.cuatrovientos.blablacar.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;

        import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.RouteEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RecyclerUserTripDayAdapter extends RecyclerView.Adapter<RecyclerUserTripDayAdapter.TripViewHolder> {
    private List<DriverTrips> trips;

    public RecyclerUserTripDayAdapter(List<DriverTrips> trips) {
        this.trips = trips;
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_trips, parent, false);
        return new TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        DriverTrips trip = trips.get(position);
        holder.assignData(trip);
    }

    @Override
    public int getItemCount() {
        return trips.size();
    }

    static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView startTime, duration, arrivalTime, originCity, destinationCity, price, driverName, driverRating;
        Button rate_trip_button, cancel_trip_button;
        // Initialize other views as needed, such as driverImage, cancelTripButton, rateTripButton

        TripViewHolder(View itemView) {
            super(itemView);
            startTime = itemView.findViewById(R.id.start_time);
            duration = itemView.findViewById(R.id.duration);
            arrivalTime = itemView.findViewById(R.id.arrival_time);
            originCity = itemView.findViewById(R.id.origin_city);
            destinationCity = itemView.findViewById(R.id.destination_city);
            price = itemView.findViewById(R.id.price);
            driverName = itemView.findViewById(R.id.driver_name);
            driverRating = itemView.findViewById(R.id.driver_rating);
            rate_trip_button = itemView.findViewById(R.id.rate_trip_button);
            cancel_trip_button = itemView.findViewById(R.id.cancel_trip_button);
        }
        public void assignData(DriverTrips route) {
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

            // Format the start time to a String
            String startTimeStr = timeFormat.format(route.getRoute().getDate());
            startTime.setText(startTimeStr);

            // Get the duration as a String
            String durationStr = route.getRoute().getDuration();
            duration.setText(durationStr);

            try {
                // Split the duration string into hours and minutes
                String[] parts = durationStr.split(":");
                int hours = Integer.parseInt(parts[0]);
                int minutes = Integer.parseInt(parts[1]);

                // Convert the duration to milliseconds
                long durationMillis = (hours * 3600 + minutes * 60) * 1000;

                // Add the duration to the start date's time
                long startTimeMillis = route.getRoute().getDate().getTime();
                long endTimeMillis = startTimeMillis + durationMillis;

                // Create a new Date object for the end time
                Date endTime = new Date(endTimeMillis);

                // Format the end time to a String
                String endTimeStr = timeFormat.format(endTime);
                arrivalTime.setText(endTimeStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                arrivalTime.setText("Error");
            }


            originCity.setText(route.getRoute().getOriginText());
            destinationCity.setText(route.getRoute().getDestinationText());


            price.setText(String.valueOf(route.getRoute().getPrice()) + "€");

            driverName.setText(route.getUser().getName());
            driverRating.setText(String.valueOf(route.getUser().getRating()));

            if (route.getRoute().getDate().after(new Date())) {
                cancel_trip_button.setVisibility(View.VISIBLE);
                rate_trip_button.setVisibility(View.GONE);
            } else {
                rate_trip_button.setVisibility(View.VISIBLE);
                cancel_trip_button.setVisibility(View.GONE);
            }

        }
    }
}

