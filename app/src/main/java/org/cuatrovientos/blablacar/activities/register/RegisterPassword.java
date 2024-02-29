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

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.MainActivity;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.internal.Util;

public class RegisterPassword extends AppCompatActivity {

    ImageButton btnNextPage;
    EditText password, passwordRep;
    String keepNombre, keepApellidos, keepEmail, keepFechaNacimiento;
    Date date = new Date();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_password);

        password = findViewById(R.id.txtPassword);
        passwordRep = findViewById(R.id.txtPasswordRepetida);
        btnNextPage = findViewById(R.id.btnNextPage);

        Bundle bundle = getIntent().getExtras();
        keepNombre = bundle.getString("nombre");
        keepApellidos = bundle.getString("apellidos");
        keepEmail = bundle.getString("email");
        keepFechaNacimiento = bundle.getString("fecNacimiento");

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
                if (passwordRep.getText().toString().length() > 8 && validatePassword(passwordRep.getText().toString())) {
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
                    date = dateFormat.parse(keepFechaNacimiento);
                    Utils.getUsers(new Utils.FirebaseCallback() {
                        @Override
                        public void onCallback(List<User> userList) {
                            int maxID = -1;
                            for (User user : userList) {
                                if (user.getId() > maxID){
                                    maxID = user.getId();
                                    errorMessage(String.valueOf(user.getBirthDate().getYear()));
                                }
                            }
                            User keepUser = new User(maxID, keepNombre, keepApellidos, date, keepEmail, userPassword);
                            Utils.pushUser(keepUser);
                            Intent intent = new Intent(RegisterPassword.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } catch (ParseException e) {
                    errorMessage("Error en el acceso a la base de datos");
                }
            } else {
                errorMessage("Contraseña no válida");
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
    private void errorMessage(String text) {
        btnNextPage.setVisibility(View.INVISIBLE);
        View contentView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(contentView, text, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.show();
    }
}