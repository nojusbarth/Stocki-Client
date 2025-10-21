package com.example.stocki_client.data.user;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;

public class UserIdManager {

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_USER_ID = "user_id";

    private static UserIdManager instance;
    private final SharedPreferences prefs;
    private String userId;

    private UserIdManager(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        userId = prefs.getString(KEY_USER_ID, null);

        if (userId == null) {
            userId = UUID.randomUUID().toString();
            prefs.edit().putString(KEY_USER_ID, userId).apply();
        }
    }

    public static synchronized UserIdManager getInstance(Context context) {
        if (instance == null) {
            instance = new UserIdManager(context);
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }
}

