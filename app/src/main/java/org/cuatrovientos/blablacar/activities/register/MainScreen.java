package org.cuatrovientos.blablacar.activities.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import org.cuatrovientos.blablacar.R;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        ConstraintLayout redirectLogIn = findViewById(R.id.redirectLogIn);
        redirectLogIn.setOnClickListener(view -> {
            Intent intent = new Intent(MainScreen.this, LogIn.class);
            startActivity(intent);
        });
        ConstraintLayout redirectRegister = findViewById(R.id.redirectRegister);
        redirectRegister.setOnClickListener(view -> {
            Intent intent = new Intent(MainScreen.this, RegisterEmail.class);
            startActivity(intent);
        });
    }
}