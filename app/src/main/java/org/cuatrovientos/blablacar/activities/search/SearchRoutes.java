package org.cuatrovientos.blablacar.activities.search;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.history.UserTripsActivity;
import org.cuatrovientos.blablacar.activities.chat.MainActivityChat;
import org.cuatrovientos.blablacar.activities.create.CreateRoute;
import org.cuatrovientos.blablacar.activities.profile.ProfileActivity;
import org.cuatrovientos.blablacar.activities.seachInput.Search;
import org.cuatrovientos.blablacar.models.PlaceOpenStreetMap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchRoutes extends AppCompatActivity {

     Button searchRoutesButton;
     TextView origin;
    Double originLat;
    Double originLon;
    PlaceOpenStreetMap PlaceOrigin;
     TextView destination;
    Double destinationLat;
    Double destinationLon;
    PlaceOpenStreetMap PlaceDestination;

     TextView date;
     Button switchRouteButton;

     boolean ida = true;

    int codigoDeSolicitud = 1;

    private ImageButton btnSearch;
    private ImageButton btnPublish;
    private ImageButton btnHistory;
    private ImageButton btnMessages;
    private ImageButton btnProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_routes);

        searchRoutesButton = findViewById(R.id.btnSearchRoute);
        origin = findViewById(R.id.lblOriginSearch);
        destination = findViewById(R.id.lblDestinationSearch);
        date = findViewById(R.id.lblDateSearch);
        switchRouteButton = findViewById(R.id.btnSwitchRouteSearch);

        PlaceOrigin = new PlaceOpenStreetMap();
        PlaceDestination = new PlaceOpenStreetMap();

        btnSearch = findViewById(R.id.btnSearch);
        btnPublish = findViewById(R.id.btnPublish);
        btnHistory = findViewById(R.id.btnHistory);
        btnMessages = findViewById(R.id.btnMessages);
        btnProfile = findViewById(R.id.btnProfile);

        btnPublish.setOnClickListener(view -> {
            Intent publishIntent = new Intent(this, CreateRoute.class);
            startActivity(publishIntent);
        });


        btnHistory.setOnClickListener(view -> {
            Intent historyIntent = new Intent(this, UserTripsActivity.class);
            startActivity(historyIntent);
        });

        btnMessages.setOnClickListener(view -> {
            Intent messagesIntent = new Intent(this, MainActivityChat.class);
            startActivity(messagesIntent);
        });

        btnProfile.setOnClickListener(view -> {

            Intent profileIntent = new Intent(this, ProfileActivity.class);
            startActivity(profileIntent);

        });

        origin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchRoutes.this, Search.class);
                intent.putExtra("type", "origin-search");
                startActivityForResult(intent, codigoDeSolicitud);
            }
        });

        destination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchRoutes.this, Search.class);
                intent.putExtra("type", "destination-search");
                startActivityForResult(intent, codigoDeSolicitud);
            }
        });

        destination.setText("C.I. Cuatrovientos");
        destinationLat = 42.824851;
        destinationLon = -1.660318;

        PlaceDestination.setLat(destinationLat.toString());
        PlaceDestination.setLon(destinationLon.toString());


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Date dateToday = new Date();
        String formattedDate = dateFormat.format(dateToday);
        date.setText(formattedDate);


        date.setOnClickListener(v -> {
            mostrarDatePicker();
        });

        switchRouteButton.setOnClickListener(v -> {
            ida = !ida;
            String originText = origin.getText().toString();
            String destinationText = destination.getText().toString();
            Double originLat = this.originLat;
            Double originLon = this.originLon;
            this.originLat = this.destinationLat;
            this.originLon = this.destinationLon;
            this.destinationLat = originLat;
            this.destinationLon = originLon;
            PlaceOpenStreetMap temp = PlaceDestination;
            PlaceDestination = PlaceOrigin;
            PlaceOrigin = temp;
            origin.setText(destinationText);
            destination.setText(originText);
        });

        searchRoutesButton.setOnClickListener(view -> {
            if (PlaceOrigin == null || PlaceDestination == null ||
                    origin.getText().toString().isEmpty() ||
                    destination.getText().toString().isEmpty() ||
                    date.getText().toString().isEmpty()) {

                Toast.makeText(getApplicationContext(), "Debes rellenar todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent searchIntent = new Intent(SearchRoutes.this, RouteFinderActivity.class);
            searchIntent.putExtra("origin", PlaceOrigin);
            searchIntent.putExtra("destination", PlaceDestination);
            searchIntent.putExtra("date", date.getText().toString());
            searchIntent.putExtra("type", ida);
            searchIntent.putExtra("text", origin.getText().toString() + " -> " + destination.getText().toString());
            setResult(RESULT_OK, searchIntent);
            startActivity(searchIntent);
        });










    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        
        if (requestCode == codigoDeSolicitud && resultCode == RESULT_OK) {
            

            PlaceOpenStreetMap origin = (PlaceOpenStreetMap) data.getSerializableExtra("origin");
            PlaceOpenStreetMap destination = (PlaceOpenStreetMap) data.getSerializableExtra("destination");

            if (origin != null) {
                this.originLat = Double.valueOf(origin.getLat());
                this.originLon = Double.valueOf(origin.getLon());



                this.PlaceOrigin.setLat(origin.getLat());
                this.PlaceOrigin.setLon(origin.getLon());

                String textoCortado = cortarTexto(origin.getDisplayName(), 20);
                this.origin.setText(textoCortado);
            } else {
                
                Log.e(TAG, "Received null origin from the intent");
            }

            if (destination != null) {
                this.destinationLat = Double.valueOf(destination.getLat());
                this.destinationLon = Double.valueOf(destination.getLon());


                this.PlaceDestination.setLat(destination.getLat());
                this.PlaceDestination.setLon(destination.getLon());

                
                String textoCortado = cortarTexto(destination.getDisplayName(), 20);
                this.destination.setText(textoCortado);
            } else {
                
                Log.e(TAG, "Received null destination from the intent");
            }
        }
    }


    protected void mostrarDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerTheme, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                final int selectedYear = year;
                final int selectedMonth = month;
                final int selectedDay = dayOfMonth;

                
                mostrarTimePicker(selectedYear, selectedMonth, selectedDay);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    private void mostrarTimePicker(final int year, final int month, final int day) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.TimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String fechaSeleccionada = day + "/" + (month + 1) + "/" + year;
                String horaSeleccionada = hourOfDay + ":" + minute;

                
                String fechaHoraSeleccionada = fechaSeleccionada + " " + horaSeleccionada;

                
                date.setText(fechaHoraSeleccionada);
            }
        }, hour, minute, true);

        timePickerDialog.show();
    }


    private static String cortarTexto(String texto, int longitudMaxima) {
        if (texto.length() > longitudMaxima) {
            return texto.substring(0, longitudMaxima)+"...";
        } else {
            return texto;
        }
    }
}