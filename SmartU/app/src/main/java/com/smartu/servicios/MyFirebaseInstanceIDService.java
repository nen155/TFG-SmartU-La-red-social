package com.smartu.servicios;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.smartu.utilidades.Constantes;
import com.smartu.utilidades.ControladorPreferencias;

/**
 * Created by NeN on 21/05/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    public MyFirebaseInstanceIDService() {
    }

    @Override
    public void onTokenRefresh() {
        String fcmToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + fcmToken);

        sendTokenToServer(fcmToken);
    }

    private void sendTokenToServer(String fcmToken) {
        ControladorPreferencias.guardarToken(getApplicationContext(),fcmToken);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseDatabase.getInstance()
                    .getReference()
                    .child(Constantes.ARG_USERS)
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .child(Constantes.ARG_FIREBASE_TOKEN)
                    .setValue(fcmToken);
        }
    }
}
