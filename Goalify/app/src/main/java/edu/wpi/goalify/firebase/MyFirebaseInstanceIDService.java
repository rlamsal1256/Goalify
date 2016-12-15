package edu.wpi.goalify.firebase;


import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by tupac on 12/5/2016.
 */


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

    private BigInteger SENDER_ID = new BigInteger("190995827288");

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);



        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {

//
//        new Thread(new Runnable() {
//            public void run() {
//
//                AtomicInteger msgId = new AtomicInteger();
//
//                FirebaseMessaging fm = FirebaseMessaging.getInstance();
//                fm.send(new RemoteMessage.Builder(SENDER_ID + "@gcm.googleapis.com")
//                        .setMessageId(Integer.toString(msgId.incrementAndGet()))
//                        .addData("my_message", "Sending token")
//                        .addData("my_action","Save token and send notification")
//                        .build());
//
//            }
//        }).start();
    }
}
