package com.smartu.servicios;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smartu.R;
import com.smartu.utilidades.Constantes;
import com.smartu.vistas.FragmentNotificaciones;
import com.smartu.vistas.MainActivity;

import java.util.Map;

/**
 * Created by NeN on 21/05/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "¡Mensaje recibido!");
        //Si es un mensaje del chat de un usuario
        if(remoteMessage.getData().containsKey("fcm_token")) {
                String title = remoteMessage.getData().get("title");
                String message = remoteMessage.getData().get("text");
                String username = remoteMessage.getData().get("username");
                String uid = remoteMessage.getData().get("uid");
                String fcmToken = remoteMessage.getData().get("fcm_token");
                //Muestro la notifiación
                sendNotification(title, message, username, uid, fcmToken);
        }else {
            //Es una nueva notificación de que alguien ha creado algo
            displayNotification(remoteMessage.getNotification(), remoteMessage.getData());
            //Envío los datos al RecyclerCiew correspondiente para que se actualice
            sendNewPromoBroadcast(remoteMessage);
        }
    }
    /**
     * Create and show a simple notification containing the received FCM message.
     */
    private void sendNotification(String title,
                                  String message,
                                  String receiver,
                                  String receiverUid,
                                  String firebaseToken) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(Constantes.ARG_RECEIVER, receiver);
        intent.putExtra(Constantes.ARG_RECEIVER_UID, receiverUid);
        intent.putExtra(Constantes.ARG_FIREBASE_TOKEN, firebaseToken);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.mensaje)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

    /**
     * Envía la notificación para que la recoja el correspondiente RecyclerView que está esperandola
     * Se encuentra en FragmentNotificaciones en el onCreate.....
     * @param remoteMessage
     */
    private void sendNewPromoBroadcast(RemoteMessage remoteMessage) {
        Intent intent = new Intent(FragmentNotificaciones.ACTION_NOTIFY_NEW_NOTIFICACION);
        intent.putExtra("nombre", remoteMessage.getNotification().getTitle());
        intent.putExtra("descripcion", remoteMessage.getNotification().getBody());
        intent.putExtra("fecha", remoteMessage.getData().get("fecha"));
        intent.putExtra("idusuario", remoteMessage.getData().get("idusuario"));
        intent.putExtra("idproyecto", remoteMessage.getData().get("idproyecto"));
        intent.putExtra("usuario", remoteMessage.getData().get("usuario"));
        intent.putExtra("proyecto", remoteMessage.getData().get("proyecto"));
        LocalBroadcastManager.getInstance(getApplicationContext())
                .sendBroadcast(intent);
    }

    /**
     * Muestra que ha habido una nueva notificación
     * @param notification
     * @param data
     */
    private void displayNotification(RemoteMessage.Notification notification, Map<String, String> data) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setContentTitle(notification.getTitle())
                .setContentText(notification.getBody())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}
