package org.cuatrovientos.blablacar.activities.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class BalanceActivity extends AppCompatActivity {

    private CircularSlider circularSlider;
    private Button btnContinue;
    private TextView txtBalance;
    private  String SECRET_KEY = "sk_test_51AXFDMASksDI5wteut0RWWQIgMBOC9wlKxJTrmNAbn2RUAm6LZh81zCmtsOsyGDwZqHYupxIkKu22o1mmEwvAHIV00OPIaBXNZ";
    private  String PUBLISH_KEY = "pk_test_Kk44etb9arfGs64zFUGivUPZ";
    PaymentSheet paymentSheet;
    String customerID;
    String EphericalKey;
    String ClientSecret;
    float addingMoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        UserManager.init(getApplicationContext());
        User currentUser = UserManager.getCurrentUser();

        circularSlider = findViewById(R.id.circularSlider);
        btnContinue = findViewById(R.id.button_continue);
        txtBalance = findViewById(R.id.balanceTextView);

        txtBalance.setText("€" + String.format("%.2f", currentUser.getBalance()));

        PaymentConfiguration.init(this, PUBLISH_KEY);

        paymentSheet = new PaymentSheet(this, paymentSheetResult -> {
            if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
                
                Log.d("PaymentSuccess", "Payment completed successfully.");

                
                currentUser.setBalance(currentUser.getBalance() + addingMoney);

                
                Utils.pushUser(currentUser);
                UserManager.setCurrentUser(currentUser);

                
                Intent profileIntent = new Intent(BalanceActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
                finish();
            }
        });


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/customers",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            customerID = object.getString("id");
                            


                            getEphericalKey(customerID);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
                Log.e("NetworkError", error.toString());
            }

        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BalanceActivity.this);
        requestQueue.add(stringRequest);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addingMoney = circularSlider.getAddingMoney();
                if (addingMoney > 0){
                    Log.e("StripeError", "Invalid amount: " + addingMoney);
                    PaymentFlow();
                }
            }
        });
    }

    private void getEphericalKey(String customerID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/ephemeral_keys",
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            EphericalKey = object.getString("id");
                            
                            
                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                
                Log.e("NetworkError", error.toString());
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                header.put("Stripe-Version", "2023-10-16");
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError{
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(BalanceActivity.this);
        requestQueue.add(stringRequest);
    }
    
    interface ClientSecretCallback {
        void onSuccess(String clientSecret);
        void onError(String error);
    }

    private void getClientSecret(String customerID, String ephericalKey, final ClientSecretCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                "https://api.stripe.com/v1/payment_intents",
                response -> {
                    try {
                        JSONObject object = new JSONObject(response);
                        ClientSecret = object.getString("client_secret");
                        Log.d("PaymentFlow", "client secret: " + ClientSecret);
                        callback.onSuccess(ClientSecret); 
                    } catch (JSONException e) {
                        e.printStackTrace();
                        callback.onError("JSON parsing error: " + e.getMessage()); 
                    }
                },
                error -> {
                    String responseBody = new String(error.networkResponse.data, StandardCharsets.UTF_8);
                    Log.e("NetworkError", responseBody);
                    callback.onError("Network error: " + responseBody); 
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + SECRET_KEY);
                return header;
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer", customerID);
                params.put("amount", convertFloatToStripeAmount(addingMoney));
                params.put("currency", "eur");
                params.put("automatic_payment_methods[enabled]", "true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    
    private void PaymentFlow() {
        addingMoney = circularSlider.getAddingMoney(); 
        if (addingMoney > 0) {
            getClientSecret(customerID, EphericalKey, new ClientSecretCallback() {
                @Override
                public void onSuccess(String clientSecret) {
                    
                    paymentSheet.presentWithPaymentIntent(clientSecret, new PaymentSheet.Configuration(
                            "Cuatro Vientos CI",
                            new PaymentSheet.CustomerConfiguration(customerID, EphericalKey)));
                }

                @Override
                public void onError(String error) {
                    
                    
                }
            });
        } else {
            
            
        }
    }


    public static String convertFloatToStripeAmount(float amount) {
        int stripeAmount = Math.round(amount * 100);
        return String.valueOf(stripeAmount);
    }
}