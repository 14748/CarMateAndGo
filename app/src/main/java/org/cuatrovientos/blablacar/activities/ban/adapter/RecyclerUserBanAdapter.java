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
    private List<String> bannedUserIds;

    public RecyclerUserBanAdapter(List<User> userList, List<String> bannedUserIds, OnItemClickListener itemClickListener) {
        this.userList = userList;
        this.itemClickListener = itemClickListener;
        this.bannedUserIds = bannedUserIds;
    }

    public void banUser(User user) {
        bannedUserIds.add(user.getId());
        notifyItemChanged(userList.indexOf(user));
    }

    public void unbanUser(User user) {
        bannedUserIds.remove(user.getId());
        notifyItemChanged(userList.indexOf(user));
    }

    @NonNull
    @Override
    public UsersDetailsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_passenger_ban, parent, false);
        return new UsersDetailsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersDetailsHolder holder, int position) {
        User user = userList.get(position);
        holder.bind(user, bannedUserIds.contains(user.getId()));

        
        holder.itemView.setOnClickListener(v -> {
            if(itemClickListener != null) {
                itemClickListener.onItemClick(user);
            }
        });

        
        holder.banButton.setOnClickListener(v -> {
            if(itemClickListener != null) {
                itemClickListener.onBanClick(user);
            }
        });

        holder.unBanButton.setOnClickListener(v -> {
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
        Button banButton, unBanButton;

        public UsersDetailsHolder(@NonNull View itemView) {
            super(itemView);
            textName = itemView.findViewById(R.id.text_name);
            imageProfile = itemView.findViewById(R.id.image_profile);
            banButton = itemView.findViewById(R.id.btn_ban_user);
            unBanButton = itemView.findViewById(R.id.btn_unban_user);

            imageProfile.getBackground().setColorFilter(Color.parseColor("#" + "CCCCCC"), PorterDuff.Mode.SRC);
        }

        public void bind(User user, boolean isBanned) {
            textName.setText(user.getName() + " " + user.getLastName());
            imageProfile.setText(user.getName().charAt(0) + "" + user.getLastName().charAt(0));
            if (isBanned) {
                banButton.setVisibility(View.GONE);
                unBanButton.setVisibility(View.VISIBLE);
            } else {
                banButton.setVisibility(View.VISIBLE);
                unBanButton.setVisibility(View.GONE);
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(User user);
        void onBanClick(User user);
    }
}