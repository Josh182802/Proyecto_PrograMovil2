package com.example.proyecto_alkaravision.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ClienteDao {

    @Insert
    suspend fun insertarCliente(cliente: ClienteEntity)

    @Query("SELECT * FROM clientes ORDER BY nombre ASC")
    fun obtenerClientes(): Flow<List<ClienteEntity>>

    @Query("SELECT * FROM clientes WHERE identidad = :identidad LIMIT 1")
    suspend fun buscarCliente(identidad: String): ClienteEntity?

    @Update
    suspend fun actualizarCliente(cliente: ClienteEntity)

    @Delete
    suspend fun eliminarCliente(cliente: ClienteEntity)
}
