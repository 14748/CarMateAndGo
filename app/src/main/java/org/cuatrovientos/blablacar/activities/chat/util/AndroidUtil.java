package org.cuatrovientos.blablacar.activities.chat.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.cuatrovientos.blablacar.models.User;

public class AndroidUtil {

   public static  void showToast(Context context,String message){
       Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, User model){
       intent.putExtra("username",model.getName());
       intent.putExtra("lastname",model.getLastName());
       intent.putExtra("color",model.getColor());
       intent.putExtra("phone",model.getTelephone());
       intent.putExtra("userId",model.getId());
    }

    public static User getUserModelFromIntent(Intent intent) {
        User userModel = new User();
        userModel.setName(intent.getStringExtra("username"));
        userModel.setLastName(intent.getStringExtra("lastname"));
        userModel.setColor(intent.getStringExtra("color"));
        String telephoneNumberStr = intent.getStringExtra("phone"); // Get the phone number as a String

        // Check if the phone number string is not null before parsing
        if (telephoneNumberStr != null) {
            try {
                int telephoneNumber = Integer.parseInt(telephoneNumberStr);
                userModel.setTelephone(telephoneNumber);
            } catch (NumberFormatException e) {
                userModel.setTelephone(0); // Set default or handle error
                Log.e("ConversionError", "Failed to convert telephone number to integer", e);
            }
        } else {
            userModel.setTelephone(0); // Set default if the phone number string is null
            Log.e("DataError", "Telephone number is missing in the intent data");
        }

        userModel.setId(intent.getStringExtra("userId"));
        return userModel;
    }

}
