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
import org.cuatrovientos.blablacar.activities.MainActivity;

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

            login();
        }

        private void login() {
            findViewById(R.id.btnIniciarSesion).setOnClickListener(v -> {
                String email = edtxtEmail.getText().toString();
                String password = edtxtContrasenha.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    View contentView = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(contentView, "Los campos son obligatorios", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                    return;
                }

                // TODO: meter lógica de validación de usuario con datos en BD
                // TODO PASAR TAMBIÉN USER EN EL INTENT
                Intent intent = new Intent(LogIn.this, MainActivity.class);
                startActivity(intent);
            });
        }
    }