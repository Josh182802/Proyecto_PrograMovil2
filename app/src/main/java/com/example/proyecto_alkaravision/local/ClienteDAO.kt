
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

    @Query("""SELECT * FROM clientes WHERE TRIM(identidad) COLLATE NOCASE =
          TRIM(:identidad) COLLATE NOCASE LIMIT 1""")
    suspend fun buscarCliente(identidad: String): ClienteEntity?

    @Query("SELECT * FROM clientes WHERE id = :id LIMIT 1")
    suspend fun obtenerClientePorId(id: Int): ClienteEntity?

    @Query("""
        SELECT * FROM clientes
        WHERE nombre LIKE '%' || :busqueda || '%'
        OR identidad LIKE '%' || :busqueda || '%'
        OR telefono LIKE '%' || :busqueda || '%'
        OR correo LIKE '%' || :busqueda || '%'
        OR departamento LIKE '%' || :busqueda || '%'
        OR municipio LIKE '%' || :busqueda || '%'
        OR localidad LIKE '%' || :busqueda || '%'
        OR direccionExacta LIKE '%' || :busqueda || '%'
        ORDER BY nombre ASC
    """)
    fun buscarClientesPorTexto(
        busqueda: String
    ): Flow<List<ClienteEntity>>

    @Update
    suspend fun actualizarCliente(cliente: ClienteEntity)

    @Delete
    suspend fun eliminarCliente(cliente: ClienteEntity)
}
