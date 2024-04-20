package org.cuatrovientos.blablacar.adapters;

        import android.content.Context;
        import android.graphics.Color;
        import android.graphics.PorterDuff;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.RelativeLayout;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import org.cuatrovientos.blablacar.R;
        import org.cuatrovientos.blablacar.models.DriverTrips;
        import org.cuatrovientos.blablacar.models.Rating;
        import org.cuatrovientos.blablacar.models.User;
        import org.cuatrovientos.blablacar.models.Utils;

        import java.text.SimpleDateFormat;
        import java.util.Date;
        import java.util.List;
        import java.util.Locale;

public class RecyclerUserTripDayAdapter extends RecyclerView.Adapter<RecyclerUserTripDayAdapter.TripViewHolder> {
    private List<DriverTrips> trips;
    private Context context;
    private OnItemClickListener listener;

    // Constructor
    public RecyclerUserTripDayAdapter(Context context, List<DriverTrips> trips, OnItemClickListener listener) {
        this.context = context;
        this.trips = trips;
        this.listener = listener;
    }

    // Interface for click events
    public interface OnItemClickListener {
        void onRateClick(DriverTrips trip);
        void onCancelClick(DriverTrips trip);
    }

    @NonNull
    @Override
    public TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_trips, parent, false);
        return new TripViewHolder(view, listener, trips);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        DriverTrips trip = trips.get(position);
        holder.assignData(trip, context);
    }

    @Override
    public int getItemCount() {
        return trips != null ? trips.size() : 0;
    }

    // ViewHolder class
    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView startTime, duration, arrivalTime, originCity, destinationCity, price, driverName, driverRating, noPassengersNotice;
        Button rateTripButton, cancelTripButton;
        TextView driverImage;
        RelativeLayout driverRatingLayout;
        RecyclerView recyclerViewTrayectos;

        public TripViewHolder(View itemView, final OnItemClickListener listener, final List<DriverTrips> trips) {
            super(itemView);
            // Initialize your views here
            startTime = itemView.findViewById(R.id.start_time);
            duration = itemView.findViewById(R.id.duration);
            arrivalTime = itemView.findViewById(R.id.arrival_time);
            originCity = itemView.findViewById(R.id.origin_city);
            destinationCity = itemView.findViewById(R.id.destination_city);
            price = itemView.findViewById(R.id.price);
            driverRatingLayout = itemView.findViewById(R.id.layout_driver_rating);
            driverImage = itemView.findViewById(R.id.driver_image);
            driverName = itemView.findViewById(R.id.driver_name);
            driverRating = itemView.findViewById(R.id.driver_rating);
            rateTripButton = itemView.findViewById(R.id.rate_trip_button);
            cancelTripButton = itemView.findViewById(R.id.cancel_trip_button);
            recyclerViewTrayectos = itemView.findViewById(R.id.recyclerViewTrayectos);
            noPassengersNotice = itemView.findViewById(R.id.noPassengersNotice);

            rateTripButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onRateClick(trips.get(position));
                }
            });

            cancelTripButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onCancelClick(trips.get(position));
                }
            });

        }

        public void assignData(DriverTrips route, Context context) {
            if (route.getUser() != null){
                driverImage.setText(route.getUser().getName().charAt(0) + "" + route.getUser().getLastName().charAt(0));
                driverImage.getBackground().setColorFilter(Color.parseColor("#" + route.getUser().getColor()), PorterDuff.Mode.SRC);

                recyclerViewTrayectos.setVisibility(View.GONE);
                noPassengersNotice.setVisibility(View.GONE);
                driverRatingLayout.setVisibility(View.VISIBLE);


                price.setText(String.valueOf("-" +route.getRoute().getPrice()) + "€");
                price.setTextColor(Color.RED);

                driverName.setText(route.getUser().getName());
                driverRating.setText(String.valueOf(route.getUser().getRating()));

                boolean alreadyRated = false;
                for (Rating rating : route.getUser().getRatings()) {
                    if (rating.getRoute().getId().equals(route.getRoute().getId()) && rating.getUserId().equals(route.getUser().getId())){
                        alreadyRated = true;
                    }
                }

                if (route.getRoute().getDate().after(new Date())) {
                    cancelTripButton.setVisibility(View.VISIBLE);
                    rateTripButton.setVisibility(View.GONE);
                } else if (route.getRoute().getDate().before(new Date()) && !alreadyRated) {
                    rateTripButton.setVisibility(View.VISIBLE);
                    cancelTripButton.setVisibility(View.GONE);
                }
            }else{
                recyclerViewTrayectos.setVisibility(View.VISIBLE);
                driverRatingLayout.setVisibility(View.GONE);

                price.setText(String.valueOf("+" + route.getRoute().getPrice() * route.getRoute().getPassengers().size()) + "€");
                price.setTextColor(Color.GREEN);

                recyclerViewTrayectos.setLayoutManager(new LinearLayoutManager(context));

                if (route.getRoute().getPassengers() != null) {
                    // Fetch user objects for the passenger IDs
                    Utils.getUsersByIds(route.getRoute().getPassengers(), new Utils.UsersCallback() {
                        @Override
                        public void onCallback(List<User> users) {
                            // Once users are fetched, set the RecyclerView adapter
                            if (users.size() == 0){
                                noPassengersNotice.setVisibility(View.VISIBLE);
                            }else{
                                noPassengersNotice.setVisibility(View.GONE);
                                recyclerViewTrayectos.setAdapter(new RecyclerTripsDetailsAdapter(users, new RecyclerTripsDetailsAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(User user) {
                                        // Handle item click events
                                    }
                                }));
                            }

                        }
                    });
                }
            }
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
        }

    }
}

