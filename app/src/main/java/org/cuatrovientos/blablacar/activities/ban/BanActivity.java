package org.cuatrovientos.blablacar.activities.ban;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.ban.adapter.RecyclerUserBanAdapter;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.ArrayList;
import java.util.List;

public class BanActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerUserBanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban);

        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        UserManager.init(getApplicationContext());
        User currentUser = UserManager.getCurrentUser();

        List<RouteEntity> createdRoutes = currentUser.getCreatedRoutes();
        List<String> passengerIds = new ArrayList<>();
        for (RouteEntity route : createdRoutes) {
            passengerIds.addAll(route.getPassengers());
        }

        Utils.getUsersByIds(passengerIds, new Utils.UsersCallback() {
            @Override
            public void onCallback(List<User> users) {
                adapter = new RecyclerUserBanAdapter(users, new RecyclerUserBanAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(User user) {
                        // Handle item click
                    }

                    @Override
                    public void onBanClick(User user) {
                        currentUser.addBannedUsers(user.getId());
                    }
                });
                recyclerView.setAdapter(adapter);
            }
        });
    }
}
