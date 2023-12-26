package com.example.ph26437_doctruyen_app_assignment_mob403.SharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.ph26437_doctruyen_app_assignment_mob403.User.Model.User;
import com.google.gson.Gson;

public class MySharedPreferences {
    private static final String MY_SHARED_PREFS = "MySharedPrefs";
    private static final String MY_USERNAME = "myUsername";
    private static final String MY_PASSWD = "myPassword";
    private static final String MY_SAVEDATA = "myData";
    private static final String MY_ACCOUNT = "myAccount";
    public static void saveStrings(Context context, String my_username, String my_password, String my_data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MY_USERNAME, my_username);
        editor.putString(MY_PASSWD, my_password);
        editor.putString(MY_SAVEDATA, my_data);
        editor.apply();
    }
    public static String[] getStrings(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        String my_username = sharedPreferences.getString(MY_USERNAME, "");
        String my_password = sharedPreferences.getString(MY_PASSWD, "");
        String my_data = sharedPreferences.getString(MY_SAVEDATA, "false");
        return new String[] {my_username, my_password, my_data};
    }
    public static void saveAccount(Context context, User user){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String dataUser = gson.toJson(user);
        editor.putString(MY_ACCOUNT, dataUser);
        editor.apply();
    }
    public static User getAccount(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, Context.MODE_PRIVATE);
        String my_account = sharedPreferences.getString(MY_ACCOUNT, "");
        if (my_account.isEmpty()){
            return null;
        }
        Gson gson = new Gson();
        User user = gson.fromJson(my_account, User.class);
        return user;
    }
}

