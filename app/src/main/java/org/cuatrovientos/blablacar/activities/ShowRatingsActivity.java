package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.adapters.RecyclerRatingAdapter;
import org.cuatrovientos.blablacar.models.Rating;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.ArrayList;
import java.util.List;

import okhttp3.internal.Util;

public class ShowRatingsActivity extends AppCompatActivity {

    private RecyclerView ratingsRecyclerView;
    private RecyclerRatingAdapter ratingsAdapter;
    private List<Rating> ratingsList;
    private User user;
    private TextView headerOpinionesUsuario;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ratings);

        if (getIntent() != null && getIntent().getSerializableExtra("ratingsList") != null) {
            ratingsList = (List<Rating>) getIntent().getSerializableExtra("ratingsList");
            user = (User) getIntent().getSerializableExtra("user");

            ratingsRecyclerView = findViewById(R.id.ratingsRecyclerView);
            ratingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            ratingsAdapter = new RecyclerRatingAdapter(ratingsList);
            ratingsRecyclerView.setAdapter(ratingsAdapter);

            headerOpinionesUsuario = findViewById(R.id.headerOpinionesUsuario);
            headerOpinionesUsuario.setText("Opiniones acerca de " + user.getName());
        } else {
            ratingsList = new ArrayList<>();
            user = new User();
        }


    }
}