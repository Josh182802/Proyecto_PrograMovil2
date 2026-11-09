package com.example.proyecto_alkaravision.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.proyecto_alkaravision.local.UsuarioEntity

@Dao
interface UsuarioDao {

    @Insert
    suspend fun insertarUsuario(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuarios WHERE usuario = :nombreUsuario LIMIT 1")
    suspend fun buscarUsuario(nombreUsuario: String): UsuarioEntity?

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun contarUsuarios(): Int
}