package org.cuatrovientos.blablacar.activities.chat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.chat.adapters.RecentChatRecyclerAdapter;
import org.cuatrovientos.blablacar.activities.chat.models.ChatroomModel;
import org.cuatrovientos.blablacar.activities.chat.util.FirebaseUtil;
import org.cuatrovientos.blablacar.models.Utils;

public class ChatFragment extends Fragment {

    RecyclerView recyclerView;
    RecentChatRecyclerAdapter adapter;
    View view;
    TextView noElements;
    public ChatFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_chat, container, false);
        recyclerView = view.findViewById(R.id.recyler_view);
        setupRecyclerView();
        noElements = view.findViewById(R.id.noElements);
        Utils.setupClickableTextView(getContext(), noElements, "Aun no tienes ninguna conversacion inicia una aquí", SearchUserActivity.class);

        return view;
    }

    void setupRecyclerView() {
        Query query = FirebaseUtil.allChatroomCollectionReference()
                .whereArrayContains("userIds", FirebaseUtil.currentUserId(getContext()))
                .orderBy("lastMessageTimestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ChatroomModel> options = new FirestoreRecyclerOptions.Builder<ChatroomModel>()
                .setQuery(query, ChatroomModel.class)
                .build();

        adapter = new RecentChatRecyclerAdapter(options, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.startListening();

        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                checkAdapterIsEmpty();
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                checkAdapterIsEmpty();
            }

            @Override
            public void onChanged() {
                checkAdapterIsEmpty();
            }

            private void checkAdapterIsEmpty() {
                boolean emptyViewVisible = adapter.getItemCount() == 0;
                recyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
                noElements.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            }

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter!=null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter!=null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter!=null)
            adapter.notifyDataSetChanged();
    }
}