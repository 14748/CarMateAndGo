package org.cuatrovientos.blablacar.activities.login;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.MainActivity;
import org.cuatrovientos.blablacar.activities.SearchRoutes;
import org.cuatrovientos.blablacar.activities.register.RegisterPassword;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.util.List;

public class LogIn extends AppCompatActivity {

        private EditText edtxtEmail;
        private EditText edtxtContrasenha;
        private Button btnIniciarSesion;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_log_in);
            edtxtEmail = findViewById(R.id.edTxtEmail);
            edtxtContrasenha = findViewById(R.id.edTxtContrasenha);
            btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

            btnIniciarSesion.setOnClickListener(v -> {
                String email = edtxtEmail.getText().toString();
                String password = edtxtContrasenha.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    showErrors("Los campos no pueden estar vacíos");
                    return;
                }
                Utils.getUsers(new Utils.FirebaseCallback() {
                    @Override
                    public void onCallback(List<User> userList) {
                        try {
                            boolean emailDontExists = true;
                            for (User user : userList) {
                                if (user.getEmail().equalsIgnoreCase(email)){
                                    emailDontExists = false;
                                    if (user.getPassword().equals(password)){
                                        UserManager.init(getApplicationContext());
                                        UserManager.setCurrentUser(user);
                                        Intent intent = new Intent(LogIn.this, SearchRoutes.class);
                                        startActivity(intent);
                                    }
                                    else {
                                        showErrors("La contraseña no es correcta");
                                        break;
                                    }
                                }
                            }
                            if (emailDontExists){
                                showErrors("No existe ningún usuario con el email " + email);
                            }
                        } catch (Exception e){
                            showErrors("El usuario " + email + " no existe");
                            edtxtEmail.setText("");
                            edtxtContrasenha.setText("");
                        }
                    }
                });
            });
        }

        public void showErrors(String text){
            View contentView = findViewById(android.R.id.content);
            Snackbar snackbar = Snackbar.make(contentView, text, Snackbar.LENGTH_SHORT);
            snackbar.setTextColor(Color.WHITE);
            snackbar.setBackgroundTint(Color.RED);
            snackbar.show();
        }
    }