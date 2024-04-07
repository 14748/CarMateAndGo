package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.cuatrovientos.blablacar.BalanceActivity;
import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.login.LogIn;
import org.cuatrovientos.blablacar.activities.login.MainScreen;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

public class ProfileActivity extends AppCompatActivity {
    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageView btnPagos;
    private ImageView btnNotificaciones;
    private LinearLayout leaderboardLayout;
    private Button btnEditPerfil;
    private Button btnLogout;
    private TextView nombre;
    private TextView email;
    private TextView fechaNacimiento;
    private TextView balance;
    private TextView password;
    private User currentUser;
    private TextView imgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Si no hay user loggeado te manda a la pantalla de LogIn
        UserManager.init(getApplicationContext());
        currentUser = UserManager.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(ProfileActivity.this, MainScreen.class);
            startActivity(intent);
        }

        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnEditPerfil = findViewById(R.id.btnEditPerfil);
        btnLogout = findViewById(R.id.btnLogout);
        btnPagos = findViewById(R.id.imagePagos);
        btnHistory = findViewById(R.id.btnHistory);
        password = findViewById(R.id.btnChangePassword);
        imgPerfil = findViewById(R.id.imageView2);
        btnNotificaciones = findViewById(R.id.imageNotificaciones);
        leaderboardLayout = findViewById(R.id.leaderboardLayout);

        password.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setTitle("Editar Contraseña");
            LinearLayout layout = new LinearLayout(ProfileActivity.this);
            layout.setOrientation(LinearLayout.VERTICAL);
            final EditText editTextOldPass = new EditText(ProfileActivity.this);
            editTextOldPass.setHint("Contraseña actual");
            editTextOldPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(editTextOldPass);
            LinearLayout.LayoutParams paramsOldPass = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            paramsOldPass.setMargins(70, 15, 70, 15);
            editTextOldPass.setLayoutParams(paramsOldPass);
            final EditText editTextNewPass = new EditText(ProfileActivity.this);
            editTextNewPass.setHint("Nueva contraseña");
            editTextNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(editTextNewPass);
            LinearLayout.LayoutParams paramsNewPass = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            paramsNewPass.setMargins(70, 15, 70, 15);
            editTextNewPass.setLayoutParams(paramsNewPass);
            final EditText editTextNewPassRep = new EditText(ProfileActivity.this);
            editTextNewPassRep.setHint("Repetir nueva contraseña");
            editTextNewPassRep.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            layout.addView(editTextNewPassRep);
            LinearLayout.LayoutParams paramsNewPass2 = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            paramsNewPass2.setMargins(70, 15, 70, 15);
            editTextNewPassRep.setLayoutParams(paramsNewPass2);
            builder.setView(layout);
            builder.setPositiveButton("Guardar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String oldPass = editTextOldPass.getText().toString().trim();
                    String pass1 = editTextNewPass.getText().toString().trim();
                    String pass2 = editTextNewPassRep.getText().toString().trim();

                    if (!(oldPass == currentUser.getPassword())){
                        message("La contraseña actual no es correcta", Color.RED);
                        return;
                    }
                    else if (!(pass1 == pass2)){
                        message("Las nuevas contraseñas no coinciden", Color.RED);
                        return;
                    }
                    currentUser.setPassword(pass1);
                    Utils.pushUser(currentUser);
                    UserManager.setCurrentUser(currentUser);
                    message("Contraseña cambiada con éxito", Color.GREEN);
                }
            });
            builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        btnPagos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent balanceIntent = new Intent(ProfileActivity.this, BalanceActivity.class);
                startActivity(balanceIntent);
                finish();
            }
        });

        leaderboardLayout.setOnClickListener(view -> {
            Intent leaderboardIntent = new Intent(this, C02LeaderboardActivity.class);
            startActivity(leaderboardIntent);
        });

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

        btnEditPerfil.setOnClickListener(view -> {
            Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
            startActivity(editProfileIntent);
        });

        btnLogout.setOnClickListener(view -> {
            currentUser = null;
            UserManager.setCurrentUser(null);
            Intent intent = new Intent(ProfileActivity.this, MainScreen.class);
            startActivity(intent);
        });

        nombre = findViewById(R.id.textViewNombre);
        email = findViewById(R.id.btnEmail);
        fechaNacimiento = findViewById(R.id.btnFechaNacimiento);
        balance = findViewById(R.id.btnBalance);
        nombre.setText(currentUser.getName() + " " + currentUser.getLastName());
        email.setText(currentUser.getEmail());
        fechaNacimiento.setText(currentUser.getBirthDate().getDate() + "/" + (currentUser.getBirthDate().getMonth() + 1)
                + "/" + currentUser.getBirthDate().getYear());
        balance.setText(String.valueOf(currentUser.getBalance()));
        imgPerfil.setText(currentUser.getName().charAt(0) + "" + currentUser.getLastName().charAt(0));
        imgPerfil.getBackground().setColorFilter(Color.parseColor("#" + Utils.getRandomColor()), PorterDuff.Mode.SRC);
    }

    private void message(String text, int color) {
        View contentView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(contentView, text, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(color);
        snackbar.show();
    }
}