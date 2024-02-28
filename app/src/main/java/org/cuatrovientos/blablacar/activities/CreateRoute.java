package org.cuatrovientos.blablacar.activities;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.models.PlaceOpenStreetMap;

import java.io.Serializable;
import java.text.BreakIterator;
import java.util.Calendar;

public class CreateRoute extends AppCompatActivity {

    TextView origin;
    Double originLat;
    Double originLon;
    PlaceOpenStreetMap PlaceOrigin;
    TextView destination;
    Double destinationLat;
    Double destinationLon;
    PlaceOpenStreetMap PlaceDestination;
    TextView date;

    Button createRoute;

    int codigoDeSolicitud = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_route);

        origin = findViewById(R.id.lblOrigin);
        destination = findViewById(R.id.lblDestination);
        date = findViewById(R.id.lblDate);
        createRoute = findViewById(R.id.btnCreateRoute);





        origin.setOnClickListener(v -> {
            Intent intent = new Intent(CreateRoute.this, Search.class);
            intent.putExtra("type", "origin");
            startActivityForResult(intent, codigoDeSolicitud);
        });

        destination.setOnClickListener(v -> {
            Intent intent = new Intent(CreateRoute.this, Search.class);
            intent.putExtra("type", "destination");
            startActivityForResult(intent, codigoDeSolicitud);
        });

        date.setOnClickListener(v -> {
            mostrarDatePicker();
        });










        createRoute.setOnClickListener(v -> {
            Intent intent = new Intent(CreateRoute.this, MainActivity.class);
            intent.putExtra("origin", (Serializable) PlaceOrigin);
            intent.putExtra("destination", (Serializable) PlaceDestination);
            intent.putExtra("date", date.getText().toString());
            finish();
        });







    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verificar si el resultado es de ActivityB y si fue exitoso
        if (requestCode == codigoDeSolicitud && resultCode == RESULT_OK) {
            // Obtener los datos enviados desde ActivityB

            PlaceOpenStreetMap origin = (PlaceOpenStreetMap) data.getSerializableExtra("origin");
            PlaceOpenStreetMap destination = (PlaceOpenStreetMap) data.getSerializableExtra("destination");

            if (origin != null) {
                this.originLat = Double.valueOf(origin.getLat());
                this.originLon = Double.valueOf(origin.getLon());
                this.origin.setText(origin.getDisplayName());
            } else {
                // Handle the case where origin is null, log an error, show a message, etc.
                Log.e(TAG, "Received null origin from the intent");
            }

            if (destination != null) {
                this.destinationLat = Double.valueOf(destination.getLat());
                this.destinationLon = Double.valueOf(destination.getLon());
                // Assuming you have a destination TextView, set its text
                this.destination.setText(destination.getDisplayName());
            } else {
                // Handle the case where destination is null, log an error, show a message, etc.
                Log.e(TAG, "Received null destination from the intent");
            }

        }
    }
    protected void mostrarDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fechaSeleccionada = dayOfMonth + "/" + (month + 1) + "/" + year;
                date.setText(fechaSeleccionada);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

}