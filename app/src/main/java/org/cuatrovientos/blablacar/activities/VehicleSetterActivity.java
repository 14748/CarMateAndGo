package org.cuatrovientos.blablacar.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;
import org.cuatrovientos.blablacar.models.Vehicle;

public class VehicleSetterActivity extends AppCompatActivity {
        private TextInputEditText makeEditText, modelEditText, yearEditText, colorEditText, licensePlateEditText;
        private Button submitButton;

        private User currentUser;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_vehicle_setter);

            UserManager.init(getApplicationContext());
            currentUser = UserManager.getCurrentUser();

            initializeViews();

            submitButton.setOnClickListener(view -> {
                if (validateFields()) {
                    Vehicle vehicle = createVehicleFromInput();
                    currentUser.setVehicle(vehicle);
                    UserManager.setCurrentUser(currentUser);
                    Utils.pushUser(currentUser);

                    Intent createRoute = new Intent(this, CreateRoute.class);
                    startActivity(createRoute);
                    finish();

                } else {
                    Toast.makeText(VehicleSetterActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void initializeViews() {
            makeEditText = findViewById(R.id.vehicle_make);
            modelEditText = findViewById(R.id.vehicle_model);
            yearEditText = findViewById(R.id.vehicle_year);
            colorEditText = findViewById(R.id.vehicle_color);
            licensePlateEditText = findViewById(R.id.vehicle_license_plate);
            submitButton = findViewById(R.id.submit_vehicle_button);
        }

        private boolean validateFields() {
            return !TextUtils.isEmpty(makeEditText.getText()) &&
                    !TextUtils.isEmpty(modelEditText.getText()) &&
                    !TextUtils.isEmpty(yearEditText.getText()) &&
                    !TextUtils.isEmpty(colorEditText.getText()) &&
                    !TextUtils.isEmpty(licensePlateEditText.getText());
        }

        private Vehicle createVehicleFromInput() {
            String make = makeEditText.getText().toString();
            String model = modelEditText.getText().toString();
            int year = Integer.parseInt(yearEditText.getText().toString());
            String color = colorEditText.getText().toString();
            String licensePlate = licensePlateEditText.getText().toString();

            return new Vehicle(make, model, year, color, licensePlate);

    }
}