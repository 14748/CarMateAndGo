package org.cuatrovientos.blablacar.activities.search.adapters;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.TextView;
        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.RecyclerView;
        import org.cuatrovientos.blablacar.R;
        import org.cuatrovientos.blablacar.models.Rating;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.List;

public class RecyclerRatingAdapter extends RecyclerView.Adapter<RecyclerRatingAdapter.RatingViewHolder> {

    private List<Rating> ratingsList;

    public RecyclerRatingAdapter(List<Rating> ratingsList) {
        this.ratingsList = ratingsList;
    }

    @NonNull
    @Override
    public RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rating, parent, false);
        return new RatingViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingViewHolder holder, int position) {
        Rating rating = ratingsList.get(position);
        Utils.getUserById(rating.getUserId(), new Utils.FirebaseCallbackUser() {
            @Override
            public void onCallback(User user) {
                holder.ratingValue.setText(String.valueOf(rating.getValue()) + "★");
                holder.ratingComment.setText(rating.getComment());
                holder.username.setText(String.valueOf(user.getName()));
                holder.driverImage.setText(user.getName().charAt(0) + "" + user.getLastName().charAt(0));
                holder.driverImage.getBackground().setColorFilter(Color.parseColor("#" + user.getColor()), PorterDuff.Mode.SRC);
            }
        });

    }

    @Override
    public int getItemCount() {
        return ratingsList.size();
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        public TextView ratingValue, ratingComment, username, driverImage;

        public RatingViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            ratingValue = view.findViewById(R.id.ratingValue);
            ratingComment = view.findViewById(R.id.ratingComment);
            driverImage = view.findViewById(R.id.driver_image);
        }
    }
}
