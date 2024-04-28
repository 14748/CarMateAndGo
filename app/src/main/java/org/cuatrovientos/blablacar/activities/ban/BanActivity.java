package org.cuatrovientos.blablacar.activities.ban;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.ban.adapter.RecyclerUserBanAdapter;
import org.cuatrovientos.blablacar.activities.chat.MainActivityChat;
import org.cuatrovientos.blablacar.activities.create.CreateRoute;
import org.cuatrovientos.blablacar.activities.history.UserTripsActivity;
import org.cuatrovientos.blablacar.activities.profile.ProfileActivity;
import org.cuatrovientos.blablacar.activities.search.SearchRoutes;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerUserBanAdapter adapter;
    private TextView noElements;
    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;
    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);

        noElements = findViewById(R.id.noElements);
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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

        UserManager.init(getApplicationContext());
        User currentUser = UserManager.getCurrentUser();

        List<RouteEntity> createdRoutes = currentUser.getCreatedRoutes();
        List<String> passengerIds = new ArrayList<>();
        for (RouteEntity route : createdRoutes) {
            if (route.getDate().before(new Date())){
                passengerIds.addAll(route.getPassengers());
            }
        }

        Utils.getUsersByIds(passengerIds, new Utils.UsersCallback() {
            @Override
            public void onCallback(List<User> users) {
                if (users.isEmpty()){
                    noElements.setVisibility(View.VISIBLE);
                }else{
                    noElements.setVisibility(View.GONE);
                    adapter = new RecyclerUserBanAdapter(users, currentUser.getBannedUsers(), new RecyclerUserBanAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(User user) {
                            
                        }

                        @Override
                        public void onBanClick(User user) {
                            if (currentUser.getBannedUsers().contains(user.getId())) {
                                currentUser.removeBannedUsers(user.getId());
                                adapter.unbanUser(user);
                            } else {
                                currentUser.addBannedUsers(user.getId());
                                adapter.banUser(user);
                            }
                            Utils.pushUser(currentUser);
                            UserManager.setCurrentUser(currentUser);
                        }
                    });
                    recyclerView.setAdapter(adapter);
                }

            }
        });
    }
}
