package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.login.MainScreen;
import org.cuatrovientos.blablacar.models.User;

public class ProfileActivity extends AppCompatActivity {

    private User currentUser;
    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private Button btnEditPerfil;
    private TextView nombre;
    private TextView email;
    private TextView fechaNacimiento;
    private TextView balance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Si no hay user loggeado te manda a la pantalla de LogIn
        currentUser = UserManager.getCurrentUser();
        if (currentUser == null){
            Intent intent = new Intent(ProfileActivity.this, MainScreen.class);
            startActivity(intent);
        }

        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnEditPerfil = findViewById(R.id.btnEditPerfil);

        btnSearch.setOnClickListener(view -> {
            /*
            TODO: redirigir a la pantalla de buscar
             */
        });

        btnPublish.setOnClickListener(view -> {
            Intent publishIntent = new Intent(this, CreateRoute.class);
            startActivity(publishIntent);
        });

        btnEditPerfil.setOnClickListener(view -> {
            // TODO: enviar a pantalla para editar o abrir dialog
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        nombre = findViewById(R.id.textViewNombre);
        email = findViewById(R.id.btnEmail);
        fechaNacimiento = findViewById(R.id.btnFechaNacimiento);
        balance = findViewById(R.id.btnBalance);
        nombre.setText(currentUser.getName() + " " + currentUser.getLastName());
        email.setText(currentUser.getEmail());
        fechaNacimiento.setText(currentUser.getBirthDate().getDate() + "/" + (currentUser.getBirthDate().getMonth()+1) + "/" + currentUser.getBirthDate().getYear());
        // TODO: completar cuando esté implementado el balance
        // balance.setText(currentUser.getBalance());
    }
}