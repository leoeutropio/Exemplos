package com.devmaker.siftkin;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.devmaker.siftkin.extras.MaskEditUtil;
import com.devmaker.siftkin.webservice.model.ModelEnvio.CadastroModel.CadastroModel;
import com.devmaker.siftkin.webservice.model.ModelEnvio.CadastroModel.CadastroModelEndereco;
import com.devmaker.siftkin.webservice.model.ModelEnvio.CadastroModel.CadastroModelImagem;
import com.devmaker.siftkin.webservice.model.request.CadastroRequest;
import com.devmaker.siftkin.webservice.model.request.GetCidadesRequest;
import com.devmaker.siftkin.webservice.model.request.GetEstadosRequest;
import com.devmaker.siftkin.webservice.model.request.UploadImagemRequest;
import com.devmaker.siftkin.webservice.retrofit.ApiManager;
import com.devmaker.siftkin.webservice.retrofit.CustomCallback;
import com.devmaker.siftkin.webservice.retrofit.RequestInterfaceUser;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CadastrarActivity extends AppCompatActivity {

    CircleImageView foto;
    String imgFile = "";
    CadastroModel cadastroModel;
    Spinner estado_sp,cidade_sp;
    EditText nome,cpf,email,telefone,senha,confirmarSenha;
    RadioButton masculino,feminino;
    List<File> imageFilesResend;
    ImageView nomeWarning, emailWarning, senhaWarning, confirmarSenhaWarning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Toolbar toolbar = findViewById(R.id.toolbarcadastro);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        vinculaComponentes();
        getEstadosRequest();

        cpf.addTextChangedListener(MaskEditUtil.mask(cpf,MaskEditUtil.FORMAT_CPF));
        telefone.addTextChangedListener(MaskEditUtil.mask(telefone,MaskEditUtil.FORMAT_FONE));

        estado_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCidadesRequest(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(CadastrarActivity.this).withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    openBottomSheetImagemCamera();
                                }else if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).onSameThread().check();
            }
        });

        nome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && nome.getText().length() < 1){
                    nomeWarning.setVisibility(View.VISIBLE);
                }else{
                    nomeWarning.setVisibility(View.GONE);
                }
            }
        });

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && email.getText().length() < 1){
                    emailWarning.setVisibility(View.VISIBLE);
                }else{
                    emailWarning.setVisibility(View.GONE);
                }
            }
        });

        cadastroModel = new CadastroModel();
        Button cadastrar = findViewById(R.id.finalizarcadastro_btn);
        cadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean nomeBool, emailBool, senhaBool;
                if(!nome.getText().toString().equals("")){
                    cadastroModel.setNome(nome.getText().toString());
                    nomeBool = true;
                }else{
                    nomeBool = false;
                }

                if(!email.getText().toString().equals("")){
                    cadastroModel.setEmail(email.getText().toString());
                    emailBool = true;
                }else{
                    emailBool = false;
                }

                if(masculino.isChecked()){
                    cadastroModel.setSexo("M");
                }else if(feminino.isChecked()){
                    cadastroModel.setSexo("F");
                }

                if(!senha.getText().toString().equals("") && !confirmarSenha.getText().toString().equals("")
                        && confirmarSenha.getText().toString().equals(senha.getText().toString())){
                    cadastroModel.setSenha(senha.getText().toString());
                    senhaBool = true;
                }else {
                    senhaBool = false;
                }

                if (!nomeBool){
                    nomeWarning.setVisibility(View.VISIBLE);
                    new AlertDialog.Builder(CadastrarActivity.this,R.style.AlertDialogTheme)
                            .setMessage("Digite um nome válido.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }else if (!emailBool){
                    emailWarning.setVisibility(View.VISIBLE);
                    new AlertDialog.Builder(CadastrarActivity.this,R.style.AlertDialogTheme)
                            .setMessage("Digite um e-mail válido.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }else if (!senhaBool){
                    senhaWarning.setVisibility(View.VISIBLE);
                    confirmarSenhaWarning.setVisibility(View.VISIBLE);
                    new AlertDialog.Builder(CadastrarActivity.this,R.style.AlertDialogTheme)
                            .setMessage("Campo de senha em branco ou elas não coincidem. Verifique se as senhas estão iguais e tente novamente.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }else {
                    nomeWarning.setVisibility(View.GONE);
                    emailWarning.setVisibility(View.GONE);
                    senhaWarning.setVisibility(View.GONE);
                    confirmarSenhaWarning.setVisibility(View.GONE);

                    CadastroModelImagem cadastroModelImagem = new CadastroModelImagem();
                    cadastroModelImagem.setFotoPerfil(imgFile);
                    cadastroModel.setImagem(cadastroModelImagem);
                    cadastroModel.setCpf(cpf.getText().toString());
                    cadastroModel.setTelefone(telefone.getText().toString());

                    CadastroModelEndereco endereco = new CadastroModelEndereco();
                    //TODO pegar latitude e longitude

                    endereco.setLatitude("0");
                    endereco.setLongitude("0");
                    endereco.setNumber("0");

                    if(estado_sp.getSelectedItem().toString().equals("Todos")) {
                        endereco.setCity("");
                        endereco.setState("");
                    }else{
                        endereco.setCity(cidade_sp.getSelectedItem().toString());
                        endereco.setState(estado_sp.getSelectedItem().toString());
                    }

                    endereco.setZipCode("");
                    endereco.setComplement("");
                    endereco.setNeighborhood("");
                    endereco.setStreet("");

                    cadastroModel.setEndereco(endereco);
                    cadastrarRequest(cadastroModel);
                }
            }
        });
    }

    private void vinculaComponentes() {
        nome = findViewById(R.id.nomecadastro_et);
        cpf = findViewById(R.id.cpfcadastro_et);
        email = findViewById(R.id.emailcadastro_et);
        telefone = findViewById(R.id.telefonecadastro_et);
        senha = findViewById(R.id.senhacadastro_et);
        confirmarSenha = findViewById(R.id.confirmarsenhacadastro_et);
        masculino = findViewById(R.id.masculinocadastro_rb);
        feminino = findViewById(R.id.femininocadastro_rb);
        estado_sp = findViewById(R.id.estado_sp);
        cidade_sp = findViewById(R.id.cidade_sp);
        foto = findViewById(R.id.cadastrarfotoperfil_btn);
        nomeWarning = findViewById(R.id.nomewarning_img);
        emailWarning = findViewById(R.id.emailwarning_img);
        senhaWarning = findViewById(R.id.senhawarning_img);
        confirmarSenhaWarning = findViewById(R.id.confirmarsenhawarning_img);
    }

    private void getEstadosRequest(){
        new ApiManager(this)
                .getRetrofit()
                .create(RequestInterfaceUser.class)
                .getEstados()
                .enqueue(new Callback<GetEstadosRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<GetEstadosRequest> call, @NonNull Response<GetEstadosRequest> response) {
                        if (response.body() != null && response.body().getData() != null) {
                            ArrayList<String> spinnerArray = new ArrayList<>();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                spinnerArray.add(response.body().getData().get(i).getSigla());
                            }

                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(CadastrarActivity.this, R.layout.layout_spinneritem, spinnerArray);
                            estado_sp.setAdapter(spinnerArrayAdapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetEstadosRequest> call, @NonNull Throwable t) {
                        getEstadosRequest();
                    }

                });
    }

    private void getCidadesRequest(int id){
        new ApiManager(this)
                .getRetrofit()
                .create(RequestInterfaceUser.class)
                .getCidades(id)
                .enqueue(new Callback<GetCidadesRequest>() {
                    @Override
                    public void onResponse(@NonNull Call<GetCidadesRequest> call, @NonNull Response<GetCidadesRequest> response) {
                        if (response.body() != null && response.body().getData() != null) {
                            ArrayList<String> spinnerArray = new ArrayList<>();
                            for (int i = 0; i < response.body().getData().size(); i++) {
                                spinnerArray.add(response.body().getData().get(i).getCidades());
                            }
                            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(CadastrarActivity.this, R.layout.layout_spinneritem, spinnerArray);
                            cidade_sp.setAdapter(spinnerArrayAdapter);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<GetCidadesRequest> call, @NonNull Throwable t) {

                    }
                });
    }

    private void cadastrarRequest(CadastroModel model){
        new ApiManager(this)
                .getRetrofit()
                .create(RequestInterfaceUser.class)
                .cadastrar(model)
                .enqueue(new CustomCallback<>(this, new CustomCallback.OnResponse<CadastroRequest>() {
                    @Override
                    public void onResponse(CadastroRequest response) {
                        if (response.getSuccess()){
                            if (response.getData() != null) {
                                new AlertDialog.Builder(CadastrarActivity.this,R.style.AlertDialogTheme)
                                        .setTitle("Parabéns!")
                                        .setCancelable(false)
                                        .setMessage("Cadastro realizado com sucesso")
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                finishAffinity();
                                                startActivity(new Intent(CadastrarActivity.this, LoginActivity.class));
                                            }
                                        }).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception t) {
                        new AlertDialog.Builder(CadastrarActivity.this,R.style.AlertDialogTheme)
                                .setTitle("Atenção")
                                .setMessage(t.getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                    @Override
                    public void onRetry(Exception t) {
                        cadastrarRequest(cadastroModel);
                    }

                }));
    }

    private void uploadImageRequest(final List<File> imageFiles) {
        RequestBody img = RequestBody.create(MediaType.parse("image/*"), imageFiles.get(0));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imageFiles.get(0).getName(), img);
        RequestBody extensao = RequestBody.create(MediaType.parse("text/plain"), "jpg");
        new ApiManager(this)
                .getRetrofit()
                .create(RequestInterfaceUser.class)
                .uploadTemp(body, extensao)
                .enqueue(new CustomCallback<>(this, new CustomCallback.OnResponse<UploadImagemRequest>() {
                    @Override
                    public void onResponse(UploadImagemRequest response) {
                        if (response != null && response.getSuccess()) {
                            if (response.getData() != null && !response.getData().isEmpty()) {
                                File userPhoto = imageFiles.get(0);
                                Glide.with(CadastrarActivity.this)
                                        .load(BitmapFactory.decodeFile(userPhoto.getPath()))
                                        .apply(new RequestOptions().centerCrop())
                                        .into(foto);
                                imgFile = response.getData();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Exception t) {
                        new AlertDialog.Builder(CadastrarActivity.this,R.style.AlertDialogTheme)
                                .setTitle("Atenção")
                                .setMessage(t.getMessage())
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                    @Override
                    public void onRetry(Exception t) {
                        uploadImageRequest(imageFilesResend);
                    }
                }));
    }

    private void openBottomSheetImagemCamera() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_camera, null);
        TextView txtGaleria = view.findViewById(R.id.txtGaleria);
        TextView txtcamera = view.findViewById(R.id.txtCamera);

        final Dialog mBottomSheetDialog = new Dialog(CadastrarActivity.this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        Objects.requireNonNull(mBottomSheetDialog.getWindow()).setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();

        txtGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openGallery(CadastrarActivity.this, 0);
                mBottomSheetDialog.dismiss();
            }
        });

        txtcamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openCamera(CadastrarActivity.this, 1);
                mBottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                imageFilesResend = imageFiles;
                uploadImageRequest(imageFiles);
            }
        });
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarActivity.this);
        builder.setTitle("Permissão");
        builder.setMessage("Precisamos da permissão para ter acesso a câmera");

        builder.setPositiveButton("CONFIGURAÇÕES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });

        builder.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

}