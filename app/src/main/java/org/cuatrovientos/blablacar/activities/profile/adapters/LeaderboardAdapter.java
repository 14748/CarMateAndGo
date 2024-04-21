package org.cuatrovientos.blablacar.activities.profile.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.User;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private List<User> userList;

    public LeaderboardAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leaderboard_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvRank.setText(String.valueOf(position + 1));
        holder.tvUserName.setText(user.getName() + " " + user.getLastName());
        holder.tvCO2Reduction.setText(String.format("%.2f", user.getC02Reduction()) + " kg");
        holder.ivAvatar.setText(user.getName().charAt(0) + "" + user.getLastName().charAt(0));
        holder.ivAvatar.getBackground().setColorFilter(Color.parseColor("#" + user.getColor()), PorterDuff.Mode.SRC);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvUserName, tvCO2Reduction, ivAvatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvCO2Reduction = itemView.findViewById(R.id.tvCO2Reduction);
            ivAvatar = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
