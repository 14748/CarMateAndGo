package org.cuatrovientos.blablacar.activities.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;

import org.cuatrovientos.blablacar.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterEmail extends AppCompatActivity {

    ImageButton nextPageBtn;
    EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        nextPageBtn = findViewById(R.id.btnNextPage);
        email = findViewById(R.id.txtEmail);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String emailText = email.getText().toString();
                if (emailText.contains("@")) {
                    nextPageBtn.setVisibility(View.VISIBLE);
                } else {
                    nextPageBtn.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        nextPageBtn.setOnClickListener(view -> {
            String emailText = email.getText().toString();
            if (validateEmail(emailText)){
                //TODO: verificar que no existe usuario ya
                Intent intent = new Intent(RegisterEmail.this, RegisterUser.class);
                intent.putExtra( "email", emailText);
                startActivity(intent);
            }else {
                errorMessage("Email no válido");
            }
        });
    }
    private boolean validateEmail(String email) {
        String emailPattern = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    private void errorMessage(String text){
        nextPageBtn.setVisibility(View.INVISIBLE);
        View contentView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(contentView, text, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.RED);
        snackbar.setBackgroundTint(Color.BLACK);
        snackbar.show();
    }
}
