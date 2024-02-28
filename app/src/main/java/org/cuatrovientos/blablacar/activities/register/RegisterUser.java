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
    EditText nombre, apellidos, fecNacimiento;
    String email;
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        nextPageBtn = findViewById(R.id.btnNextPage);
        nombre = findViewById(R.id.txtNombre);
        apellidos = findViewById(R.id.txtApellidos);
        fecNacimiento = findViewById(R.id.txtFechaNacimiento);
        bundle = getIntent().getExtras();
        email = bundle.getString("email");

        nextPageBtn.setOnClickListener(view -> {
            Intent intent = new Intent(RegisterUser.this, RegisterPassword.class);
            String nombreText = nombre.getText().toString();
            String apellidosText = apellidos.getText().toString();
            String fecNacimientoText = fecNacimiento.getText().toString();
            intent.putExtra("nombre", nombreText);
            intent.putExtra("apellidos", apellidosText);
            intent.putExtra("email", email);
            intent.putExtra("fecNacimiento", fecNacimientoText);
            startActivity(intent);
                });

        nombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((nombre.length() > 3 && apellidos.length() > 2)){
                    nextPageBtn.setVisibility(View.VISIBLE);
                } else {
                    nextPageBtn.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

        apellidos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if((nombre.length() > 3 && apellidos.length() > 2)){
                    nextPageBtn.setVisibility(View.VISIBLE);
                } else {
                    nextPageBtn.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}