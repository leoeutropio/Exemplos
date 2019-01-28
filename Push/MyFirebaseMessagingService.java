package com.devmaker.siftkin.push;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.crashlytics.android.Crashlytics;
import com.devmaker.siftkin.MainActivity;
import com.devmaker.siftkin.R;
import com.devmaker.siftkin.dao.LocalDbImplement;
import com.devmaker.siftkin.webservice.model.request.LoginRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.util.Map;

/**
 * Created by DevmakerDiego on 07/08/2018.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            notification(remoteMessage.getData());
        }
        else
            // Check if message contains a notification payload.
            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                notification(remoteMessage.getNotification());
            }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    public void notification(Map<String, String> body){
        try {
            if(body.containsKey("pedido_id")){
                notificationPedido(body.containsKey("title") ? body.get("title") : getResources().getString(R.string.app_name)
                        ,body.get("body"),body.get("pedido_id")
                        ,body.containsKey("nome")?body.get("nome"):""
                        ,body.containsKey("imagem")?body.get("imagem"):""  );
            }else{
                notification(body.containsKey("title") ? body.get("title") : getResources().getString(R.string.app_name)
                        ,body.get("body"));
            }


        }catch (Exception ex){
            Crashlytics.logException(ex);
        }

    }

    public void notification(RemoteMessage.Notification notification){
        notification(notification.getTitle(),notification.getBody());
    }

    @SuppressLint("StaticFieldLeak")
    public void notificationPedido(final String title, final String body, final String id, String nome, final String imagem){

        LoginRequest usuario = new LocalDbImplement<LoginRequest>(getBaseContext()).getDefault(LoginRequest.class);
        if (usuario == null || usuario.getData().getId() == null){
            Crashlytics.log("push pedido usuario deslogado -> " + body );
            return;
        }

        new AsyncTask<String,String,Bitmap>(){
            @Override
            protected Bitmap doInBackground(String... strings) {
                try {
                    if(!imagem.isEmpty()){
                        return Glide.with(MyFirebaseMessagingService.this)
                                .asBitmap()
                                .load(imagem)
                                .apply(new RequestOptions().centerCrop())
                                .submit(100,100).get();
                    }else{
                        return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    }

                }catch (Exception ex){
                    return BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                }
            }

            @Override
            protected void onPostExecute(Bitmap s) {
                super.onPostExecute(s);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("id", Integer.parseInt(id));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                //configura a estrutura
                Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(MyFirebaseMessagingService.this, "siftkin")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(s)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setVibrate(new long[] {1000,1000,1000,1000,1000})
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSound(soundUri)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(body))
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;
                    NotificationChannel notificationChannel = new NotificationChannel("001", "Notificações", importance);
                    notificationChannel.enableLights(true);
                    notificationChannel.setLightColor(Color.BLUE);
                    notificationChannel.enableVibration(true);
                    notificationChannel.setVibrationPattern(new long[] {100,200,300,400,500,400,300,200,400});
                    assert notificationManager != null;
                    notificationBuilder.setChannelId("001");
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                notificationManager.notify(0, notificationBuilder.build());
            }
        }.execute();

    }

    public void notification(String title,String body){

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        //configura a estrutura
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap largeicon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "siftkin")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(largeicon)
                .setContentTitle(title)
                .setContentText(body)
                .setVibrate(new long[] {1000,1000,1000,1000,1000})
                .setDefaults(Notification.DEFAULT_ALL)
                .setSound(soundUri)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(body))
                .setAutoCancel(true)
        .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel notificationChannel = new NotificationChannel("001", "Notificações", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[] {100,200,300,400,500,400,300,200,400});
            assert notificationManager != null;
            notificationBuilder.setChannelId("001");
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(0, notificationBuilder.build());
    }


}
