package com.devmaker.siftkin.webservice.retrofit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.devmaker.siftkin.R;
import com.devmaker.siftkin.webservice.model.request.BaseResponse;
import com.google.gson.Gson;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by DevMaker on 7/11/16.
 */
public class CustomCallback<T> implements Callback<T> {
    Context context;
    private Dialog dialog;
    OnResponse onResponse;
    View viewLayout;

    public CustomCallback(Context context, OnResponse<T> onResponse){
        this.context = context;

        dialog = new Dialog(context ,R.style.FullscreenThemeMenu);
        dialog.setContentView(R.layout.dialog_loading);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.show();

        this.onResponse = onResponse;
    }

    @Override
    public void onResponse(Call<T> call, retrofit2.Response<T> response) {
        dialog.dismiss();

        if(response.isSuccessful())
            onResponse.onResponse(response.body());
        else{
            try {
                String erro = response.errorBody().string();
                try {
                    BaseResponse r = new Gson().fromJson(erro,BaseResponse.class);
                    onResponse.onFailure(new Exception(r.getMessage()));
                }catch (Exception ex){
                    onResponse.onFailure(new Exception(erro));
                }
            }catch (Exception ex){
                onResponse.onFailure(new Exception("Ocorreu um erro"));
            }
        }

    }

    @Override
    public void onFailure(Call<T> call, final Throwable t) {
        dialog.dismiss();
        if(viewLayout != null){
            Snackbar snackbar = Snackbar
                    .make(viewLayout, "Problema de conexao", Snackbar.LENGTH_LONG)
                    .setAction("Tentar novamente", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onResponse.onRetry(new Exception(t));
                        }
                    });
            snackbar.show();
        }else {
            //Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(context,R.style.AppTheme_AlertDialog);
            //define o titulo
            builder.setTitle("Problema de conexao");
            //define a mensagem
            builder.setMessage("Gostaria de tentar novamente");
            //define um botão como positivo
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                    onResponse.onRetry(new Exception(t));
                }
            });
            //define um botão como negativo.
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                    onResponse.onFailure(new Exception(t));
                }
            });
            //cria o AlertDialog
            AlertDialog alerta = builder.create();
            //Exibe
            alerta.show();
        }
    }

    public void useSnackBar(View view){
        viewLayout = view;
    }

    public interface OnResponse<T>{
        public void onResponse(T response);
        public void onFailure(Exception t);
        public void onRetry(Exception t);
    }


}