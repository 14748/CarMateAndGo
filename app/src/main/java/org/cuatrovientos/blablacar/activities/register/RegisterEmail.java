package org.cuatrovientos.blablacar.activities.register;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.User;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterEmail extends AppCompatActivity {

    ImageButton nextPageBtn;
    EditText email;
    private ArrayList<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_email);
        nextPageBtn = findViewById(R.id.btnNextPage);
        email = findViewById(R.id.txtEmail);
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
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
            public void afterTextChanged(Editable s) {
            }
        });

        nextPageBtn.setOnClickListener(view -> {
            String emailText = email.getText().toString();
            // TODO: validar si existe ya el email
            if (validateEmail(emailText)) {
//                boolean validate = true;
//                for (User user : users) {
//                    if (user.getEmail().equalsIgnoreCase(emailText)) {
//                        validate = false;
//                        break;
//                    }
//                }
//                if (validate){
                    Intent intent = new Intent(RegisterEmail.this, RegisterUser.class);
                    intent.putExtra("email", emailText.toLowerCase());
                    startActivity(intent);
//                }
            } else {
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

    private void errorMessage(String text) {
        nextPageBtn.setVisibility(View.INVISIBLE);
        View contentView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(contentView, text, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.show();
    }
}
