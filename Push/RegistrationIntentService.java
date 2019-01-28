package com.devmaker.siftkin.push;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.devmaker.siftkin.dao.LocalDbImplement;
import com.devmaker.siftkin.webservice.model.request.DeviceTokenResponse;
import com.devmaker.siftkin.webservice.model.request.LoginRequest;
import com.devmaker.siftkin.webservice.retrofit.ApiManager;
import com.devmaker.siftkin.webservice.retrofit.RequestInterfaceUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DevmakerDiego on 18/10/2017.
 */

public class RegistrationIntentService extends IntentService {

    public RegistrationIntentService() {
        super("RegistationIntentService");
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, RegistrationIntentService.class);
        context.startService(intent);
    }

    public static void delete(Context context) {
        Intent intent = new Intent(context, RegistrationIntentService.class);
        intent.putExtra("delete",true);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (intent != null) {

            LoginRequest usuario = new LocalDbImplement<LoginRequest>(getBaseContext()).getDefault(LoginRequest.class);
            if (usuario == null || usuario.getData().getId() == null){
                try {
                    if(intent.hasExtra("delete")){
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    }
                }catch (Exception ex){
                    Crashlytics.logException(ex);
                }
                return;
            }

            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                @Override
                public void onSuccess(InstanceIdResult instanceIdResult) {
                    String refreshedToken = instanceIdResult.getToken();

                    if (refreshedToken.length() == 0)
                        return;

                    String device_model = "android";
//                    try {
//                        device_model = Settings.Secure.getString(RegistrationIntentService
//                                .this.getContentResolver(), Settings.Secure.ANDROID_ID);
//                    } catch (Exception ex) {
//                        Log.d("RegistrationIntent", ex.getMessage());
//                    }

                    Map<String,String> param = new HashMap<>();
                    param.put("device_token", refreshedToken);
                    param.put("device_model", device_model);

                    new ApiManager(RegistrationIntentService.this)
                            .getRetrofit()
                            .create(RequestInterfaceUser.class)
                            .atualizarDeviceToken(param)
                            .enqueue(new Callback<DeviceTokenResponse>() {
                                @Override
                                public void onResponse(Call<DeviceTokenResponse> call, Response<DeviceTokenResponse> response) {
                                    Log.d("TAG", "onResponse: " + response.isSuccessful());
                                }

                                @Override
                                public void onFailure(Call<DeviceTokenResponse> call, Throwable t) {
                                    Log.e("TAG", "onFailure: " + t.getMessage());
                                }
                            });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });

        }
    }

}