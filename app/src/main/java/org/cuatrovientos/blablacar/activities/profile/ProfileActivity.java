package org.cuatrovientos.blablacar.activities.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
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

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.ban.BanActivity;
import org.cuatrovientos.blablacar.activities.search.SearchRoutes;
import org.cuatrovientos.blablacar.activities.history.UserTripsActivity;
import org.cuatrovientos.blablacar.activities.chat.MainActivityChat;
import org.cuatrovientos.blablacar.activities.create.CreateRoute;
import org.cuatrovientos.blablacar.activities.login.MainScreen;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileActivity extends AppCompatActivity {
    TextView avatar;
    TextView name;
    TextView email;
    TextView leaderBoardTextViewClick;
    TextView creditsTextViewClick;
    TextView changePasswordTextViewClick;
    TextView banPassengersTextViewClick;
    TextView logoutTextViewClick;
    TextView c02ReducedText;
    TextView fechaNacimientoText;
    TextView balanceText;
    User currentUser;
    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_test);

        UserManager.init(getApplicationContext());
        currentUser = UserManager.getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(ProfileActivity.this, MainScreen.class);
            startActivity(intent);
        }

        avatar = findViewById(R.id.avatarImageView);
        name = findViewById(R.id.userNameTextView);
        email = findViewById(R.id.emailTextView);
        leaderBoardTextViewClick = findViewById(R.id.leaderBoardTextView);
        creditsTextViewClick = findViewById(R.id.creditsTextView);
        changePasswordTextViewClick = findViewById(R.id.changePasswordTextView);
        banPassengersTextViewClick =  findViewById(R.id.banPassengersTextView);
        logoutTextViewClick = findViewById(R.id.logoutTextView);
        c02ReducedText = findViewById(R.id.c02ReducedText);
        fechaNacimientoText = findViewById(R.id.fechaNacimientoText);
        balanceText = findViewById(R.id.balanceText);
        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnHistory = findViewById(R.id.btnHistory);
        btnMessages = findViewById(R.id.btnMessages);

        changePasswordTextViewClick.setOnClickListener(view -> {
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

        creditsTextViewClick.setOnClickListener(view -> {
                Intent balanceIntent = new Intent(ProfileActivity.this, BalanceActivity.class);
                startActivity(balanceIntent);
        });

        leaderBoardTextViewClick.setOnClickListener(view -> {
            Intent leaderboardIntent = new Intent(this, C02LeaderboardActivity.class);
            startActivity(leaderboardIntent);
        });

        banPassengersTextViewClick.setOnClickListener(v -> {
            Intent banIntent = new Intent(this, BanActivity.class);
            startActivity(banIntent);
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

        btnMessages.setOnClickListener(v -> {
            Intent chatIntent = new Intent(this, MainActivityChat.class);
            startActivity(chatIntent);
        });

        name.setOnClickListener(view -> {
            Intent editProfileIntent = new Intent(this, EditProfileActivity.class);
            startActivity(editProfileIntent);
        });

        logoutTextViewClick.setOnClickListener(view -> {
            currentUser = null;
            UserManager.setCurrentUser(null);
            Intent intent = new Intent(ProfileActivity.this, MainScreen.class);
            startActivity(intent);
        });

        name.setText(currentUser.getName() + " " + currentUser.getLastName());
        email.setText(currentUser.getEmail());

        if (currentUser.getBirthDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(currentUser.getBirthDate());
            fechaNacimientoText.setText(formattedDate);
        }

        balanceText.setText(String.format("%.2f€", currentUser.getBalance()));
        c02ReducedText.setText(currentUser.getC02Reduction() + "kg");
        avatar.setText(currentUser.getName().charAt(0) + "" + currentUser.getLastName().charAt(0));
        avatar.getBackground().setColorFilter(Color.parseColor("#" + currentUser.getColor()), PorterDuff.Mode.SRC);
    }

    private void message(String text, int color) {
        View contentView = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar.make(contentView, text, Snackbar.LENGTH_SHORT);
        snackbar.setTextColor(Color.WHITE);
        snackbar.setBackgroundTint(color);
        snackbar.show();
    }
}