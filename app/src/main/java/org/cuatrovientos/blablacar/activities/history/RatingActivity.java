package org.cuatrovientos.blablacar.activities.history;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.FirebaseDatabase;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.models.DriverTrips;
import org.cuatrovientos.blablacar.models.Rating;
import org.cuatrovientos.blablacar.models.RouteEntity;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import okhttp3.internal.Util;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText commentInput;
    private Button submitRatingButton;
    private DriverTrips currentDriverTrip; 

    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        UserManager.init(getApplicationContext());
        currentUser = UserManager.getCurrentUser();

        ratingBar = findViewById(R.id.ratingBar);
        commentInput = findViewById(R.id.commentInput);
        submitRatingButton = findViewById(R.id.submitRatingButton);

        
        currentDriverTrip = (DriverTrips) getIntent().getSerializableExtra("DRIVERTRIPS_KEY");

        submitRatingButton.setOnClickListener(view -> submitRating());
    }

    private void submitRating() {
        float value = ratingBar.getRating();
        String comment = commentInput.getText().toString();

        
        Rating rating = new Rating(value, comment, currentDriverTrip.getRoute(), currentDriverTrip.getUser().getId());

        currentDriverTrip.getUser().addRating(rating);
        Utils.pushUser(currentDriverTrip.getUser());

        finish();
    }
}
