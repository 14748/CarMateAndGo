package org.cuatrovientos.blablacar.activities.chat.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.chat.ChatActivity;
import org.cuatrovientos.blablacar.activities.chat.util.AndroidUtil;
import org.cuatrovientos.blablacar.activities.chat.util.FirebaseUtil;
import org.cuatrovientos.blablacar.models.User;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<User, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;

    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<User> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder holder, int position, @NonNull User model) {
        holder.usernameText.setText(model.getName() + " " + model.getLastName());
        holder.phoneText.setText(String.valueOf(model.getTelephone()));
        if(model.getId().equals(FirebaseUtil.currentUserId(context.getApplicationContext()))){
            holder.usernameText.setText(model.getName()+" (Me)");
        }

        holder.profilePic.setText(model.getName().charAt(0) + "" + model.getLastName().charAt(0));
        holder.profilePic.getBackground().setColorFilter(Color.parseColor("#" + model.getColor()), PorterDuff.Mode.SRC);

        holder.itemView.setOnClickListener(v -> {
            //navigate to chat activity
            Intent intent = new Intent(context, ChatActivity.class);
            AndroidUtil.passUserModelAsIntent(intent,model);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_user_recycler_row,parent,false);
        return new UserModelViewHolder(view);
    }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView phoneText;
        TextView profilePic;

        public UserModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            phoneText = itemView.findViewById(R.id.phone_text);
            profilePic = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
