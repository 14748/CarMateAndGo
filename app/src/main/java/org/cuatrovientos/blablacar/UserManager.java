package org.cuatrovientos.blablacar;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson; // Add Gson dependency for object serialization
import org.cuatrovientos.blablacar.models.User;

public class UserManager {

    private static final String PREFS_NAME = "BlablaCarPrefs";
    private static final String USER_KEY = "CurrentUser";
    private static User currentUser;
    private static SharedPreferences prefs;

    // Initialize SharedPreferences
    public static void init(Context context) {
        if (prefs == null) {
            prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        }
        loadUser(); // Load user at initialization
    }

    private static void saveUser() {
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String userJson = gson.toJson(currentUser);
        editor.putString(USER_KEY, userJson);
        editor.apply();
    }

    private static void loadUser() {
        String userJson = prefs.getString(USER_KEY, null);
        if (userJson != null) {
            Gson gson = new Gson();
            currentUser = gson.fromJson(userJson, User.class);
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
        saveUser(); // Save user when it's set
    }

    public static void logoutUser() {
        currentUser = null;
        prefs.edit().remove(USER_KEY).apply(); // Clear user from SharedPreferences
    }
}
