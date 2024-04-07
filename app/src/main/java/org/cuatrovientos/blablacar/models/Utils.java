package org.cuatrovientos.blablacar.models;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static void getUsers(final FirebaseCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersTest1")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<User> users = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot document : list) {
                                User user = document.toObject(User.class);
                                users.add(user);
                            }
                        }
                        callback.onCallback(users); // Callback with users
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(TAG, "Error getting documents: ", e);
                    }
                });
    }

    // Interface remains unchanged

    public static void pushUser(User user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersTest1").document(user.getId())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public static void getUserById(String userId, final FirebaseCallbackUser callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("UsersTest1").document(userId);
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            User user = documentSnapshot.toObject(User.class);
            callback.onCallback(user); // Callback with user
        }).addOnFailureListener(e -> Log.w(TAG, "Error getting document: ", e));
    }

    public static void getUsersByIds(List<String> userIds, final UsersCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference usersRef = database.getReference("UsersTest1");
        List<User> users = new ArrayList<>();
        AtomicInteger remainingUsers = new AtomicInteger(userIds.size());

        if (userIds.isEmpty()) {
            callback.onCallback(users); // Return an empty list immediately if no IDs are provided
            return;
        }

        for (String id : userIds) {
            usersRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        users.add(user);
                    }
                    // Check if this is the last user to fetch
                    if (remainingUsers.decrementAndGet() == 0) {
                        callback.onCallback(users); // Return the list of users once all have been fetched
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Decrement the count and check if it's time to callback even if there's an error
                    if (remainingUsers.decrementAndGet() == 0) {
                        callback.onCallback(users);
                    }
                }
            });
        }
    }

    public interface UsersCallback {
        void onCallback(List<User> users);
    }

    public interface FirebaseCallbackUser {
        void onCallback(User user);
    }

    public interface FirebaseCallback {
        void onCallback(List<User> userList);
    }

    public static String[] colors = new String[]
            {"F44336", "E91E63", "9C27B0", "673AB7", "3F51B5",
                    "03A9F4", "009688", "4CAF50", "CDDC39", "FFC107",
                    "FF5722", "795548", "9E9E9E", "455A64", "FF5722"};

    public static String getRandomColor() {
        // Número aleatorio entre [0] y [14];
        int randonNumber = new Random().nextInt(colors.length) + 0;
        // Devolvemos el color
        return colors[randonNumber];
    }

}
