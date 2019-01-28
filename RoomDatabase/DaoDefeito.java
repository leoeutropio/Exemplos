package com.example.devmakerdiego.rms.roomdatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.devmakerdiego.rms.model.Defeito;

import java.util.List;

@Dao
public interface DaoDefeito {
    @Query("SELECT * FROM DefeitoInspecao")
    List<Defeito> getAll();

    @Query("SELECT * FROM DefeitoInspecao WHERE idDefeitoInspecao IN (:idDefeito)")
    List<Defeito> loadAllByIds(int[] idDefeito);

    @Query("SELECT * FROM DefeitoInspecao WHERE nome_defeito LIKE :nomeDefeito LIMIT 1")
    Defeito findByName(String nomeDefeito);

    @Query("SELECT * FROM DefeitoInspecao WHERE id_inspecao IN (:idInspecao)")
    List<Defeito> getDefeitosInspecao(String idInspecao);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Defeito defeito);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Defeito... defeitos);

    @Update
    void update(Defeito defeito);

    @Delete
    void delete(Defeito defeito);

}