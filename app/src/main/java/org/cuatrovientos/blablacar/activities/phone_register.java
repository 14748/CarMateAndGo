package org.cuatrovientos.blablacar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hbb20.CountryCodePicker;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.activities.chat.MainActivityChat;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

public class phone_register extends AppCompatActivity {
    CountryCodePicker countryCodePicker;
    EditText phoneInput;
    Button sendOtpBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_register);

        countryCodePicker = findViewById(R.id.login_countrycode);
        phoneInput = findViewById(R.id.login_mobile_number);
        sendOtpBtn = findViewById(R.id.send_otp_btn);
        progressBar = findViewById(R.id.login_progress_bar);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        sendOtpBtn.setOnClickListener((v)->{
            if(!countryCodePicker.isValidFullNumber()){
                phoneInput.setError("Phone number not valid");
                return;
            }

            Intent intent = new Intent(phone_register.this,activity_verify_otp.class);
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}