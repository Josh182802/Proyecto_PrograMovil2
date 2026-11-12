package com.example.proyecto_alkaravision.ui.screens.Consultas

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.proyecto_alkaravision.local.ProductoDao
import com.example.proyecto_alkaravision.local.ProductoEntity
import com.example.proyecto_alkaravision.ui.components.ImagenProducto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ConsultaProductosScreen(
    productoDao: ProductoDao,
    onVolver: () -> Unit,
    onEditar: (ProductoEntity) -> Unit
) {

    var busqueda by rememberSaveable { mutableStateOf("") }

    val productos by productoDao.buscarProductosPorTexto(busqueda).collectAsState(initial = emptyList())

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var productoEliminar by remember {
        mutableStateOf<ProductoEntity?>(null)
    }

    var eliminando by remember {
        mutableStateOf(false)
    }

    val celeste = Color(0xFF16C5FF)
    val azulMarino = Color(0xFF071B3A)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF071B3A),
                        Color(0xFF050914),
                        Color.Black
                    )
                )
            )
            .padding(20.dp)
    ) {

        Button(
            onClick = onVolver,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF087DEB)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Volver"
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text("VOLVER AL MENÚ")
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = "INVENTARIO",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Productos registrados",
            color = celeste,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        // BUSCADOR DE PRODUCTOS
        OutlinedTextField(
            value = busqueda,
            onValueChange = { busqueda = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Buscar por código, nombre, marca o categoría",
                    color = Color.LightGray
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Buscar",
                    tint = celeste
                )
            },
            trailingIcon = {
                if (busqueda.isNotEmpty()) {
                    IconButton(
                        onClick = { busqueda = "" }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Limpiar búsqueda",
                            tint = celeste
                        )
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = celeste,
                unfocusedBorderColor = celeste.copy(alpha = 0.5f),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = celeste,
                focusedLeadingIconColor = celeste,
                unfocusedLeadingIconColor = celeste
            )
        )

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Productos encontrados: ${productos.size}",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(15.dp))

        if (productos.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = null,
                        tint = celeste,
                        modifier = Modifier.size(65.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (busqueda.isNotBlank()) {
                            "No se encontraron productos"
                        } else {
                            "No hay productos registrados"
                        },
                        color = Color.White,
                        fontSize = 16.sp
                    )

                    if (busqueda.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Intenta con otro nombre, código o marca",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )
                    }
                }
            }

        } else {

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                items(
                    items = productos,
                    key = { it.id }
                ) { producto ->

                    TarjetaProducto(
                        producto = producto,
                        onEditar = {
                            onEditar(producto)
                        },
                        onEliminar = {
                            productoEliminar = producto
                        }
                    )
                }
            }
        }
    }

    // VENTANA DE CONFIRMACIÓN PARA ELIMINAR
    productoEliminar?.let { producto ->

        AlertDialog(
            onDismissRequest = {
                if (!eliminando) {
                    productoEliminar = null
                }
            },
            title = {
                Text("Eliminar producto")
            },
            text = {
                Text(
                    "¿Estás seguro de que deseas eliminar " +
                            "\"${producto.nombre}\"? Esta acción no se puede deshacer."
                )
            },
            confirmButton = {

                TextButton(
                    enabled = !eliminando,
                    onClick = {

                        eliminando = true

                        scope.launch {

                            try {

                                withContext(Dispatchers.IO) {
                                    productoDao.eliminarProducto(producto)
                                }

                                Toast.makeText(
                                    context,
                                    "Producto eliminado correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()

                                productoEliminar = null

                            } catch (e: Exception) {

                                Toast.makeText(
                                    context,
                                    "Error al eliminar: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()

                            } finally {

                                eliminando = false
                            }
                        }
                    }
                ) {
                    Text(
                        if (eliminando) "ELIMINANDO..." else "SÍ, ELIMINAR",
                        color = Color.Red
                    )
                }
            },
            dismissButton = {

                TextButton(
                    enabled = !eliminando,
                    onClick = {
                        productoEliminar = null
                    }
                ) {
                    Text("CANCELAR")
                }
            }
        )
    }
}

@Composable
fun TarjetaProducto(
    producto: ProductoEntity,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF10294A)
        )
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            // FOTOGRAFÍA DEL PRODUCTO
            ImagenProducto(
                rutaFoto = producto.foto,
                modifier = Modifier.fillMaxWidth(),
                altura = 180
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = producto.nombre,
                color = Color.White,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            DatoProducto("Código", producto.codigo)
            DatoProducto("Categoría", producto.categoria)
            DatoProducto("Marca", producto.marca)
            DatoProducto(
                "Precio",
                "L. ${"%.2f".format(producto.precio)}"
            )
            DatoProducto("Cantidad", producto.cantidad.toString())
            DatoProducto("Descripción", producto.descripcion)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onEditar,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF087DEB)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("EDITAR PRODUCTO")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onEliminar,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD32F2F)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {

                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("ELIMINAR PRODUCTO")
            }
        }
    }
}

@Composable
fun DatoProducto(
    titulo: String,
    valor: String
) {

    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {

        Text(
            text = titulo,
            color = Color(0xFF16C5FF),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = valor,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
