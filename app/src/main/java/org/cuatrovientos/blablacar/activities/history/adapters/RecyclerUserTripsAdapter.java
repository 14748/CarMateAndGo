package org.cuatrovientos.blablacar.activities.history.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.history.RatingActivity;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RecyclerUserTripsAdapter extends RecyclerView.Adapter<RecyclerUserTripsAdapter.DayViewHolder> {
    private Context context;
    private Map<Date, List<DriverTrips>> groupedTripsByDate = new LinkedHashMap<>();

    private List<DriverTrips> allDriverTrips;

    public RecyclerUserTripsAdapter(Context context, List<DriverTrips> driverTripsList) {
        this.context = context;
        this.allDriverTrips = driverTripsList;
        groupDriverTripsByDate(driverTripsList);
    }

    private void groupDriverTripsByDate(List<DriverTrips> driverTripsList) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        groupedTripsByDate.clear();

        for (DriverTrips driverTrip : driverTripsList) {
            Date date = driverTrip.getRoute().getDate(); 
            try {
                
                Date normalizedDate = sdf.parse(sdf.format(date));
                if (!groupedTripsByDate.containsKey(normalizedDate)) {
                    groupedTripsByDate.put(normalizedDate, new ArrayList<>());
                }
                groupedTripsByDate.get(normalizedDate).add(driverTrip);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void updateData(List<DriverTrips> newDriverTripsList) {
        groupedTripsByDate.clear();
        groupDriverTripsByDate(newDriverTripsList);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_trips_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        List<Date> dates = new ArrayList<>(groupedTripsByDate.keySet());
        Date date = dates.get(position);
        List<DriverTrips> tripsForDate = groupedTripsByDate.get(date);

        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.dayTitle.setText(String.valueOf(dateFormat.format(date)));
        RecyclerUserTripDayAdapter tripAdapter = new RecyclerUserTripDayAdapter(context, tripsForDate, new RecyclerUserTripDayAdapter.OnItemClickListener() {
            @Override
            public void onRateClick(DriverTrips trip) {
                Intent rateIntent = new Intent(context, RatingActivity.class);
                rateIntent.putExtra("DRIVERTRIPS_KEY", trip);
                context.startActivity(rateIntent);
            }

            @Override
            public void onCancelClick(DriverTrips trip) {
                UserManager.init(context.getApplicationContext());
                User currentUser = UserManager.getCurrentUser();

                List<String> userIDs = trip.getRoute().getPassengers();
                if (userIDs == null) {
                    userIDs = new ArrayList<>();
                }
                userIDs.remove(currentUser.getId());

                User user = trip.getUser();
                user.getCreatedRoutes().get(trip.getUser().getCreatedRoutes().indexOf(trip.getRoute())).setPassengers(userIDs);

                float newBalance = currentUser.getBalance() + trip.getRoute().getPrice();
                currentUser.setBalance(newBalance);

                currentUser.removePassengerRoute(trip.getRoute().getId());

                Utils.pushUser(currentUser);
                Utils.pushUser(user);
                UserManager.setCurrentUser(currentUser);

                allDriverTrips.remove(trip);
                updateData(allDriverTrips);
            }
        });
        holder.dayTripsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.dayTripsRecyclerView.setAdapter(tripAdapter);
    }

    @Override
    public int getItemCount() {
        return groupedTripsByDate.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {
        TextView dayTitle;
        RecyclerView dayTripsRecyclerView;

        DayViewHolder(View itemView) {
            super(itemView);
            dayTitle = itemView.findViewById(R.id.dayTitle);
            dayTripsRecyclerView = itemView.findViewById(R.id.dayTripsRecyclerView);
        }
    }
}
