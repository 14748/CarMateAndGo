package org.cuatrovientos.blablacar;

import android.content.Context;
import android.content.SharedPreferences;

import org.cuatrovientos.blablacar.models.User;
import org.cuatrovientos.blablacar.models.Utils;

public class UserManager {

    private static User currentUser;

    public static User getCurrentUser(){
        return currentUser;
    }
    public static void setCurrentUser(User user){
        currentUser = user;
    }
}