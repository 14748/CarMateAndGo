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
import org.cuatrovientos.blablacar.activities.chat.models.ChatroomModel;
import org.cuatrovientos.blablacar.activities.chat.util.AndroidUtil;
import org.cuatrovientos.blablacar.activities.chat.util.FirebaseUtil;
import org.cuatrovientos.blablacar.models.User;

public class RecentChatRecyclerAdapter extends FirestoreRecyclerAdapter<ChatroomModel, RecentChatRecyclerAdapter.ChatroomModelViewHolder> {

    Context context;

    public RecentChatRecyclerAdapter(@NonNull FirestoreRecyclerOptions<ChatroomModel> options,Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatroomModelViewHolder holder, int position, @NonNull ChatroomModel model) {
        FirebaseUtil.getOtherUserFromChatroom(model.getUserIds(), context.getApplicationContext())
                .get().addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            boolean lastMessageSentByMe = model.getLastMessageSenderId().equals(FirebaseUtil.currentUserId(context.getApplicationContext()));


                            User otherUserModel = task.getResult().toObject(User.class);

                            holder.usernameText.setText(otherUserModel.getName() + " " + otherUserModel.getLastName());
                            if(lastMessageSentByMe)
                                holder.lastMessageText.setText("Tu : "+model.getLastMessage());
                            else
                                holder.lastMessageText.setText(model.getLastMessage());
                            holder.lastMessageTime.setText(FirebaseUtil.timestampToString(model.getLastMessageTimestamp()));

                            holder.profilePic.setText(otherUserModel.getName().charAt(0) + "" + otherUserModel.getLastName().charAt(0));
                            holder.profilePic.getBackground().setColorFilter(Color.parseColor("#" + otherUserModel.getColor()), PorterDuff.Mode.SRC);

                            holder.itemView.setOnClickListener(v -> {
                                
                                Intent intent = new Intent(context, ChatActivity.class);
                                AndroidUtil.passUserModelAsIntent(intent,otherUserModel);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            });

                        }
                });
    }

    @NonNull
    @Override
    public ChatroomModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recent_chat_recycler_row,parent,false);
        return new ChatroomModelViewHolder(view);
    }

    class ChatroomModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView lastMessageText;
        TextView lastMessageTime;
        TextView profilePic;

        public ChatroomModelViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.user_name_text);
            lastMessageText = itemView.findViewById(R.id.last_message_text);
            lastMessageTime = itemView.findViewById(R.id.last_message_time_text);
            profilePic = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
