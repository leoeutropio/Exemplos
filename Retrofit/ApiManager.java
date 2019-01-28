package com.devmaker.siftkin.webservice.retrofit;

import android.content.Context;

import com.devmaker.siftkin.dao.LocalDbImplement;
import com.devmaker.siftkin.webservice.model.request.LoginRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by DevMaker on 9/28/16.
 */

public class ApiManager {

    private OkHttpClient okHttpClient;
    private Retrofit retrofit;
    public final static String cepUrl = "https://viacep.com.br/ws/";
    public static String MAIN = "MAIN";
    public static String IBGE = "IBGE";
    private Context context;
    String endpoint = "http://192.168.100.242:8080/api/";
    String endPointHomolog = "http://hom-siftkin.devmakerdigital.com.br/api/";

    public ApiManager(final Context context){
        this.context = context;

        MyHttpLoggingInterceptor logInterceptor = new MyHttpLoggingInterceptor();
        logInterceptor.setLevel(MyHttpLoggingInterceptor.Level.BODY);

        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                final LoginRequest loginRequest = new LocalDbImplement<LoginRequest>(context).getDefault(LoginRequest.class);
                Request original = chain.request();
                if (loginRequest != null) {
                    Request.Builder request = original.newBuilder()
                            .addHeader("Authorization","Bearer " + loginRequest.getToken())
                            .addHeader("Content-Type", "application/json")
                            .addHeader("Accept", "application/json");
                    return chain.proceed(request.build());
                }

                Request.Builder request = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Accept", "application/json");
                return chain.proceed(request.build());

            }
        };

        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(30000*6, TimeUnit.MILLISECONDS)
                .readTimeout(30000*6, TimeUnit.MILLISECONDS)
                .writeTimeout(30000*6, TimeUnit.MILLISECONDS)
                .addInterceptor(logInterceptor)
                .addInterceptor(interceptor)
                .build();

        Gson gson = new GsonBuilder()
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(endPointHomolog)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public ApiManager(Retrofit retrofit, OkHttpClient okHttpClient) {
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public ApiManager(Context context, Retrofit retrofit, OkHttpClient okHttpClient) {
        this.context = context;
        this.retrofit = retrofit;
        this.okHttpClient = okHttpClient;
    }

    public static ApiManager getViaCep(){
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor();
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .writeTimeout(10000, TimeUnit.MILLISECONDS)
                .addInterceptor(logInterceptor)
                .build();


        Gson gson = new GsonBuilder()
                .create();

        //http://maps.google.com/maps/api/geocode/json?components=country:Brazil&language=PT-br?address=
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(cepUrl)
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return new ApiManager(retrofit,okHttpClient);
    }

}
/*new ApiManager(this)
                .getRetrofit()
                .create(RequestInterfaceUser.class)
                .cadastro(params)
                .enqueue(new Cus*/