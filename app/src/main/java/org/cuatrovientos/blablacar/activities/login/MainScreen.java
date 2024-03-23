package org.cuatrovientos.blablacar.activities.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.MainActivity;
import org.cuatrovientos.blablacar.activities.SearchRoutes;
import org.cuatrovientos.blablacar.activities.register.RegisterEmail;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        UserManager.init(getApplicationContext());
        User currentUser = UserManager.getCurrentUser();

        if (currentUser == null){
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
        }else{
            Utils.getUserById(String.valueOf(currentUser.getId()), new Utils.FirebaseCallbackUser() {
                @Override
                public void onCallback(User user) {
                    UserManager.setCurrentUser(user);
                }
            });
            Intent intent = new Intent(MainScreen.this, SearchRoutes.class);
            startActivity(intent);
            finish();
        }


    }
}