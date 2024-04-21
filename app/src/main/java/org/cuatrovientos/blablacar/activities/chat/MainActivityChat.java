package org.cuatrovientos.blablacar.activities.chat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.create.CreateRoute;
import org.cuatrovientos.blablacar.activities.phone_register;
import org.cuatrovientos.blablacar.activities.profile.ProfileActivity;
import org.cuatrovientos.blablacar.activities.search.SearchRoutes;
import org.cuatrovientos.blablacar.activities.history.UserTripsActivity;
import org.cuatrovientos.blablacar.models.User;

public class MainActivityChat extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ImageButton searchButton;

    ChatFragment chatFragment;
    //ProfileFragment profileFragment;

    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;
    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        UserManager.init(getApplicationContext());
        User currentUser = UserManager.getCurrentUser();

        if (currentUser.getTelephone() == null){
            Intent phoneIntent = new Intent(this, phone_register.class);
            startActivity(phoneIntent);
        }

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

        btnProfile.setOnClickListener(view -> {

            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);

        });

        chatFragment = new ChatFragment();
        //profileFragment = new ProfileFragment();

        searchButton = findViewById(R.id.main_search_btn);

        searchButton.setOnClickListener((v)->{
            startActivity(new Intent(MainActivityChat.this,SearchUserActivity.class));
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame_layout,chatFragment).commit();

        //getFCMToken();

    }

    /*
    void getFCMToken(){
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                FirebaseUtil.currentUserDetails(getApplicationContext()).update("fcmToken",token);

            }
        });
        }
     */
}