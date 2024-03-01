package org.cuatrovientos.blablacar.models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static List<LatLng> convertCustomLatLngListToLatLngList(List<CustomLatLng> customLatLngList) {
        List<LatLng> latLngList = new ArrayList<>();
        for (CustomLatLng customLatLng : customLatLngList) {
            LatLng latLng = new LatLng(customLatLng.getLatitude(), customLatLng.getLongitude());
            latLngList.add(latLng);
        }
        return latLngList;
    }
    public static List<CustomLatLng> convertLatLngListToCustomLatLngList(List<LatLng> latLngList) {
        List<CustomLatLng> customLatLngList = new ArrayList<>();
        for (LatLng latLng : latLngList) {
            CustomLatLng customLatLng = new CustomLatLng(latLng.latitude, latLng.longitude);
            customLatLngList.add(customLatLng);
        }
        return customLatLngList;
    }

    public static List<List<LatLng>> convertListOfCustomLatLngListToListOfLatLngList(List<List<CustomLatLng>> customLatLngLists) {
        List<List<LatLng>> latLngLists = new ArrayList<>();
        for (List<CustomLatLng> customLatLngList : customLatLngLists) {
            List<LatLng> latLngList = convertCustomLatLngListToLatLngList(customLatLngList);
            latLngLists.add(latLngList);
        }
        return latLngLists;
    }

    public static void getUsers(FirebaseCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("UsersTest");

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Assuming each child under "Users" is a User object
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                }
                callback.onCallback(users); // Pass the users list to the callback
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Data could not be retrieved " + databaseError.getMessage());
            }
        });
    }

    public interface FirebaseCallback {
        void onCallback(List<User> userList);
    }

    public static void pushUser(User user) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("UsersTest");

        // Generate a new unique key for each new user
        String userId = String.valueOf(user.getId());

        // Use the generated key to create a new entry in your database
        if (userId != null) {
            usersRef.child(userId).setValue(user)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "User added successfully!"))
                    .addOnFailureListener(e -> Log.d(TAG, "Failed to add user: " + e.getMessage()));
        }
    }

    public static void updateUser(User user) {
        if (user == null) {
            Log.d("FirebaseUserManager", "User or User ID is null, cannot update");
            return;
        }

        String userId = String.valueOf(user.getId()); // Assuming id is stored as a String in Firebase. Adjust if it's actually an int.
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("UsersTest");

        usersRef.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> Log.d("FirebaseUserManager", "User updated successfully with ID: " + userId))
                .addOnFailureListener(e -> Log.d("FirebaseUserManager", "Failed to update user with ID: " + userId + "; Error: " + e.getMessage()));
    }



    public static void getUserById(String userId, FirebaseCallbackUser callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userRef = database.getReference("UsersTest").child(userId);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                callback.onCallback(user); // Pass the single user to the callback
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Data could not be retrieved " + databaseError.getMessage());
            }
        });
    }

    public interface FirebaseCallbackUser {
        void onCallback(User user);
    }

}
