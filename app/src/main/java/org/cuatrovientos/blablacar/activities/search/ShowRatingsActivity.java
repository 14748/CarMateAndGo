package org.cuatrovientos.blablacar.activities.search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.search.adapters.RecyclerRatingAdapter;
import org.cuatrovientos.blablacar.models.Rating;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.List;

public class ShowRatingsActivity extends AppCompatActivity {

    private RecyclerView ratingsRecyclerView;
    private RecyclerRatingAdapter ratingsAdapter;
    private List<Rating> ratingsList;
    private User user;
    private TextView headerOpinionesUsuario;
    private  TextView noElements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_ratings);

        noElements = findViewById(R.id.noElements);
        ratingsRecyclerView = findViewById(R.id.ratingsRecyclerView);

        if (getIntent() != null && getIntent().getSerializableExtra("ratingsList") != null) {
            ratingsList = (List<Rating>) getIntent().getSerializableExtra("ratingsList");
            user = (User) getIntent().getSerializableExtra("user");

            if (ratingsList.size() > 0){
                noElements.setVisibility(View.GONE);
                ratingsRecyclerView.setVisibility(View.VISIBLE);
                ratingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

                ratingsAdapter = new RecyclerRatingAdapter(ratingsList);
                ratingsRecyclerView.setAdapter(ratingsAdapter);
            }else {
                noElements.setVisibility(View.VISIBLE);
                ratingsRecyclerView.setVisibility(View.GONE);
            }


            headerOpinionesUsuario = findViewById(R.id.headerOpinionesUsuario);
            headerOpinionesUsuario.setText("Opiniones acerca de " + user.getName());
        } else {
            ratingsList = new ArrayList<>();
            user = new User();
        }


    }
}