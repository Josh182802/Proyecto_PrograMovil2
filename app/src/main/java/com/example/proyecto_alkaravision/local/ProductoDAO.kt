package com.example.proyecto_alkaravision.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductoDao {

    @Insert
    suspend fun insertarProducto(producto: ProductoEntity)

    @Query("SELECT * FROM productos ORDER BY nombre ASC")
    fun obtenerProductos(): Flow<List<ProductoEntity>>

    @Query("SELECT * FROM productos WHERE codigo = :codigo COLLATE NOCASE LIMIT 1")
    suspend fun buscarProducto(codigo: String): ProductoEntity?

    @Query("SELECT * FROM productos WHERE id = :id LIMIT 1")
    suspend fun obtenerProductoPorId(id: Int): ProductoEntity?

    @Query("""
        SELECT * FROM productos
        WHERE codigo LIKE '%' || :busqueda || '%'
        OR nombre LIKE '%' || :busqueda || '%'
        OR marca LIKE '%' || :busqueda || '%'
        OR categoria LIKE '%' || :busqueda || '%'
        ORDER BY nombre ASC
    """)
    fun buscarProductosPorTexto(
        busqueda: String
    ): Flow<List<ProductoEntity>>

    @Update
    suspend fun actualizarProducto(producto: ProductoEntity)

    @Delete
    suspend fun eliminarProducto(producto: ProductoEntity)
}
