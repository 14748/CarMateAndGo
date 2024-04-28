package org.cuatrovientos.blablacar.models;

import static android.content.ContentValues.TAG;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.cuatrovientos.blablacar.R;
import org.cuatrovientos.blablacar.activities.chat.SearchUserActivity;

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
                        callback.onCallback(users); 
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Log.w(TAG, "Error getting documents: ", e);
                    }
                });
    }

    public static void getTopUsersByCO2Reduction(final FirebaseCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("UsersTest1")
                .orderBy("c02Reduction", Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<User> users = new ArrayList<>();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                                User user = document.toObject(User.class);
                                users.add(user);
                            }
                        }
                        callback.onCallback(users);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error getting documents: ", e);
                    }
                });
    }


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
            callback.onCallback(user); 
        }).addOnFailureListener(e -> Log.w(TAG, "Error getting document: ", e));
    }

    public static void getUsersByIds(List<String> userIds, final UsersCallback callback) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        List<User> users = new ArrayList<>();
        AtomicInteger remainingUsers = new AtomicInteger(userIds.size());

        if (userIds.isEmpty()) {
            callback.onCallback(users);
            return;
        }

        for (String userId : userIds) {
            DocumentReference docRef = db.collection("UsersTest1").document(userId);
            docRef.get().addOnSuccessListener(documentSnapshot -> {
                User user = documentSnapshot.toObject(User.class);
                if (user != null) {
                    synchronized (users) {
                        users.add(user);
                    }
                }
                if (remainingUsers.decrementAndGet() == 0) {
                    callback.onCallback(users);
                }
            }).addOnFailureListener(e -> {
                if (remainingUsers.decrementAndGet() == 0) {
                    callback.onCallback(users);
                }
            });
        }
    }

    public static void setupClickableTextView(Context context, TextView textView, String text, Class<?> targetActivityClass) {
        SpannableString spannableString = new SpannableString(text);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(context, targetActivityClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(true);
                ds.setColor(ContextCompat.getColor(context, R.color.blue_primary));
            }
        };

        int start = text.indexOf("aquí");
        int end = start + "aquí".length();
        if (start == -1) {
            return;
        }
        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannableString);
        textView.setMovementMethod(LinkMovementMethod.getInstance());
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
        
        int randonNumber = new Random().nextInt(colors.length) + 0;
        
        return colors[randonNumber];
    }

}
