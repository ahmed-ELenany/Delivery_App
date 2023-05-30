package com.mk_tech.delivery.services;


import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;

/**
 *
 */

public class MyFirebaseInstanceIdService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseIdService";
    private static final String TOPIC_GLOBAL = "global";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }


}