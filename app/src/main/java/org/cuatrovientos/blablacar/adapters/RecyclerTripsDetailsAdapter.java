package org.cuatrovientos.blablacar.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.List;

public class RecyclerTripsDetailsAdapter extends RecyclerView.Adapter<RecyclerTripsDetailsAdapter.TripsDetailsHolder> {

    private List<User> userList;
    private OnItemClickListener itemClickListener;

    public RecyclerTripsDetailsAdapter(List<User> userList, OnItemClickListener itemClickListener) {
        this.userList = userList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public TripsDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passengers, parent, false);
        return new TripsDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsDetailsHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);

        holder.itemView.setOnClickListener(v -> {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class TripsDetailsHolder extends RecyclerView.ViewHolder {

        TextView textName;
        TextView imageProfile;

        public TripsDetailsHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            imageProfile = itemView.findViewById(R.id.image_profile);
        }

        public void bind(User user) {
            textName.setText(String.valueOf(user.getName() + " " +  user.getLastName()));
            imageProfile.setText(user.getName().charAt(0) + "" + user.getLastName().charAt(0));
            imageProfile.getBackground().setColorFilter(Color.parseColor("#" + Utils.getRandomColor()), PorterDuff.Mode.SRC);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
    }
}