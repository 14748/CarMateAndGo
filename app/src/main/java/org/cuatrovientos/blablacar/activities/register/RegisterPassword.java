package org.cuatrovientos.blablacar.activities.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.MainActivity;
import org.cuatrovientos.blablacar.models.User;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterPassword extends AppCompatActivity {

    ImageButton btnNextPage;
    EditText password, passwordRep;
    Bundle bundle;
    String keepNombre, keepApellidos, keepEmail, keepFechaNacimiento;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        password = findViewById(R.id.txtPassword);
        passwordRep = findViewById(R.id.txtPasswordRepetida);
        btnNextPage = findViewById(R.id.btnNextPage);
        bundle = getIntent().getExtras();
        keepNombre = bundle.getString("nombre");
        keepApellidos = bundle.getString("apellidos");
        keepEmail = bundle.getString("email");
        keepFechaNacimiento = bundle.getString("fecNacimiento");
        db = FirebaseFirestore.getInstance();

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String userPassword = password.getText().toString();
                if (!(userPassword.length() >= 8 && validatePassword(userPassword))) {
                    btnNextPage.setVisibility(View.INVISIBLE);
                    passwordRep.setText("");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        passwordRep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String rePasswordString = passwordRep.getText().toString();

                if (rePasswordString.length() > 2) {
                    btnNextPage.setVisibility(View.VISIBLE);
                } else {
                    btnNextPage.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnNextPage.setOnClickListener(view -> {
            String userPassword = password.getText().toString();
            String userPasswordRepetida = passwordRep.getText().toString();

            if (userPassword.equals(userPasswordRepetida)) {

                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date fecha = dateFormat.parse(keepFechaNacimiento);
                    SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
                    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
                    int day = Integer.parseInt(dayFormat.format(fecha));
                    int month = Integer.parseInt(monthFormat.format(fecha));
                    int year = Integer.parseInt(yearFormat.format(fecha));
                    Date fechaFormateada = new Date(year - 1900, month - 1, day);
                    User keepUser = new User(1, keepNombre, keepApellidos, fechaFormateada, keepEmail, userPassword);
                    //TODO: persistir en BD
                    //TODO: redirigir a la activity que corresponda
                    Intent intent = new Intent(RegisterPassword.this, MainActivity.class);
                    startActivity(intent);
                } catch (ParseException e) {
                    View contentView = findViewById(android.R.id.content);
                    Snackbar snackbar = Snackbar.make(contentView, "Error en el acceso a la base de datos", Snackbar.LENGTH_SHORT);
                    snackbar.setTextColor(Color.WHITE);
                    snackbar.setBackgroundTint(Color.RED);
                    snackbar.show();
                }
            } else {
                btnNextPage.setVisibility(View.INVISIBLE);
                View contentView = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(contentView, "Contraseña no válida", Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.WHITE);
                snackbar.setBackgroundTint(Color.RED);
                snackbar.show();
            }
        });
    }

    private boolean validatePassword(String input){
        String letters = "abcdefghijklmnopqrstuvwxyz";
        String nums = "1234567890";
        String special = "!$%&/()=?¿¡#@";
        boolean letterValidation = false, numberValidation = false, specialValidation = false;
        for (char c : input.toCharArray()) {

            if (letters.contains(String.valueOf(c).toLowerCase())) {
                letterValidation = true;
            }
            else if (nums.contains(String.valueOf(c))) {
                return true;
            }
            else if (special.contains(String.valueOf(c))) {
                specialValidation = true;
            }
            if (letterValidation && numberValidation && specialValidation){
                return true;
            }
        }
        return false;
    }
}