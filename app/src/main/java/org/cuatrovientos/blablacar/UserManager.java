package org.cuatrovientos.blablacar;

import android.content.Context;
import android.content.SharedPreferences;

import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

public class UserManager {
    private static UserManager instance;
    private User currentUser;
    private final Context context;

    private UserManager(Context context) {
        this.context = context.getApplicationContext();
    }

    public static synchronized UserManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserManager(context);
        }
        return instance;
    }

    public void loadUserFromDatabase() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("LoggedInUserId", null);
        if (userId != null) {
            Utils.getUserById(userId, new Utils.FirebaseCallbackUser() {
                @Override
                public void onCallback(User user) {
                    currentUser = user;
                }
            });
        }
    }

    public User getCurrentUser() {
        if (currentUser == null) {
            loadUserFromDatabase();
        }
        return currentUser;
    }

    public void clearCurrentUser() {
        this.currentUser = null;
    }

}