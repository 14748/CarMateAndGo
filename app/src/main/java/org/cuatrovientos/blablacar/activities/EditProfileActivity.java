package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.cuatrovientos.blablacar.BalanceActivity;
import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {

    private User currentUser;
    private EditText nombre;
    private EditText apellido;
    private EditText correo;
    private Button btnGuardar;
    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private TextView imgPerfil;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        UserManager.init(getApplicationContext());
        currentUser = UserManager.getCurrentUser();
        nombre = findViewById(R.id.edTxtNombre);
        apellido = findViewById(R.id.edTxtApellido);
        correo = findViewById(R.id.edTxtEmail);
        btnGuardar = findViewById(R.id.btnEditPerfil);
        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnHistory = findViewById(R.id.btnHistory);
        imgPerfil = findViewById(R.id.imgUser);

        nombre.setText(currentUser.getName());
        apellido.setText(currentUser.getLastName());
        correo.setText(currentUser.getEmail());
        imgPerfil.setText(currentUser.getName().charAt(0) + "" + currentUser.getLastName().charAt(0));
        imgPerfil.getBackground().setColorFilter(Color.parseColor("#" + Utils.getRandomColor()), PorterDuff.Mode.SRC);

        btnSearch.setOnClickListener(view -> {
            Intent searchIntent = new Intent(this, SearchRoutes.class);
            startActivity(searchIntent);
        });

        btnPublish.setOnClickListener(view -> {
            Intent publishIntent = new Intent(this, CreateRoute.class);
            startActivity(publishIntent);
        });

        btnHistory.setOnClickListener(view -> {
            Intent historyIntent = new Intent(this, UserTripsActivity.class);
            startActivity(historyIntent);
        });

        btnGuardar.setOnClickListener(view -> {
            if (validateEmail(correo.getText().toString())){
                if ((nombre.getText().toString() != null && !nombre.getText().toString().isEmpty()) && (apellido.getText().toString() != null && !apellido.getText().toString().isEmpty())){
                    currentUser.setName(nombre.getText().toString());
                    currentUser.setLastName(apellido.getText().toString());
                    currentUser.setEmail(correo.getText().toString());
                    Utils.pushUser(currentUser);
                    UserManager.setCurrentUser(currentUser);
                    Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                    startActivity(intent);
                }
                else {
                    errorMessage("El nombre introducido no es válido");
                }
            }
            else {
                errorMessage("El nuevo correo electrónico no es válido");
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
        View contentView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(contentView, text, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(Color.RED);
        snackbar.show();
    }
}