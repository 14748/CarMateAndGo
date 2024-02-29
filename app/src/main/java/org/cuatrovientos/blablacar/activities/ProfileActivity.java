package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import org.cuatrovientos.blablacar.R;

public class ProfileActivity extends AppCompatActivity {
    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;
    private ImageButton btnProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnHistory = findViewById(R.id.btnHistory);
        btnMessages = findViewById(R.id.btnMessages);
        btnProfile = findViewById(R.id.btnProfile);

        btnSearch.setOnClickListener(view -> {
            /*
            Intent searchIntent = new Intent(this, Search.class);
            startActivity(searchIntent);
             */
        });

        btnPublish.setOnClickListener(view -> {
            Intent publishIntent = new Intent(this, CreateRoute.class);
            startActivity(publishIntent);
            finish();
        });

        btnHistory.setOnClickListener(view -> {
            /*
            Intent historyIntent = new Intent(this, HistoryActivity.class);
            startActivity(historyIntent);
             */
        });

        btnMessages.setOnClickListener(view -> {
            /*
            Intent messagesIntent = new Intent(this, MessagesActivity.class);
            startActivity(messagesIntent);
             */
        });

        btnProfile.setOnClickListener(view -> {

            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);
            finish();
        });
    }
}