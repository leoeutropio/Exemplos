package com.example.devmakerdiego.rms.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.devmakerdiego.rms.model.Defeito;
import com.example.devmakerdiego.rms.model.Divisao;
import com.example.devmakerdiego.rms.model.Ferramenta;
import com.example.devmakerdiego.rms.model.Imagem;
import com.example.devmakerdiego.rms.model.Inspecao;
import com.example.devmakerdiego.rms.model.PosicaoDefeito;
import com.example.devmakerdiego.rms.model.Subdivisao;

@Database(entities = {Inspecao.class, Ferramenta.class, Defeito.class, Divisao.class, Subdivisao.class, Imagem.class, PosicaoDefeito.class}, version = 11, exportSchema = false)
public abstract class LocalDatabase extends RoomDatabase {

    public abstract DaoFerramenta daoFerramenta();
    public abstract DaoDefeito daoDefeito();
    public abstract DaoDivisao daoDivisao();
    public abstract DaoSubdivisao daoSubdivisao();
    public abstract DaoImagem daoImagem();
    public abstract DaoPosicaoDefeito daoPosicaoDefeito();
    public abstract DaoInspecao daoInspecao();

    private static LocalDatabase localDatabase;
    public static LocalDatabase create(Context context) {
        final String DATABASE_NAME = "rms_db";

        if (localDatabase == null){
            localDatabase = Room.databaseBuilder(context, LocalDatabase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }

        return localDatabase;
    }

}