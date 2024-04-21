package org.cuatrovientos.blablacar.activities.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.chat.MainActivityChat;
import org.cuatrovientos.blablacar.activities.create.CreateRoute;
import org.cuatrovientos.blablacar.activities.search.SearchRoutes;
import org.cuatrovientos.blablacar.activities.history.UserTripsActivity;
import org.cuatrovientos.blablacar.activities.profile.adapters.LeaderboardAdapter;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.List;

public class C02LeaderboardActivity extends AppCompatActivity {

    private RecyclerView leaderboardRecyclerView;
    private LeaderboardAdapter leaderboardAdapter;
    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;
    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c02_leaderboard);

        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnHistory = findViewById(R.id.btnHistory);
        btnMessages = findViewById(R.id.btnMessages);
        btnProfile = findViewById(R.id.btnProfile);

        btnSearch.setOnClickListener(view -> {
            Intent searchIntent = new Intent(this, SearchRoutes.class);
            startActivity(searchIntent);
        });

        btnPublish.setOnClickListener(view -> {
            Intent publishIntent = new Intent(this, CreateRoute.class);
            startActivity(publishIntent);
        });


        btnHistory.setOnClickListener(view -> {
            Intent historyIntent = new Intent(this, UserTripsActivity.class);
            startActivity(historyIntent);
        });

        btnMessages.setOnClickListener(view -> {
            Intent messagesIntent = new Intent(this, MainActivityChat.class);
            startActivity(messagesIntent);
        });

        btnProfile.setOnClickListener(view -> {

            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);

        });

        leaderboardRecyclerView = findViewById(R.id.leaderboardRecyclerView);
        leaderboardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Utils.getTopUsersByCO2Reduction(new Utils.FirebaseCallback() {
            @Override
            public void onCallback(List<User> userList) {
                leaderboardAdapter = new LeaderboardAdapter(userList);
                leaderboardRecyclerView.setAdapter(leaderboardAdapter);
            }
        });
    }
}