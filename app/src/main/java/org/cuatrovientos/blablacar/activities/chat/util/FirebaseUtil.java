package org.cuatrovientos.blablacar.activities.chat.util;

import android.content.Context;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.cuatrovientos.blablacar.UserManager;
import org.cuatrovientos.blablacar.models.User;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {

    public static String currentUserId(Context appContext){
        UserManager.init(appContext);
        return UserManager.getCurrentUser().getId();
    }

    public static boolean isLoggedIn(Context appContext){
        if(currentUserId(appContext)!=null){
            return true;
        }
        return false;
    }

    public static DocumentReference currentUserDetails(Context context){
        return FirebaseFirestore.getInstance().collection("UsersTest1").document(currentUserId(context));
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("UsersTest1");
    }

    public static DocumentReference getChatroomReference(String chatroomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId);
    }

    public static CollectionReference getChatroomMessageReference(String chatroomId){
        return getChatroomReference(chatroomId).collection("chats");
    }

    public static String getChatroomId(String userId1,String userId2){
        if(userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }else{
            return userId2+"_"+userId1;
        }
    }

    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }

    public static DocumentReference getOtherUserFromChatroom(List<String> userIds, Context context){
        if(userIds.get(0).equals(FirebaseUtil.currentUserId(context))){
            return allUserCollectionReference().document(userIds.get(1));
        }else{
            return allUserCollectionReference().document(userIds.get(0));
        }
    }

    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
}










