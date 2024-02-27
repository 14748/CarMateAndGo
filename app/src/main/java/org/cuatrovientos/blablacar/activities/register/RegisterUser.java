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

public class RegisterUser extends AppCompatActivity {

    ImageButton nextPageBtn;
    EditText nombre, apellidos;
    String email;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        nextPageBtn = findViewById(R.id.btnNextPage);
        nombre = findViewById(R.id.txtNombre);
        apellidos = findViewById(R.id.txtApellidos);
        bundle = getIntent().getExtras();
        assert bundle != null;
        email = bundle.getString("email");

        nextPageBtn.setOnClickListener(view -> {
            if(validateDNI(dniText)){
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("personalDetails").document(dniText).get().addOnSuccessListener(documentSnapshot -> {
                    next.setVisibility(View.INVISIBLE);
                    if(documentSnapshot.exists()){
                        View contentView = findViewById(android.R.id.content);
                        Snackbar snackbar = Snackbar.make(contentView, R.string.errorLogDniExist, Snackbar.LENGTH_SHORT);
                        snackbar.setTextColor(Color.WHITE);
                        snackbar.setBackgroundTint(Color.RED);
                        snackbar.show();
                    } else{
                        Intent intent = new Intent(SecondRegisterActivity.this, ThirdRegisterActivity.class);
                        String nombreText = nombre.getText().toString();
                        String apellidosText = apellidos.getText().toString();
                        intent.putExtra("name", nombreText);
                        intent.putExtra("surname", apellidosText);
                        intent.putExtra( "email", email);
                        startActivity(intent);
                    }
                }).addOnFailureListener(e -> {
                });
            } else {
                nextPageBtn.setVisibility(View.INVISIBLE);
                View contentView = findViewById(android.R.id.content);
                Snackbar snackbar = Snackbar.make(contentView, R.string.errorLogDNI, Snackbar.LENGTH_SHORT);
                snackbar.setTextColor(Color.RED);
                snackbar.setBackgroundTint(Color.BLACK);
                snackbar.show();
            }
        });

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        apellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                buttonVisibility();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    private void buttonVisibility(){
        String nameText = nombre.getText().toString();
        String surnameText = apellidos.getText().toString();
        if((nameText.length() > 3 && surnameText.length() > 2)){
            next.setVisibility(View.VISIBLE);
        } else {
            next.setVisibility(View.INVISIBLE);
        }
    }
}