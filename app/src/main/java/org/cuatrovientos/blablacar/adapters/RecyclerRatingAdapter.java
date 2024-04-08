package org.cuatrovientos.blablacar.adapters;
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

import okhttp3.internal.Util;

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
            }
        });

    }

    @Override
    public int getItemCount() {
        return ratingsList.size();
    }

    public static class RatingViewHolder extends RecyclerView.ViewHolder {
        public TextView ratingValue, ratingComment, username;

        public RatingViewHolder(View view) {
            super(view);
            username = view.findViewById(R.id.username);
            ratingValue = view.findViewById(R.id.ratingValue);
            ratingComment = view.findViewById(R.id.ratingComment);
        }
    }
}
