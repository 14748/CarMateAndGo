package org.cuatrovientos.blablacar.activities.ban.adapter;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.User;

import java.util.List;
public class RecyclerUserBanAdapter extends RecyclerView.Adapter<RecyclerUserBanAdapter.UsersDetailsHolder> {

    private List<User> userList;
    private OnItemClickListener itemClickListener;

    public RecyclerUserBanAdapter(List<User> userList, OnItemClickListener itemClickListener) {
        this.userList = userList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public UsersDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passengers, parent, false);
        return new UsersDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersDetailsHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user);

        // Set click listener for the itemView
        holder.itemView.setOnClickListener(v -> {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(user);
            }
        });

        // Set click listener for the Ban User button
        holder.banButton.setOnClickListener(v -> {
            if(itemClickListener != null) {
                itemClickListener.onBanClick(user);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UsersDetailsHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView imageProfile;
        Button banButton;

        public UsersDetailsHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            imageProfile = itemView.findViewById(R.id.image_profile);
            banButton = itemView.findViewById(R.id.btn_ban_user);

            imageProfile.getBackground().setColorFilter(Color.parseColor("#" + "CCCCCC"), PorterDuff.Mode.SRC);
        }

        public void bind(User user) {
            textName.setText(user.getName() + " " + user.getLastName());
            imageProfile.setText(user.getName().charAt(0) + "" + user.getLastName().charAt(0));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
        void onBanClick(User user);
    }
}