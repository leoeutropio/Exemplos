package com.example.devmakerdiego.rms.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverter;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

@Entity (tableName = "DefeitoInspecao")
public class Defeito implements Serializable {

    @NonNull
    @PrimaryKey
    private String idDefeitoInspecao = UUID.randomUUID().toString();

    @ColumnInfo(name = "id_inspecao")
    private String idInspecao;

    @ColumnInfo(name = "id_defeito")
    private String idDefeito;

    @ColumnInfo(name = "nome_defeito")
    private String nomeDefeito;

    @ColumnInfo(name = "localizacao_defeito")
    private String localizacaoDefeito;

    @ColumnInfo(name = "medida_defeito")
    private String medidaDefeito;

    @ColumnInfo(name = "unidade_medida_defeito")
    private String unidadeMedidaDefeito;

    @ColumnInfo(name = "extensao_defeito")
    private String extensaoDefeito;

    @ColumnInfo(name = "unidade_extensao")
    private String unidadeExtensao;

    @ColumnInfo(name = "prioridade_defeito")
    private String prioridadeDefeito;

    @ColumnInfo(name = "observacoes_defeito")
    private String observacoesDefeito;

    @Ignore
    @ColumnInfo(name = "imagens_defeito")
    private ArrayList<Imagem> imagensDefeito;

    @ColumnInfo(name = "posicao_defeito")
    private String posicaoDefeito;

    @ColumnInfo(name = "data_registro")
    private String dataRegistro;

    @ColumnInfo(name = "codigo")
    private String codigo;

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getUnidadeExtensao() {
        return unidadeExtensao;
    }

    public void setUnidadeExtensao(String unidadeExtensao) {
        this.unidadeExtensao = unidadeExtensao;
    }

    @NonNull
    public String getIdDefeitoInspecao() {
        return idDefeitoInspecao;
    }

    public void setIdDefeitoInspecao(@NonNull String idDefeitoInspecao) {
        this.idDefeitoInspecao = idDefeitoInspecao;
    }

    public String getIdInspecao() {
        return idInspecao;
    }

    public void setIdInspecao(String idInspecao) {
        this.idInspecao = idInspecao;
    }

    public String getIdDefeito() {
        return idDefeito;
    }

    public void setIdDefeito(String idDefeito) {
        this.idDefeito = idDefeito;
    }

    public String getNomeDefeito() {
        return nomeDefeito;
    }

    public void setNomeDefeito(String nomeDefeito) {
        this.nomeDefeito = nomeDefeito;
    }

    public String getLocalizacaoDefeito() {
        return localizacaoDefeito;
    }

    public void setLocalizacaoDefeito(String localizacaoDefeito) {
        this.localizacaoDefeito = localizacaoDefeito;
    }

    public String getMedidaDefeito() {
        return medidaDefeito;
    }

    public void setMedidaDefeito(String medidaDefeito) {
        this.medidaDefeito = medidaDefeito;
    }

    public String getExtensaoDefeito() {
        return extensaoDefeito;
    }

    public void setExtensaoDefeito(String extensaoDefeito) {
        this.extensaoDefeito = extensaoDefeito;
    }

    public String getPosicaoDefeito() {
        return posicaoDefeito;
    }

    public void setPosicaoDefeito(String posicaoDefeito) {
        this.posicaoDefeito = posicaoDefeito;
    }

    public ArrayList<PosicaoDefeito> getListPosicaoDefeito() {
        try {
            return  new Gson().fromJson(posicaoDefeito, new TypeToken<ArrayList<PosicaoDefeito>>() {}.getType());
        }catch (Exception ex){
            return new ArrayList<>();
        }
    }

    public void setListPosicaoDefeito(ArrayList<PosicaoDefeito> defeitos) {
        this.posicaoDefeito = new Gson().toJson(defeitos);
    }

    public String getPrioridadeDefeito() {
        return prioridadeDefeito;
    }

    public void setPrioridadeDefeito(String prioridadeDefeito) {
        this.prioridadeDefeito = prioridadeDefeito;
    }

    public String getObservacoesDefeito() {
        return observacoesDefeito;
    }

    public void setObservacoesDefeito(String observacoesDefeito) {
        this.observacoesDefeito = observacoesDefeito;
    }

    public ArrayList<Imagem> getImagensDefeito() {
        return imagensDefeito;
    }

    public void setImagensDefeito(ArrayList<Imagem> imagensDefeito) {
        this.imagensDefeito = imagensDefeito;
    }

    public String getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(String dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public String getUnidadeMedidaDefeito() {
        return unidadeMedidaDefeito;
    }

    public void setUnidadeMedidaDefeito(String unidadeMedidaDefeito) {
        this.unidadeMedidaDefeito = unidadeMedidaDefeito;
    }

}