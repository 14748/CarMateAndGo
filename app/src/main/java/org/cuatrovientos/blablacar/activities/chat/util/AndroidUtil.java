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
        userModel.setTelephone(intent.getStringExtra("phone")); 
        userModel.setId(intent.getStringExtra("userId"));
        return userModel;
    }

}
