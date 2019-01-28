package com.example.devmakerdiego.rms.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.airbnb.lottie.animation.content.Content;
import com.example.devmakerdiego.rms.roomdatabase.LocalDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity (tableName = "Inspecao")
public class Inspecao implements Serializable {

    @NonNull
    @PrimaryKey
    private String idInspecao = UUID.randomUUID().toString();

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "data_inspecao")
    private String dataInspecao;

    @ColumnInfo(name = "hora_inspecao")
    private String horaInspecao;

    @ColumnInfo(name = "km_inicial")
    private String kmInicial;

    @ColumnInfo(name = "km_final")
    private String kmFinal;

    @ColumnInfo(name = "tipo_inspecao")
    private String tipoInspecao;

    @ColumnInfo(name = "tipo_execucao")
    private String tipoExecucao;

    @ColumnInfo(name = "nome_empresa")
    private String nomeEmpresa;

    @ColumnInfo(name = "bitola")
    private String bitola;

    @ColumnInfo(name = "localizacao_divisao")
    private String localizacaoDivisao;

    @ColumnInfo(name = "localizacao_subdivisao")
    private String localizacaoSubdivisao;

    @ColumnInfo(name = "ferramentas")
    private String ferramentas;

    @ColumnInfo(name = "sincronizado")
    private boolean sincronizado = false;

    public boolean isSincronizado() {
        return sincronizado;
    }

    public void setSincronizado(boolean sincronizado) {
        this.sincronizado = sincronizado;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @NonNull
    public String getIdInspecao() {
        return idInspecao;
    }

    public void setIdInspecao(@NonNull String idInspecao) {
        this.idInspecao = idInspecao;
    }

    public String getDataInspecao() {
        return dataInspecao;
    }

    public void setDataInspecao(String dataInspecao) {
        this.dataInspecao = dataInspecao;
    }

    public String getHoraInspecao() {
        return horaInspecao;
    }

    public void setHoraInspecao(String horaInspecao) {
        this.horaInspecao = horaInspecao;
    }

    public String getKmInicial() {
        return kmInicial;
    }

    public void setKmInicial(String kmInicial) {
        this.kmInicial = kmInicial;
    }

    public String getKmFinal() {
        return kmFinal;
    }

    public void setKmFinal(String kmFinal) {
        this.kmFinal = kmFinal;
    }

    public String getTipoInspecao() {
        return tipoInspecao;
    }

    public void setTipoInspecao(String tipoInspecao) {
        this.tipoInspecao = tipoInspecao;
    }

    public String getTipoExecucao() {
        return tipoExecucao;
    }

    public void setTipoExecucao(String tipoExecucao) {
        this.tipoExecucao = tipoExecucao;
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    public String getBitola() {
        return bitola;
    }

    public void setBitola(String bitola) {
        this.bitola = bitola;
    }

    public String getLocalizacaoDivisao() {
        return localizacaoDivisao;
    }

    public void setLocalizacaoDivisao(String localizacaoDivisao) {
        this.localizacaoDivisao = localizacaoDivisao;
    }

    public String getLocalizacaoSubdivisao() {
        return localizacaoSubdivisao;
    }

    public void setLocalizacaoSubdivisao(String localizacaoSubdivisao) {
        this.localizacaoSubdivisao = localizacaoSubdivisao;
    }

    public String getFerramentas() {
        return ferramentas;
    }

    public void setFerramentas(String ferramentas) {
        this.ferramentas = ferramentas;
    }

    public ArrayList<Ferramenta> getListFerramentas() {
        try {
            return  new Gson().fromJson(ferramentas, new TypeToken<ArrayList<Ferramenta>>() {}.getType());
        }catch (Exception ex){
            return new ArrayList<>();
        }
    }

    public void setListFerramentas(ArrayList<Ferramenta> ferramentas) {
        this.ferramentas = new Gson().toJson(ferramentas);
    }

}