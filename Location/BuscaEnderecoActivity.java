package com.devmaker.siftkin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.devmaker.siftkin.extras.Utils;
import com.devmaker.siftkin.webservice.model.request.CepResponse;
import com.devmaker.siftkin.webservice.retrofit.ApiManager;
import com.devmaker.siftkin.webservice.retrofit.CustomCallback;
import com.devmaker.siftkin.webservice.retrofit.RequestInterfaceUser;
import com.google.android.gms.maps.model.LatLng;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscaEnderecoActivity extends AppCompatActivity {

    EditText cep,rua,numero,bairro,cidade,estado;
    Button confirmar;
    String endereco,ruaNumero;
    LatLng localizacao;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_endereco);

        Toolbar toolbar = findViewById(R.id.toolbarbuscarendereco);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        vinculaComponentes();

        cep.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus && rua.getText().toString().equals("")){
                    if (cep.getText().toString().length() == 8) {
                        buscaCepClick();
                    }
                }
            }
        });

        cep.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    buscaCep();
                    return true;
                }
                return false;
            }
        });

        confirmar.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(BuscaEnderecoActivity.this);
                progressDialog.setIndeterminate(true);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(true);
                progressDialog.setMessage("Aguarde...");
                progressDialog.show();

                endereco = rua.getText().toString() + ", " + numero.getText().toString() + ", " +
                        bairro.getText().toString() + ", " + cidade.getText().toString() + ", " + estado.getText().toString();

                ruaNumero = rua.getText().toString() + ", " + numero.getText().toString();

                new AsyncTask<String, String, String>() {
                    @Override
                    protected String doInBackground(String... strings) {
                        try {
                            localizacao = Utils.getLocalizacao(endereco, BuscaEnderecoActivity.this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        super.onPostExecute(s);
                        if (String.valueOf(localizacao.latitude).equals("0.0") || String.valueOf(localizacao.longitude).equals("0.0")) {
                            progressDialog.dismiss();
                            Toast.makeText(BuscaEnderecoActivity.this, "Verifique se seu endereço está correto", Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.dismiss();
                            Intent i = new Intent();
                            i.putExtra("localizacao", localizacao);
                            i.putExtra("endereco", ruaNumero);
                            setResult(RESULT_OK, i);
                            finish();
                        }
                    }
                }.execute();

            }
        });
    }

    private void vinculaComponentes(){
        rua = findViewById(R.id.rua_et);
        bairro = findViewById(R.id.bairro_et);
        numero = findViewById(R.id.numero_et);
        cep = findViewById(R.id.cep_et);
        cidade = findViewById(R.id.cidade_et);
        estado = findViewById(R.id.estado_et);
        confirmar = findViewById(R.id.confirmar_btn);
    }

    private void buscaCep(){
        ApiManager.getViaCep()
                .getRetrofit()
                .create(RequestInterfaceUser.class)
                .cep(cep.getText().toString())
                .enqueue(new CustomCallback<>(this, new CustomCallback.OnResponse<CepResponse>() {
                    @Override
                    public void onResponse(CepResponse response) {
                        if(response.getErro() == null){
                            rua.setText(response.getLogradouro());
                            bairro.setText(response.getBairro());
                            estado.setText(response.getUf());
                            cidade.setText(response.getLocalidade());
                        }else{
                            new AlertDialog.Builder(BuscaEnderecoActivity.this,R.style.AlertDialogTheme)
                                    .setTitle("Atenção")
                                    .setMessage("CEP Inválido")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onFailure(Exception t) {
                        new AlertDialog.Builder(BuscaEnderecoActivity.this,R.style.AlertDialogTheme)
                                .setTitle("Atenção")
                                .setMessage("CEP Inválido")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }

                    @Override
                    public void onRetry(Exception t) {

                    }
                }));
    }

    private void buscaCepClick(){
        ApiManager.getViaCep()
                .getRetrofit()
                .create(RequestInterfaceUser.class)
                .cep(cep.getText().toString())
                .enqueue(new Callback<CepResponse>() {
                    @Override
                    public void onResponse(Call<CepResponse> call, Response<CepResponse> response) {
                        if(response.body() != null && response.body().getErro() == null){
                            rua.setText(response.body().getLogradouro());
                            bairro.setText(response.body().getBairro());
                            estado.setText(response.body().getUf());
                            cidade.setText(response.body().getLocalidade());
                        }else{
                            new AlertDialog.Builder(BuscaEnderecoActivity.this,R.style.AlertDialogTheme)
                                    .setTitle("Atenção")
                                    .setMessage("CEP Inválido")
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<CepResponse> call, Throwable t) {
                        new AlertDialog.Builder(BuscaEnderecoActivity.this,R.style.AlertDialogTheme)
                                .setTitle("Atenção")
                                .setMessage("CEP Inválido")
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                }).show();
                    }
                });
    }

}